package com.fvbackground.common;

import com.fvbackground.utils.Base64Util;
import com.fvbackground.utils.FileUtil;
import com.fvbackground.utils.HttpUtil;

import java.net.URLEncoder;

//用于相似图片检索功能
public class Retrieve {

    private String fileUploadPath = "C:/Users/cxy/Desktop/创新综合实践/FV/files/";

    //入库
    public  String similarAdd(String Token,String file) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/realtime_search/similar/add";
        try {
            // 本地文件路径
            String filePath = fileUploadPath + file;
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "brief=" + file + "&image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = Token;

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //检索
    public  String similarSearch(String token,String file) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/realtime_search/similar/search";
        try {
            // 本地文件路径
            String filePath =fileUploadPath + file;;
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam + "&pn=" + 0 + "&rn=" + 30;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = token;

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
