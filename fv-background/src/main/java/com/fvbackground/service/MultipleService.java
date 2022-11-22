package com.fvbackground.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fvbackground.common.FruitVegetableSet;
import com.fvbackground.common.Ingredient;
import com.fvbackground.entity.Log;
import com.fvbackground.entity.Multiple;
import com.fvbackground.mapper.MultipleMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;

@Service
public class MultipleService  extends ServiceImpl<MultipleMapper, Multiple> {

    @Resource
    private LogService logService;

    //获取多个果蔬识别结果
    public String multipleIdentity(String accessToken, String file,String username) {
        Ingredient ingredient = new Ingredient();
        String result = ingredient.ingredient(accessToken,file);

        //将string类型转换为JSON，解析里面的属性
        JSONObject object = JSONObject.parseObject(result);
        String logId = object.getString("log_id");
        Integer resuleNum = object.getInteger("result_num");
        List<Multiple> multiples = JSON.parseArray(object.getJSONArray("result").toJSONString(),Multiple.class);
        for (Multiple item:multiples) {
            item.setLogId(logId);
            item.setUsername(username);
        }

        //将日志记录到数据库
        Log log = new Log();
        log.setRecordId(logId);
        log.setUsername(username);
        log.setPath("/multiple");
        log.setPhoto(file);
        logService.save(log);

        //将识别结果存到multiple表中
        saveBatch(multiples);

        //将识别结果返回给前端
        JSONObject obj = new JSONObject();
        obj.put("log_id",logId);
        obj.put("result",multiples);

        return JSON.toJSONString(obj);
    }

    //获取果蔬类别
    public String FVclassification(String accessToken, String file, String username) {
        Ingredient ingredient = new Ingredient();
        String result = ingredient.ingredient(accessToken,file);

        //将string类型转换为JSON，解析里面的属性
        JSONObject object = JSONObject.parseObject(result);
        String logId = object.getString("log_id");
        Integer resuleNum = object.getInteger("result_num");
        List<Multiple> multiples = JSON.parseArray(object.getJSONArray("result").toJSONString(),Multiple.class);
        for (Multiple item:multiples) {
            item.setLogId(logId);
            item.setUsername(username);
            item.setType(new FruitVegetableSet().getType(item.getName()));
        }

        //将日志记录到数据库
        Log log = new Log();
        log.setRecordId(logId);
        log.setUsername(username);
        log.setPath("/classification");
        log.setPhoto(file);
        logService.save(log);

        //将识别结果存到multiple表中
        saveBatch(multiples);

        //将识别结果返回给前端
        JSONObject obj = new JSONObject();
        obj.put("log_id",logId);
        obj.put("result",multiples);

        return JSON.toJSONString(obj);
    }


}

