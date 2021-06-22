package com.qbk.amqp.delayed.web;

import com.qbk.amqp.delayed.provider.CancelOrderSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private CancelOrderSender cancelOrderSender;

    @GetMapping
    public String get(long key ,long delayTimes){
        cancelOrderSender.sendMessage(key,delayTimes);
        return "s";
    }

}
