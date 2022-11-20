package com.fvbackground.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fvbackground.entity.Multiple;
import com.fvbackground.entity.ProductInformation;
import com.fvbackground.entity.Single;
import com.fvbackground.service.ProductInformationService;
import com.fvbackground.service.SingleService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;


@RestController
@RequestMapping("/single")
public class SingleIdentifyController {

    @Resource
    private SingleService singleService;

    @Resource
    private ProductInformationService productInformationService;


//    ------
//    单个识别
//    ------
    @GetMapping("/identity")
    public String SingleIdentity(@RequestParam String accessToken,@RequestParam String file) {
          return singleService.SingleIdentity(accessToken,file);
    }


//    --------
//    将识别结果存储到数据库中
//    --------
    @PostMapping
    public boolean save(@RequestBody Single single) {
        return singleService.Save(single);
    }


//    ---------
//    获取从百度百科爬到的内容
//    ---------
    @GetMapping("/introduction")
    public String getIntroduction(@RequestParam String search,
                                  @RequestParam String logId,
                                  @RequestParam String username) throws UnsupportedEncodingException {
       return singleService.getIntroduction(search,logId,username);
    }

//    --------
//    获取从京东爬到的价格信息
//    --------
    @GetMapping("/price")
    public String getPrice(@RequestParam String search,
                                  @RequestParam String logId,
                                  @RequestParam String username) throws UnsupportedEncodingException {
        return singleService.getPrice(search,logId,username);
    }


//    ---------
//    获取数据库中的结果
//    ---------
    @GetMapping("page")
    public IPage<ProductInformation> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam String username,
                                    @RequestParam String search) {
        IPage<ProductInformation> page = new Page<>(pageNum,pageSize);
        QueryWrapper<ProductInformation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        queryWrapper.eq("name",search);
        return productInformationService.page(page,queryWrapper);
    }

}
