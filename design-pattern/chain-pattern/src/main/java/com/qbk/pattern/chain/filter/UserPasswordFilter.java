package com.qbk.pattern.chain.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 用户密码校验
 */
@Order(2)
@Component
public class UserPasswordFilter implements UserFilter{
    @Override
    public boolean check(String username, String password) {
        if(!(UserFilter.USERNAME.equals(username) && UserFilter.PASSWORD.equals(password))){
            System.out.println("校验密码失败");
            return false;
        }
        System.out.println("校验密码成功");
        return true;
    }
}
