package com.qbk.pattern.strategy.controller;

import com.qbk.pattern.strategy.serviceimpl.UserQQServiceImpl;
import com.qbk.pattern.strategy.serviceimpl.UserWechatServiceImpl;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 策略模式第四种写法
 * 枚举式
 */
@RestController
public class UserEnumController {

    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/enumget")
    public String enumpget(int key){
        return UserServiceEnum.getInstance(key).login(applicationContext);
    }
}

interface UserFacade{
    String login(ApplicationContext applicationContext);
}

@Getter
enum UserServiceEnum implements UserFacade{
    /**
     * qq
     */
    QQ (1){
        @Override
        public String login(ApplicationContext applicationContext) {
            return applicationContext.getBean(UserQQServiceImpl.class).login();
        }
    },
    /**
     * 微信
     */
    WECHAT(2) {
        @Override
        public String login(ApplicationContext applicationContext) {
            return applicationContext.getBean(UserWechatServiceImpl.class).login();
        }
    };

    private Integer type;

    UserServiceEnum(Integer type) {
        this.type = type;
    }

   public static UserServiceEnum getInstance(int type){
        for (UserServiceEnum serviceEnum: UserServiceEnum.values()) {
            if(type == serviceEnum.getType()){
                return serviceEnum;
            }
        }
        return QQ;
    }
}
