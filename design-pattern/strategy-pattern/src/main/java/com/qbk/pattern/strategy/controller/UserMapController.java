package com.qbk.pattern.strategy.controller;

import com.qbk.pattern.strategy.service.UserService;
import com.qbk.pattern.strategy.serviceimpl.UserQQServiceImpl;
import com.qbk.pattern.strategy.serviceimpl.UserWechatServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 策略模式第三种写法
 * 构造器注入 + map
 */
@RestController
public class UserMapController {

    private final Map<Integer, UserService> userServiceMaps;

    private final UserQQServiceImpl userQQService;

    public UserMapController(UserQQServiceImpl userQQService , UserWechatServiceImpl userWechatService) {
        this.userQQService = userQQService;
        userServiceMaps = new HashMap<>();
        userServiceMaps.put(1,userQQService);
        userServiceMaps.put(2,userWechatService);
    }

    @GetMapping("/mapget")
    public String mapget(int key){
        return userServiceMaps.getOrDefault(key,userQQService).login();
    }
}
