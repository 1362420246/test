package com.qbk.configuration;

import com.qbk.configuration.properties.UserProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

/**
 * @author ：quboka
 * @description：读取属性文件
 * @date ：2019/11/28 20:19
 */
@SpringBootApplication
public class ConfigProperties {
    public static void main(String[] args) {
        SpringApplication.run(ConfigProperties.class,args);
    }
    /**
     * 校验 并测试顺序
     */
    @Autowired
    private UserProperties user;

    {  //代码块
        System.out.println(1);
        //null 为注入
        System.out.println(user);
    }
    public ConfigProperties(){
        //构造方法
        System.out.println(2);
        //未注入
        System.out.println(user);
    }
    @PostConstruct
    private void init(){
        //初始化方法
        System.out.println(3);
        //已注入
        System.out.println(user);
    }
    @Bean
    public ApplicationRunner runner(){
        return applicationArguments -> {
            System.out.println(4);
            //已注入
            System.out.println(user);
            System.out.println(user.getRole().getRoleNames());
            user.getRole().getRoleMap().forEach((k,v) -> System.out.println(k+":"+v));
        };
    }
}
