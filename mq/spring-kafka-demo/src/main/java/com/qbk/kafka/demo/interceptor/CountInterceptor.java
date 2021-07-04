package com.qbk.kafka.demo.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * Producer拦截器
 *
 * CountInterceptor 计数
 */
public class CountInterceptor implements ProducerInterceptor<String, String> {

    private int successCount = 0;
    private int errorCount = 0;

    /**
     * 该方法封装进KafkaProducer.send方法中，即它运行在用户主线程中。Producer确保在消息被序列化
     * 以及计算分区前调用该方法。用户可以在该方法中对消息做任何操作，但最好保证不要修改消息所属的
     *  topic和分区，否则会影响目标分区的计算
     */
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        return record;
    }

    /**
     * 该方法会在消息被应答或消息发送失败时调用，并且通常都是在producer回调逻辑触发之前。
     *  onAcknowledgement运行在producer的IO线程中，因此不要在该方法中放入很重的逻辑，否则会拖慢
     *  producer的消息发送效率
     */
    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if (exception == null) {
            successCount++;
            System.out.println("当前成功计数:"+successCount);
        } else {
            errorCount++;
            System.out.println("当前失败计数:"+errorCount);

        }
    }

    /**
     * 关闭interceptor，主要用于执行一些资源清理工作
     */
    @Override
    public void close() {
        // 关闭的时候结算 Springboot 项目一般不会关闭~
        System.out.println("发送成功" + successCount);
        System.out.println("发送失败" + errorCount);
    }

    /**
     * 获取配置信息和初始化数据时调用
     */
    @Override
    public void configure(Map<String, ?> configs) {

    }
}

