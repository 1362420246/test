package com.qbk.dockerfileplugin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * https://github.com/spotify/dockerfile-maven/blob/master/docs/authentication.md
 *
 * 启动容器：
 * docker run -p 8088:8088 -d 39.106.33.10:5000/dockerfile-plugin:1.0.0
 */
@RestController
@SpringBootApplication
public class DockerFilePluginApplication {
    public static void main(String[] args) {
        SpringApplication.run(DockerFilePluginApplication.class,args);
    }

    @GetMapping("/")
    public String get(){
        return "hello dockerfile" ;
    }
}
