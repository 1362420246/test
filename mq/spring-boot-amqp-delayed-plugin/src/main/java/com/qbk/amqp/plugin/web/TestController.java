package com.qbk.amqp.plugin.web;

import com.qbk.amqp.plugin.provider.RabbitProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class TestController {

    @Autowired
    private RabbitProduct rabbitProduct;

    @GetMapping
    public String get(long delayTimes){
        rabbitProduct.sendMessage(Arrays.asList(222,333),delayTimes);
        return "s";
    }

}
