package com.qbk.satemachine.multi.store;
import java.util.HashMap;
import java.util.Map;

import com.qbk.satemachine.multi.enums.OrderEvents;
import com.qbk.satemachine.multi.enums.OrderStates;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.stereotype.Component;

/**
 * 在内存中持久化状态机
 *
 * StateMachinePersist 是存储 StateMachine 的
 */
@Component
public class InMemoryStateMachinePersist implements StateMachinePersist<OrderStates, OrderEvents, String> {

    private Map<String, StateMachineContext<OrderStates, OrderEvents>> map = new HashMap<>();

    @Override
    public void write(StateMachineContext<OrderStates, OrderEvents> context, String contextObj) throws Exception {
        map.put(contextObj, context);
    }

    @Override
    public StateMachineContext<OrderStates, OrderEvents> read(String contextObj) throws Exception {
        return map.get(contextObj);
    }

}