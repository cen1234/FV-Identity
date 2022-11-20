package com.fvbackground.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fvbackground.entity.Similar;
import com.fvbackground.service.SimilarService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;


@RestController
@RequestMapping("/similar")
public class SimilarController {

    @Resource
    private SimilarService similarService;


//    ---------
//    图片入库，将本地图片传入百度ai图片库中
//    ---------
    @GetMapping("/add")
    public String SimilarAdd(@RequestParam String token, @RequestParam String file,@RequestParam String username) {
        return similarService.SimilarAdd(token,file,username);
    }

//    --------
//    从ai图库中检索相似图片
//    --------
     @GetMapping("/search")
    public String getSimilar(@RequestParam String token,@RequestParam String file,@RequestParam String username) {
        return similarService.getSimilar(token,file,username);
     }


//     ----------
//     获取数据库中的相似图片
//     ----------
    @GetMapping("/page")
    public IPage<Similar> getSimilar(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String logId) {
        IPage<Similar> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Similar> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("log_id",logId);
        queryWrapper.gt("score",0.5);
        return similarService.page(page,queryWrapper);
    }
}
