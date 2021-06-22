package com.qbk.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *  属性文件
 */
@Component// 以组件的方式使用，使用的时候可以直接注入
@ConfigurationProperties(prefix="info.test")// 用来指定properties配置文件中的key前缀
//@PropertySource("classpath:jdbc.properties")// 用来指定配置文件的位置
@Data
public class TestProperties {
    private String name ;
    private Integer age;
}
