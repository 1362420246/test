package com.qbk.configuration.controller;

import com.qbk.configuration.properties.Authentication;
import com.qbk.configuration.properties.RoleProperties;
import com.qbk.configuration.properties.UserProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ：quboka
 * @description：
 * @date ：2019/12/27 16:45
 */
@RestController
public class PropertiesController {

    @Autowired
    private UserProperties properties;

    @GetMapping("/get")
    public String get(){
        String name = properties.getName();
        System.out.println(name);
        List<Authentication> authentication = properties.getAuthentication();
        System.out.println(authentication);
        return "S";
    }
}
