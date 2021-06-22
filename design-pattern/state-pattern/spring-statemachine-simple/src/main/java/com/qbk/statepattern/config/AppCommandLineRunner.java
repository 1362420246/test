package com.qbk.statepattern.config;

import com.qbk.statepattern.enums.Events;
import com.qbk.statepattern.enums.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

@Component
public class AppCommandLineRunner implements CommandLineRunner {

    @Autowired
    private StateMachine<States, Events> stateMachine;

    @Override
    public void run(String... args) throws Exception {
        /*
        start() 就是创建这个订单流程，根据之前的定义，该订单会处于待支付状态，然后通过调用 sendEvent(Events.PAY) 执行支付操作，
        最后通过掉用 sendEvent(Events.RECEIVE) 来完成收货操作。
         */
        stateMachine.start();
        if(true){
            stateMachine.sendEvent(Events.PAY);
            stateMachine.sendEvent(Events.RECEIVE);
        }else {
            stateMachine.sendEvent(Events.CANCEL);
        }
    }
}
