package com.qbk.dozermapper.controller;

import com.qbk.dozermapper.bean.User;
import com.qbk.dozermapper.bean.UserVO;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;

@RestController
public class TestContoller {

    @Autowired
    private DozerBeanMapper dozerBeanMapper;

    @GetMapping("/")
    public UserVO get(){
        return dozerBeanMapper.map(
                User.builder()
                        .id(1)
                        .name("kk")
                        .roles(Collections.singletonList("loser"))
                        .build(),
                UserVO.class);
    }
}
