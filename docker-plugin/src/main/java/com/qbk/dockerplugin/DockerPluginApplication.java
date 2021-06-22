package com.qbk.dockerplugin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * docker-maven-plugin 插件 + dockerfile 生成镜像
 * docker启动容器：
 * docker run -p 8090:8090 -d docker-plugin:1.0.0
 */
@RestController
@SpringBootApplication
public class DockerPluginApplication {
    public static void main(String[] args) {
        SpringApplication.run(DockerPluginApplication.class,args);
    }

    @GetMapping("/")
    public String get(){
        return "hello world" ;
    }
}
