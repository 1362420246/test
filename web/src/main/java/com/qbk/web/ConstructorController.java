package com.qbk.web;

import com.qbk.service.ConstructorService;
import com.qbk.service.TestService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 *  构造器注入
 */
@RestController
@AllArgsConstructor
public class ConstructorController {

    private TestService testService;

    private ConstructorService constructorService;

    /**
     * 测试构造器注入
     */
    @GetMapping("/constructor")
    public String constructor(){
        testService.into();
        constructorService.into();
        return "s";
    }
}
