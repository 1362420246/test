package com.qbk.satemachine.multi.config;

import com.qbk.satemachine.multi.enums.OrderEvents;
import com.qbk.satemachine.multi.enums.OrderStates;
import com.qbk.satemachine.multi.store.InMemoryStateMachinePersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

/**
 * 内存持久化 状态机 配置
 *
 * 先生成一个StateMachinePersist，这里是InMemoryStateMachinePersist，
 * 然后再包装输出StateMachinePersister
 * 最后在controller里面 调用 StateMachinePersister 的 persist 和 restore 方法持久化 StateMachine
 */
@Configuration
public class PersistConfig {


    @Autowired
    private InMemoryStateMachinePersist inMemoryStateMachinePersist;

    /**
     * 注入StateMachinePersister对象
     *
     * StateMachinePersister是可以直接保存StateMachine对象的
     */
    @Bean(name="orderMemoryPersister")
    public StateMachinePersister<OrderStates, OrderEvents, String> getPersister() {
        return new DefaultStateMachinePersister<>(inMemoryStateMachinePersist);
    }

}