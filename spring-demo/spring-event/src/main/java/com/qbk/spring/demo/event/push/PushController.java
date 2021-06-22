package com.qbk.spring.demo.event.push;

import com.qbk.spring.demo.event.event.IpEvent;
import com.qbk.spring.demo.event.event.LoginEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 发布事件
 */
@RestController
public class PushController {

    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/")
    public String push(){
        Map<String,String> ipMap = new HashMap<>();
        ipMap.put("ip","127.0.0.1");

        Map<String,String> loginMap = new HashMap<>();
        loginMap.put("name","kk");

        //发送不同的事件
        applicationContext.publishEvent(new IpEvent(ipMap));
        applicationContext.publishEvent(new LoginEvent(loginMap));

        return LocalDateTime.now().toString();
    }
}
