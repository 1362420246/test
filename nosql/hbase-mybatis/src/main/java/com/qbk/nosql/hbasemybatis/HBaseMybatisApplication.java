package com.qbk.nosql.hbasemybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TODO 没有环境 临时放弃
 */
@MapperScan("com.fsl.data.mapper")
@SpringBootApplication
public class HBaseMybatisApplication {
    public static void main(String[] args) {
        SpringApplication.run(HBaseMybatisApplication.class,args);
    }
}
