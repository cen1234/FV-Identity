package com.fvbackground.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fvbackground.common.Retrieve;
import com.fvbackground.entity.Log;
import com.fvbackground.entity.Similar;
import com.fvbackground.mapper.SimilarMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;


@Service
public class SimilarService extends ServiceImpl<SimilarMapper, Similar> {

    @Resource
    private LogService logService;

   //图片入库，将本地图片传入百度ai图片库中
    public String SimilarAdd(String token,String file,String username) {
        Retrieve retrieve = new Retrieve();
        String result = retrieve.similarAdd(token,file);

        //将string类型转换为JSON，解析里面的属性
        JSONObject object = JSONObject.parseObject(result);
        String logId = object.getString("log_id");

        //写日志
        Log log = new Log();
        log.setRecordId(logId);
        log.setUsername(username);
        log.setPath("/imageStorage");
        log.setPhoto(file);
        logService.save(log);

        return  result;
    }

    public String getSimilar(String token, String file,String username) {
        Retrieve retrieve = new Retrieve();
        String result =  retrieve.similarSearch(token, file);
        //将string类型转换为JSON，解析里面的属性
        JSONObject object = JSONObject.parseObject(result);
        String logId = object.getString("log_id");
        List<Similar> picture = JSON.parseArray(object.getJSONArray("result").toJSONString(),Similar.class);
        //给检索结果每一项带上logId和username
        for (Similar item:picture) {
            item.setLogId(logId);
            item.setUsername(username);
        }
        //将结果存入similar表中
        saveBatch(picture);

        //写日志
        Log log = new Log();
        log.setRecordId(logId);
        log.setUsername(username);
        log.setPath("/similar");
        log.setPhoto(file);
        logService.save(log);

        //将检索结果返回给前端
        //为什么这里要重新定义一个json对象？因为如果按照原result返回去，前端的resutl.log_id会因为不是string类型而改变数据大小，
        // 比如检索出来的log_id是xxxx1234，返到前台就成了xxxx1000,这对于前台按照log_id分页获取表格数据是致命的。
        JSONObject obj = new JSONObject();
        obj.put("log_id",logId);
        obj.put("result",picture);

        return JSON.toJSONString(obj);
    }
}
