package com.qbk.springboot.log4j.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Java Lookup:
 * ${java:version}
 * ${java:runtime}
 * ${java:vm}
 * ${java:os}
 * ${java:locale}
 * ${java:hw}
 *
 * springboot复现方式:
 * 1.排除spring-boot-starter-logging
 * 2.依赖spring-boot-starter-log4j2
 *
 * springboot修复方式:
 *  <log4j2.version>2.15.0</log4j2.version>
 * 或者使用:
 *  logback
 */
@Slf4j
@RestController
public class LogController {

    public static Logger logger = LoggerFactory.getLogger(LogController.class);

    @PostMapping("/login")
    public String login(
            @RequestParam("username")String username,
            @RequestParam("password")String password ){
        //log4j输出用户名
        log.info("当前登录:{}",username);
        logger.info("当前登录:{}",username);
        if("qbk".equals(username) && "123".equals(password)){
            return "登录成功";
        }
        return "登录失败";
    }
}
