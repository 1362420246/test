package com.qbk.jdbcdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * jdbctemplate
 */
@MapperScan("com.qbk.jdbcdemo.mapper")
@SpringBootApplication
public class JdbcDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(JdbcDemoApplication.class,args);
    }
}
