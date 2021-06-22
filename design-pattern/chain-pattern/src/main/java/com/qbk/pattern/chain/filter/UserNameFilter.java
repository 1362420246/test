package com.qbk.pattern.chain.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 用户名称校验
 */
@Order(1)
@Component
public class UserNameFilter implements UserFilter {
    @Override
    public boolean check(String username, String password) {
        if(!UserFilter.USERNAME.equals(username)){
            System.out.println("校验名称失败");
            return false;
        }
        System.out.println("校验名称成功");
        return true;
    }
}
