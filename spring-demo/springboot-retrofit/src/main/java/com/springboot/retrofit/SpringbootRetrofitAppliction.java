package com.springboot.retrofit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * okhttp是一款由square公司开源的java版本http客户端工具。
 * square公司还开源了基于okhttp进一步封装的retrofit工具。
 *
 * retrofit工具,安卓多结合rxjava一起使用
 *
 * 官网:https://square.github.io/retrofit/
 */
@SpringBootApplication
public class SpringbootRetrofitAppliction {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootRetrofitAppliction.class,args);
    }
}
