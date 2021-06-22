package com.qbk.sql.transaction;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 测试事务隔离级别
 */
@SpringBootApplication
public class SqlTransactionAppliction {
    public static void main(String[] args) {
        SpringApplication.run(SqlTransactionAppliction.class,args);
    }
}
