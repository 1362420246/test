package com.qbk.boot.init.postconstruct2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 初始化c 触发c的 PostConstruct 方法
 */
@RestController
public class WebController {

    @Autowired
    private ApplicationContext context;

    @GetMapping("/init")
    public String init(){
        final PostConstructC bean = context.getBean(PostConstructC.class);
        return "bean c init";
    }
}
