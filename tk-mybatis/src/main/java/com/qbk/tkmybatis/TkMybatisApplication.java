package com.qbk.tkmybatis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//扫描mapper
@tk.mybatis.spring.annotation.MapperScan(basePackages = "com.qbk.tkmybatis.mapper")
public class TkMybatisApplication {
    public static void main(String[] args) {
        SpringApplication.run(TkMybatisApplication.class,args);
    }
}
