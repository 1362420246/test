package com.qbk.statepattern.config;

import com.qbk.statepattern.enums.Events;
import com.qbk.statepattern.enums.States;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import java.util.EnumSet;

/**
 * EnableStateMachine注解用来启用Spring StateMachine状态机功能
 */
@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {

    /**
     * 为当前的状态机指定了状态监听器，其中listener()则是调用了下一个内容创建的监听器实例，用来处理各个各个发生的状态迁移事件
     */
    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {
        config
                .withConfiguration()
//                .autoStartup(true)
                .listener(listener());
    }

    /**
     * 初始化当前状态机拥有哪些状态，其中initial(States.UNPAID)定义了初始状态为UNPAID，
     * states(EnumSet.allOf(States.class))则指定了使用上一步中定义的所有状态作为该状态机的状态定义。
     */
    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
        states
                .withStates()
                .initial(States.UNPAID)
                .states(EnumSet.allOf(States.class));
    }

    /**
     * 初始化当前状态机有哪些状态迁移动作，其中命名中我们很容易理解每一个迁移动作，都有来源状态source，
     * 目标状态target以及触发事件event
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
        transitions
                .withExternal()
                .source(States.UNPAID).target(States.WAITING_FOR_RECEIVE)
                .event(Events.PAY)
                .and()
                .withExternal()
                .source(States.WAITING_FOR_RECEIVE).target(States.DONE)
                .event(Events.RECEIVE)
                .and()
                .withExternal()
                .source(States.UNPAID).target(States.DONE)
                .event(Events.CANCEL);
    }

    /**
     * 创建StateMachineListener状态监听器的实例，在该实例中会定义具体的状态迁移处理逻辑，
     *
     * 实现中只是做了一些输出，实际业务场景会会有更负责的逻辑，所以通常情况下，我们可以将该实例的定义放到独立的类定义中，
     * 并用注入的方式加载进来。
     */
    @Bean
    public StateMachineListener<States, Events> listener() {
        return new StateMachineListenerAdapter<States, Events>() {
            @Override
            public void transition(Transition<States, Events> transition) {
                if(transition.getTarget().getId() == States.UNPAID) {
                    System.out.println("订单创建，待支付");
                    return;
                }

                if(transition.getSource().getId() == States.UNPAID
                        && transition.getTarget().getId() == States.WAITING_FOR_RECEIVE) {
                    System.out.println("用户完成支付，待收货");
                    return;
                }

                if(transition.getSource().getId() == States.WAITING_FOR_RECEIVE
                        && transition.getTarget().getId() == States.DONE) {
                    System.out.println("用户已收货，订单完成");
                }

                if(transition.getSource().getId() == States.UNPAID
                        && transition.getTarget().getId() == States.DONE) {
                    System.out.println("用户取消订单，订单完成");
                }
            }

        };
    }
}