package com.qbk.satemachine.multi.controller;

import com.qbk.satemachine.multi.config.OrderStateMachineBuilder;
import com.qbk.satemachine.multi.entity.Order;
import com.qbk.satemachine.multi.enums.OrderEvents;
import com.qbk.satemachine.multi.enums.OrderStates;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 多状态机 + 传递参数
 */
@RestController
public class OrderController {

    @Autowired
    private OrderStateMachineBuilder orderStateMachineBuilder;

    @Autowired
    private BeanFactory beanFactory;

    /**
     *  次请求testOrderState就会生成一个新的statemachine
     *
     */
    @RequestMapping("/testOrderState")
    public void testOrderState(String id) throws Exception {

        StateMachine<OrderStates, OrderEvents> stateMachine = orderStateMachineBuilder.build(beanFactory);

        // 创建流程
        stateMachine.start();

        // 触发PAY事件
        stateMachine.sendEvent(OrderEvents.PAY);

        // 触发RECEIVE事件 传递参数
        Order order = new Order(id, "RECEIVE");
        //Message。它其实不是spirng statemachine专属的，它是spring里面通用的一种消息工具
        //把状态塞到message的payload里面，然后把需要传递的业务数据（例子里面就是order对象）塞到header里面。
        // 创建message用的是messagebuilder，看它的名字就知道是专门创建message的
        //建了message后，状态机sendEvent就可以不只是传一个event，可以组合event（OrderEvents.RECEIVE）和数据内容（order）一起发送给状态机变化的处理类eventconfig了
        Message<OrderEvents> message = MessageBuilder.withPayload(OrderEvents.RECEIVE).setHeader("order", order).build();
        stateMachine.sendEvent(message);

        // 获取最终状态
        System.out.println("最终状态：" + stateMachine.getState().getId());
    }

}
