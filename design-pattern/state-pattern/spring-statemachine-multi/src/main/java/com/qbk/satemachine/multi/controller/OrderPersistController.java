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
 * 状态机 内存 持久化
 */
@RestController
public class OrderPersistController {

    @Autowired
    private OrderStateMachineBuilder orderStateMachineBuilder;

    @Autowired
    private BeanFactory beanFactory;

    @Resource(name="orderMemoryPersister")
    private StateMachinePersister<OrderStates, OrderEvents, String> orderMemorypersister;

    /**
     * 保存状态机
     */
    @RequestMapping("/testMemoryPersister")
    public void tesMemorytPersister(String id) throws Exception {
        StateMachine<OrderStates, OrderEvents> stateMachine = orderStateMachineBuilder.build(beanFactory);
        stateMachine.start();

        //发送PAY事件
        stateMachine.sendEvent(OrderEvents.PAY);
        Order order = new Order();
        order.setOrderId(id);

        //持久化stateMachine
        orderMemorypersister.persist(stateMachine, order.getOrderId());
    }

    /**
     * 取出状态机
     */
    @RequestMapping("/testMemoryPersisterRestore")
    public void testMemoryRestore(String id) throws Exception {
        StateMachine<OrderStates, OrderEvents> stateMachine = orderStateMachineBuilder.build(beanFactory);
        orderMemorypersister.restore(stateMachine, id);
        System.out.println("恢复状态机后的状态为：" + stateMachine.getState().getId());
    }

}
