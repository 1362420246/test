package com.qbk.rocketmq.demo.transaction;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;

import java.nio.charset.StandardCharsets;

/**
 * 事务消息生产者端的消息监听器
 *
 * 事务消息共有三种状态，提交状态、回滚状态、中间状态：
 *  TransactionStatus.CommitTransaction: 提交事务，它允许消费者消费此消息。
 *  TransactionStatus.RollbackTransaction: 回滚事务，它代表该消息将被删除，不允许被消费。
 *  TransactionStatus.Unknown: 中间状态，它代表需要检查消息队列来确定状态。
 *
 */
@Slf4j
@RocketMQTransactionListener()
public class TXMsgListener implements RocketMQLocalTransactionListener {

    /**
     * 执行本地事务
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        log.info(">>>> TX message listener execute local transaction, message={},args={} <<<<",msg,arg);
        // 执行本地事务
        RocketMQLocalTransactionState result = RocketMQLocalTransactionState.COMMIT;
        try {
            String message = new String((byte[]) msg.getPayload(), StandardCharsets.UTF_8);
            System.out.println("执行本地事务:" + message);
            //int a = 10 /0;
        } catch (Exception e) {
            log.error(">>>> exception message={} <<<<",e.getMessage());
            result = RocketMQLocalTransactionState.UNKNOWN;
        }
        return result;
    }

    /**
     * 检查本地事务
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        log.info(">>>> TX message listener check local transaction, message={} <<<<",msg.getPayload());
        // 检查本地事务
        RocketMQLocalTransactionState result = RocketMQLocalTransactionState.COMMIT;
        try {
            String message = new String((byte[]) msg.getPayload(), StandardCharsets.UTF_8);
            System.out.println("检查本地事务:" + message);
            //int b = 10 /0;
        } catch (Exception e) {
            // 异常就回滚
            log.error(">>>> exception message={} <<<<",e.getMessage());
            result = RocketMQLocalTransactionState.ROLLBACK;
        }
        return result;
    }
}