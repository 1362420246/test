package com.qbk.rocketmq.demo.examples;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 * Producer端发送同步消息
 * 这种可靠性同步地发送方式使用的比较广泛，比如：重要的消息通知，短信通知。
 */
public class SyncProducer {
    public static final String NAME_SERVER_ADDR = "192.168.1.6:9876";
    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {
        // 1. 创建生产者对象
        DefaultMQProducer producer = new DefaultMQProducer("GROUP_TEST");

        // 2. 设置NameServer的地址，如果设置了环境变量NAMESRV_ADDR，可以省略此步
        producer.setNamesrvAddr(NAME_SERVER_ADDR);

        //不是设置 会报 sendDefaultImpl call timeout
        producer.setSendMsgTimeout(5000);

        // 3. 启动生产者
        producer.start();

        // 4. 生产者发送消息
        for (int i = 0; i < 10; i++) {
            Message message = new Message("TopicTest", "TagA", ("Hello MQ:" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));

            SendResult result = producer.send(message);

            System.out.printf("发送结果：%s%n", result);
        }

        // 5. 停止生产者
        producer.shutdown();
    }
}