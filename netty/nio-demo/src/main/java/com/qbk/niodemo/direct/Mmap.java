package com.qbk.niodemo.direct;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.Cleaner;
import sun.nio.ch.DirectBuffer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * mmap内存映射
 * MappedByteBuffer
 */
@RestController
public class Mmap implements ServletContextAware {

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * 上传
     */
    @PostMapping("/update")
    public boolean update(MultipartFile file){
        MappedByteBuffer buffer = null;
        try (
                RandomAccessFile randomAccessFile = new RandomAccessFile("D://data.txt", "rw");
                FileChannel fileChannel = randomAccessFile.getChannel()
        ) {
            // 写入文件
            byte[] fileBytes = file.getBytes();
            buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileBytes.length);
            buffer.put(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            // 释放
            if(buffer != null){
                Cleaner cleaner = ((DirectBuffer)buffer).cleaner();
                if(cleaner != null) {
                    cleaner.clean();
                }
            }
        }
        return true;
    }

    /**
     * 下载 request
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> download(HttpServletRequest request){
        // 获取文件路径
        //String filePath = "file://" + System.getProperty("user.dir") + "/" + fileName ;
        String filePath = "D://data.txt";
        Resource resource = new FileSystemResource(filePath);

        if (!resource.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        HttpHeaders headers = new HttpHeaders();
        MappedByteBuffer buffer = null;
        RandomAccessFile randomAccessFile = null;
        FileChannel fileChannel = null;
        byte[] bytes = new byte[0];
        try {
            // 设置文件响应头
            String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentDispositionFormData("attachment", resource.getFilename());
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            // 使用MappedByteBuffer实现文件下载
            File file = resource.getFile();
            randomAccessFile = new RandomAccessFile(file, "r");
            fileChannel = randomAccessFile.getChannel();
            buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            bytes = new byte[(int) file.length()];
            buffer.get(bytes);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // 释放
            if(buffer != null){
                Cleaner cleaner = ((DirectBuffer)buffer).cleaner();
                if(cleaner != null) {
                    cleaner.clean();
                }
            }
            if(fileChannel != null){
                try {
                    fileChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(randomAccessFile != null){
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new ResponseEntity<>(new ByteArrayResource(bytes), headers, HttpStatus.OK);
    }

    /**
     * 下载  response
     */
    @GetMapping("/download2")
    public void download2(HttpServletResponse response){
        // 获取文件路径
        //String filePath = "file://" + System.getProperty("user.dir") + "/" + fileName;
        String filePath = "D://data.txt";
        Resource resource = new FileSystemResource(filePath);

        if (!resource.exists()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }

        MappedByteBuffer buffer = null;
        RandomAccessFile randomAccessFile = null;
        FileChannel fileChannel = null;
        try {
            // 设置文件响应头
            String contentType = servletContext.getMimeType(resource.getFile().getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            response.setContentType(contentType);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
            response.setHeader(HttpHeaders.CACHE_CONTROL, "must-revalidate, post-check=0, pre-check=0");

            // 使用MappedByteBuffer实现文件下载
            File file = resource.getFile();
            randomAccessFile = new RandomAccessFile(file, "r");
            fileChannel = randomAccessFile.getChannel();
            buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            byte[] bytes = new byte[(int) file.length()];
            buffer.get(bytes);

            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // 释放
            if(buffer != null){
                Cleaner cleaner = ((DirectBuffer)buffer).cleaner();
                if(cleaner != null) {
                    cleaner.clean();
                }
            }
            if(fileChannel != null){
                try {
                    fileChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(randomAccessFile != null){
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
