package com.fvbackground.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fvbackground.common.Craw;
import com.fvbackground.common.Ingredient;
import com.fvbackground.entity.Log;
import com.fvbackground.entity.ProductInformation;
import com.fvbackground.entity.Single;
import com.fvbackground.mapper.SingleMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.List;


@Service
public class SingleService extends ServiceImpl<SingleMapper, Single> {
    @Resource
    private LogService logService;

    @Resource
    private ProductInformationService productInformationService;



    //获取单个果蔬识别结果
    public String SingleIdentity(String accessToken, String file) {
        Ingredient ingredient = new Ingredient();
        return ingredient.ingredient(accessToken,file);
    }

    //将识别结果存储到数据库中
    public boolean Save(Single single) {
        //把日志写入log表中
        Log log = new Log();
        log.setPath("/single");
        log.setUsername(single.getUsername());
        log.setRecordId(single.getLogId());
        log.setPhoto(single.getPhoto());
        log.setResuleNum(single.getResuleNum());
        boolean isLogSave = logService.save(log);

        //把识别结果存入single表中
        boolean isSingleSave =save(single);

        return isLogSave && isSingleSave;
    }

    //获取从百度百科爬到的内容
    public String getIntroduction(String search, String logId, String username) throws UnsupportedEncodingException {
        //爬内容
        String result = Craw.CrawlContent(search);
        //将string类型转换为JSON，解析里面的属性
        JSONObject object = JSONObject.parseObject(result);
        //对数据库中的这份数据进行修改，添加爬到的内容
        QueryWrapper<Single> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("log_id",logId);
        queryWrapper.eq("username",username);
        Single single = getOne(queryWrapper);
        single.setTitle(object.getString("title"));
        single.setDescription(object.getString("description"));
        single.setBenefit(object.getString("benefit"));
        //更新数据库
        updateById(single);
        //返回结果到前端
        return result;
    }

   //获取从京东爬到的价格信息
    public String getPrice(String search, String logId, String username) throws UnsupportedEncodingException {
        //爬内容
        String result = Craw.CrawlPrice(search);
        //将string类型转换为JSON，解析里面的属性
        JSONObject object = JSONObject.parseObject(result);
        List<ProductInformation> product = JSON.parseArray(object.getJSONArray("goods").toJSONString(),ProductInformation.class);
        //找价格最小的
        double minprice = Double.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < product.size(); i++) {
            if (product.get(i).getPrice() < minprice) {
                minprice = product.get(i).getPrice();
                index = i;
            }
        }
        //先查询production_information表中是否存在search信息，存在就不再存进去了
        QueryWrapper<ProductInformation> queryWrapperProduct = new QueryWrapper<>();
        queryWrapperProduct.eq("name",search);
        List<ProductInformation> list = productInformationService.list(queryWrapperProduct);
        if (list.size() == 0) {
            //将logId，username信息存到product每一项中
            for (ProductInformation item:product) {
                item.setLogId(logId);
                item.setUsername(username);
                item.setName(search);
            }
            //将商品的信息存到production_information表
            productInformationService.saveBatch(product,30);
        }
        //更新single表与商品相关的信息
        QueryWrapper<Single> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("log_id",logId);
        queryWrapper.eq("username",username);
        Single single = getOne(queryWrapper);
        single.setPrice(product.get(index).getPrice());
        single.setLink(product.get(index).getLink());
        single.setShop(product.get(index).getShop());
        single.setIntroduction(product.get(index).getIntroduction());
        //更新数据库
        updateById(single);
        return result;
    }
}
