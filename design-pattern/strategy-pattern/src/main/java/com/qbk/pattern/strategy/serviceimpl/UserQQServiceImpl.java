package com.qbk.pattern.strategy.serviceimpl;

import com.qbk.pattern.strategy.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserQQServiceImpl implements UserService {
    @Override
    public String login() {
        return "QQ";
    }
}
