package com.qbk.spring.demo.external;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * spring外部参数、jar包瘦身
 * java -jar -Dloader.path="lib/" -DNAME=kk  spring-boot-external-1.0.0.jar
 */
@SpringBootApplication
public class SpringExternalApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringExternalApplication.class,args);
    }
}
