package com.qbk.pattern.strategy.config;

import com.qbk.pattern.strategy.service.UserService;
import com.qbk.pattern.strategy.serviceimpl.UserQQServiceImpl;
import com.qbk.pattern.strategy.serviceimpl.UserWechatServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class UserConfig {

    /**
     * 策略容器
     */
    @Bean
    public Map<Integer, UserService> userServiceMap(
            UserQQServiceImpl userQQService,
            UserWechatServiceImpl userWechatService) {
        Map<Integer, UserService> userServiceMap = new HashMap<>();
        userServiceMap.put(1,userQQService);
        userServiceMap.put(2,userWechatService);
        return userServiceMap;
    }
}
