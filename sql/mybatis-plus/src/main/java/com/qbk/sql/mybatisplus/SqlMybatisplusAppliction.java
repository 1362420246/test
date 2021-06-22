package com.qbk.sql.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 测试事务隔离级别
 */
@MapperScan("com.qbk.sql.mybatisplus.mapper")
@SpringBootApplication
public class SqlMybatisplusAppliction {
    public static void main(String[] args) {
        SpringApplication.run(SqlMybatisplusAppliction.class,args);
    }
}
