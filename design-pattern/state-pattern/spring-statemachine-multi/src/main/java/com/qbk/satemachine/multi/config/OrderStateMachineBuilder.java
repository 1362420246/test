package com.qbk.satemachine.multi.config;

import com.qbk.satemachine.multi.enums.OrderEvents;
import com.qbk.satemachine.multi.enums.OrderStates;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

@EnableStateMachine
@Component
public class OrderStateMachineBuilder {

    private final static String MACHINEID = "orderMachine";

    /**
     * 构建状态机
     *
     */
    public StateMachine<OrderStates, OrderEvents> build(BeanFactory beanFactory) throws Exception {
        StateMachineBuilder.Builder<OrderStates, OrderEvents> builder = StateMachineBuilder.builder();

        System.out.println("构建订单状态机");

        //machineId是状态机的配置类和事件实现类的关联，和它关联的是OrderEventConfig
        builder.configureConfiguration()
                .withConfiguration()
                .machineId(MACHINEID)
                .beanFactory(beanFactory);

        builder.configureStates()
                .withStates()
                .initial(OrderStates.UNPAID)
                .states(EnumSet.allOf(OrderStates.class));

        builder.configureTransitions()
                .withExternal()
                .source(OrderStates.UNPAID).target(OrderStates.WAITING_FOR_RECEIVE)
                .event(OrderEvents.PAY).action(action())
                .and()
                .withExternal()
                .source(OrderStates.WAITING_FOR_RECEIVE).target(OrderStates.DONE)
                .event(OrderEvents.RECEIVE);

        return builder.build();
    }

    @Bean
    public Action<OrderStates, OrderEvents> action() {
        return new Action<OrderStates, OrderEvents>() {
            @Override
            public void execute(StateContext<OrderStates, OrderEvents> context) {
                System.out.println(context);
            }
        };
    }

}
