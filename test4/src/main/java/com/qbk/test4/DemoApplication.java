package com.qbk.test4;

import org.apache.tomcat.jni.FileInfo;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@SpringBootApplication
@RestController
public class DemoApplication {

    @GetMapping("/get")
    public List<String> get(int id , String name){
        System.out.println("------------------"+id);
        System.out.println("------------------"+name);
        return new ArrayList<>(Arrays.asList("A","B","C"));
    }

    @PostMapping("/post")
    public Map post( @RequestBody Map<String,String> map ){
        System.out.println("------------------"+map);
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("code","201");
        return hashMap;
    }

    private String folder = "D:/code/test/test4/src/main/java/com/qbk/test4";

    /**
     * 上传
     */
    @PostMapping("/file")
    public String upload(MultipartFile file) throws Exception {
        System.out.println(file.getName());
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getSize());

        File localFile = new File(folder, System.currentTimeMillis() + ".txt");
        file.transferTo(localFile);
        return localFile.getAbsolutePath();
    }

    /**
     *  下载
     */
    @GetMapping("/{id}")
    public void download(@PathVariable String id,HttpServletResponse response) throws Exception {

        try (
                InputStream inputStream = new FileInputStream(new File(folder, id + ".txt"));
                OutputStream outputStream = response.getOutputStream();
             ) {

            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=test.txt");

            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
