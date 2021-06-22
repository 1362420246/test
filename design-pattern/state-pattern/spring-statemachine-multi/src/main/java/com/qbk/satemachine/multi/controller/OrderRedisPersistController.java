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
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 状态机 redis 持久化
 */
@RestController
public class OrderRedisPersistController {

    @Autowired
    private OrderStateMachineBuilder orderStateMachineBuilder;

    @Autowired
    private BeanFactory beanFactory;

    @Resource(name="orderRedisPersister")
    private StateMachinePersister<OrderStates, OrderEvents, String> orderRedisPersister;

    /**
     * 保存状态机
     */
    @RequestMapping("/testRedisPersister")
    public void testRedisPersister(String id) throws Exception {
        StateMachine<OrderStates, OrderEvents> stateMachine = orderStateMachineBuilder.build(beanFactory);
        stateMachine.start();
        Order order = new Order();
        order.setOrderId(id);
        //发送PAY事件
        Message<OrderEvents> message = MessageBuilder.withPayload(OrderEvents.PAY).setHeader("order", order).build();
        stateMachine.sendEvent(message);
        //持久化stateMachine
        orderRedisPersister.persist(stateMachine, order.getOrderId());
    }

    /**
     * 取出状态机
     */
    @RequestMapping("/testRedisPersisterRestore")
    public void testRestore(String id) throws Exception {
        StateMachine<OrderStates, OrderEvents> stateMachine = orderStateMachineBuilder.build(beanFactory);
        orderRedisPersister.restore(stateMachine, id);
        System.out.println("恢复状态机后的状态为：" + stateMachine.getState().getId());
    }

}
