package com.qbk.satemachine.multi.config;

import com.qbk.satemachine.multi.enums.OrderEvents;
import com.qbk.satemachine.multi.enums.OrderStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.persist.RepositoryStateMachinePersist;
import org.springframework.statemachine.redis.RedisStateMachineContextRepository;
import org.springframework.statemachine.redis.RedisStateMachinePersister;

/**
 * redis持久化 状态机 配置
 *
 * 先生成一个StateMachinePersist，这里是通过RedisConnectionFactory生成RepositoryStateMachinePersist，
 * 然后再包装输出StateMachinePersister，这里是RedisStateMachinePersister。
 * 最后在controller里面 调用 RedisStateMachinePersister 的 persist 和 restore 方法持久化 StateMachine
 */
@Configuration
public class PersistRedisConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * 通过redisConnectionFactory创建StateMachinePersist
     */
    public StateMachinePersist<OrderStates, OrderEvents,String> redisPersist() {
        RedisStateMachineContextRepository<OrderStates, OrderEvents> repository
                = new RedisStateMachineContextRepository<>(redisConnectionFactory);
        return new RepositoryStateMachinePersist<>(repository);
    }

    /**
     * 注入RedisStateMachinePersister对象
     */
    @Bean(name = "orderRedisPersister")
    public RedisStateMachinePersister<OrderStates, OrderEvents> redisPersister() {
        return new RedisStateMachinePersister<>(redisPersist());
    }

}
