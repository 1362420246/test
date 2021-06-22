package com.qbk.source;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 排除数据源自动注入
 */
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
@MapperScan(basePackages = "com.qbk.source.mapper")
public class SpringRoutingDataSource {
    public static void main(String[] args) {
        SpringApplication.run(SpringRoutingDataSource.class,args);
    }
    @Bean
    public ApplicationRunner runner(){
        return applicationArguments -> {
            System.out.println("1");
            System.out.println("2");
            System.out.println("3");
        };
    }
}
