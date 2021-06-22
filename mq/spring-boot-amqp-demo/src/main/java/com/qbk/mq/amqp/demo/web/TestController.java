package com.qbk.mq.amqp.demo.web;

import com.qbk.mq.amqp.demo.dynamic.DynamicQueue;
import com.qbk.mq.amqp.demo.provider.MyProvider;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TestController {

    @Autowired
    private MyProvider provider;

   @GetMapping("/")
    public String send(){
       provider.send();
       return "s";
   }

   @Autowired
   private DynamicQueue dynamicQueue;

    /**
     * 动态创建队列测试
     */
   @GetMapping("/dynamic")
   public String sendDynamic(){
       Exchange exchange = dynamicQueue.createExchange("myDirectExchange");
       Queue queue = dynamicQueue.createQueue("myDirectQueue3");
       String routingKey = "mine.direct";
       dynamicQueue.binding(exchange,queue,routingKey);
       dynamicQueue.sendMessage(exchange,routingKey,"动态绑定测试");
       return "s";
   }

    /**
     * 动态创建队列测试
     */
    @GetMapping("/listener")
    public String listener() throws Exception {
        Queue queue = dynamicQueue.createQueue("myDirectQueue3");
        dynamicQueue.createListener(queue);
        return "s";
    }

}
