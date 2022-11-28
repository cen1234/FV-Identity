package com.fvbackground.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fvbackground.entity.ProductInformation;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Craw {

    //根据关键词爬取百度百科内容
    public static String CrawlContent(String search) throws UnsupportedEncodingException {
        //将中文字符编码
        String encodeStr = URLEncoder.encode(search,"utf-8");

        //1.生成httpclient，相当于该打开一个浏览器
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        //2.创建get请求，相当于在浏览器地址栏输入 网址
        HttpGet request = new HttpGet("https://baike.baidu.com/item/"+encodeStr);

        try {
            //3.执行get请求，相当于在输入地址栏后敲回车键
            response = httpClient.execute(request);

            //4.判断响应状态为200，进行处理
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //5.获取响应内容
                HttpEntity httpEntity = response.getEntity();
                String html = EntityUtils.toString(httpEntity, "utf-8");

                //6.Jsoup解析html
                Document document =  Jsoup.parse(html);

                //将爬取到数据存入json对象中返回
                JSONObject object = new JSONObject();

                //像js一样，通过标签获取title,主要内容，益处
                String title = document.title();
                object.put("title",title);

                String description = document.select("div.lemma-summary").text();
                object.put("description",description);

                if (document.select("ol.para-list").size() >= 3 ) {
                    String benefit  = document.select("ol.para-list").get(2).text();
                    object.put("benefit",benefit);
                }

                return JSON.toJSONString(object);
            } else {
                //如果返回状态不是200，比如404（页面不存在）等，根据情况做处理，这里略
                System.out.println("返回状态不是200");
                System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpClient);
        }
        return null;
    }

    //根据关键词爬取京东的商品价格
    public static String CrawlPrice(String search) throws UnsupportedEncodingException {
        //将中文字符编码
        String encodeStr = URLEncoder.encode(search,"utf-8");

        //1.生成httpclient，相当于该打开一个浏览器
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        //2.创建get请求，相当于在浏览器地址栏输入 网址
        HttpGet request = new HttpGet("http://search.jd.com/Search?keyword="+encodeStr+"&page=1");
        //使用HttpClient爬取数据时, 为了防止被网站拦截, 应该设置请求头，伪装成用户；没有下面这行的话直接爬，结果就要输入密码才能访问京东
        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");

        try {
            //3.执行get请求，相当于在输入地址栏后敲回车键
            response = httpClient.execute(request);

            //4.判断响应状态为200，进行处理
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //5.获取响应内容
                HttpEntity httpEntity = response.getEntity();
                String html = EntityUtils.toString(httpEntity, "utf-8");
//                System.out.println("html"+html);

                //6.Jsoup解析html,获取商品价格，链接，店家，介绍的信息
                Document document =  Jsoup.parse(html);
                Elements p_prices = document.select("div.p-price strong i");
                Elements p_links = document.select("div.p-name a");
                Elements p_shops = document.select("div.p-shop span a");
                Elements p_introductions = document.select("div.p-name a em");

                List<Double> priceList = new ArrayList<Double>();
                List<String> linkList = new ArrayList<String>();
                List<String> shopList = new ArrayList<String>();
                List<String> introductionList = new ArrayList<String>();
                for(Element item:p_prices) {
                    priceList.add(Double.parseDouble(item.text()));
                }
                for(Element item:p_links) {
                    linkList.add(item.attr("href"));
                }
                for(Element item:p_shops) {
                    shopList.add(item.text());
                }
                for(Element item:p_introductions) {
                    introductionList.add(item.text());
                }

                List<ProductInformation> productInformationArrayList = new ArrayList<ProductInformation>();

                for (int i = 0;i <priceList.size();i++) {
                    ProductInformation productInformation = new ProductInformation();
                    productInformation.setPrice(priceList.get(i));
                    productInformation.setLink(linkList.get(i));
                    productInformation.setShop(shopList.get(i));
                    productInformation.setIntroduction(introductionList.get(i));
                    productInformationArrayList.add(productInformation);
                }

                //将爬取到数据存入json对象中返回
                JSONObject object = new JSONObject();
                object.put("goods",productInformationArrayList);
                return JSON.toJSONString(object);
            } else {
                //如果返回状态不是200，比如404（页面不存在）等，根据情况做处理，这里略
                System.out.println("返回状态不是200");
                System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpClient);
        }
        return null;
    }


}
