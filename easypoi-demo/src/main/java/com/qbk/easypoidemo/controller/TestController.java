package com.qbk.easypoidemo.controller;

import cn.afterturn.easypoi.word.WordExportUtil;
import com.qbk.easypoidemo.utils.EasyWordUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试word生成
 */
@RestController
@RequestMapping("/demo")
public class TestController {

    @RequestMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> params = new HashMap<>();
        params.put("title","这是标题");
        params.put("name","李四");
        //这里是我说的一行代码
        EasyWordUtil.exportWord("word/test.docx","C:/Users/86186/Desktop","aaa.docx",params,request,response);
    }

    /**
     * 测试 linux 下 通过 读取模板 直接生成流下载
     */
    @PostMapping("/test")
    public String test( @RequestBody Map<String,Object> map ,HttpServletResponse response){

        try {
            String url = (String) map.get("url");
            if(StringUtils.isBlank(url)){
                url = "/opt/test/word/001.docx" ;
            }
            XWPFDocument doc = WordExportUtil.exportWord07(url, map);
            // 设置强制下载不打开
            response.setContentType("application/force-download");
            // 设置文件名
            response.addHeader("Content-Disposition", "attachment;fileName=" + "kk.docx");
            OutputStream out = response.getOutputStream();
            doc.write(out);
            out.close();
            doc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "s";
    }
}
