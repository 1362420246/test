package com.qbk.pattern.chain.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 用户状态校验
 */
@Order(3)
@Component
public class UserStatusFilter implements UserFilter {
    @Override
    public boolean check(String username, String password) {
        System.out.println("校验状态成功");
        return true;
    }
}
