package com.fvbackground.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fvbackground.entity.Log;
import com.fvbackground.service.LogService;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.SheetUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/log")
public class LogController {

    @Resource
    private LogService logService;

//    ------
//    分页获取
//    ------
    @GetMapping("page")
    public IPage<Log> findPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam(defaultValue = "") String search,
                               @RequestParam(defaultValue = "1") String type) {
        IPage<Log> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Log> queryWrapper = new QueryWrapper<>();
        switch (type)
        {
            case "1": queryWrapper.like("username",search);
                      break;
            case "2": queryWrapper.like("path",search);
                      break;
        }
        return logService.page(page,queryWrapper);
    }


//    --------
//    导出日志数据
//    --------
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
        //从数据库中查出所有数据
        List<Log> list = logService.list();
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题名
        writer.addHeaderAlias("id","ID");
        writer.addHeaderAlias("recordId","log_id");
        writer.addHeaderAlias("username","用户名");
        writer.addHeaderAlias("path","访问功能路径");
        writer.addHeaderAlias("photo","图片");
        writer.addHeaderAlias("resuleNum","识别数目");
        //一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list,true);
        //定义单元格背景色
        StyleSet style = writer.getStyleSet();
        //设置内容字体
        Font font = writer.createFont();
        font.setFontHeightInPoints((short) 12);
        //重点，设置中文字体
        font.setFontName("宋体");
        //第二个参数表示是否忽略头部样式
        style.getHeadCellStyle().setFont(font);
        int columnCount = writer.getColumnCount();
        for (int i = 0; i < columnCount; ++i) {
            double width = SheetUtil.getColumnWidth(writer.getSheet(), i, false);
            if (width != -1.0D) {
                width *= 256.0D;
                //此处可以适当调整，调整列空白处宽度
                width += 220D;
                writer.setColumnWidth(i, Math.toIntExact(Math.round(width / 256D)));
            }
        }
        //设置浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("菜单信息","UTF-8");
        response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");

        ServletOutputStream out=response.getOutputStream();
        writer.flush(out, true);
        out.close();
        // 关闭writer，释放内存
        writer.close();

    }


}
