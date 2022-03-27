package com.qbk.file.controller;

import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * 下载
 */
@RestController
public class DownloadController {

    /**
     * 读取jar内部文件
     * 下载
     */
    @GetMapping("/download")
    public void download(HttpServletResponse response) throws Exception{
        Resource resource = new ClassPathResource("file/template.txt");
        response.setContentType("application/force-download;charset=UTF-8");
        response.addHeader("Content-Disposition", "attachment;fileName=" +  java.net.URLEncoder.encode("来自jar里的文件.txt", "UTF-8"));
//        byte[] buffer = new byte[1024];
        try (BufferedInputStream bis = new BufferedInputStream(resource.getInputStream())) {
            OutputStream os = response.getOutputStream();
//            int i = bis.read(buffer);
//            while (i != -1) {
//                os.write(buffer, 0, i);
//                i = bis.read(buffer);
//            }
            IOUtils.copy(bis,os);
            os.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 从指定url下载文件
     */
    @GetMapping(value = "/file")
    public void fileImport(HttpServletResponse response) throws Exception{

        URL url = new URL("http://172.16.88.141:8080/file/doc/1619685826349.txt");
        //文件名
        String name = FilenameUtils.getName(url.getFile());
        response.setContentType("application/force-download;charset=UTF-8");
        response.addHeader("Content-Disposition", "attachment;fileName=" +  java.net.URLEncoder.encode(name, "UTF-8"));
        try (BufferedInputStream bis = new BufferedInputStream(url.openStream())) {
            OutputStream os = response.getOutputStream();
            IOUtils.copy(bis,os);
            os.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
