package com.qbk.dockerpluginfree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  docker-maven-plugin 免dockerfile 生成镜像
 *
 * docker启动容器：
 * docker run -p 8089:8089 -d docker-plugin-free:1.0.0
 */
@RestController
@SpringBootApplication
public class DockerPluginFreeApplication {
    public static void main(String[] args) {
        SpringApplication.run(DockerPluginFreeApplication.class,args);
    }

    @GetMapping("/")
    public String get(){
        return "hello docker" ;
    }
}
