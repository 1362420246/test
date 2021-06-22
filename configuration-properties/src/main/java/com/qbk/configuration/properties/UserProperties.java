package com.qbk.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
//用来指定properties配置文件中的key前缀
@ConfigurationProperties(prefix="info.user")
public class UserProperties {
    private String name ;
    private Integer age ;
    private String loginName ;
    private RoleProperties role;
    private List<Authentication> authentication;
}
