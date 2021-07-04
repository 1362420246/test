package com.qbk.kafka.demo.partitioner;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义分区器
 *
 *  kafka中每个topic被划分为多个分区，那么生产者将消息发送到topic时，具体追加到哪个分区呢？
 *  这就是所谓的分区策略，Kafka 为我们提供了默认的分区策略，同时它也支持自定义分区策略。其路由机制为：
 *
 *  ① 若发送消息时指定了分区（即自定义分区策略），则直接将消息append到指定分区；
 *
 *  ② 若发送消息时未指定 patition，但指定了 key（kafka允许为每条消息设置一个key），
 *  则对key值进行hash计算，根据计算结果路由到指定分区，这种情况下可以保证同一个 Key 的所有消息都进入到相同的分区；
 *
 *  ③  patition 和 key 都未指定，则使用kafka默认的分区策略，轮询选出一个 patition；
 */
public class CustomizePartitioner implements Partitioner {
    private final AtomicInteger counter = new AtomicInteger(new Random().nextInt());
    private Random random = new Random();

    /**
     * # 自定义分区器
     * spring.kafka.producer.properties.partitioner.class=CustomizePartitioner
     */
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitioners = cluster.partitionsForTopic(topic);
        int numPartitions = partitioners.size();

        /**
         * 由于我们按key分区，在这里我们规定：key值不允许为null。
         * 在实际项目中，key为null的消息*，可以发送到同一个分区,或者随机分区。
         */
        int res;
        if (keyBytes == null) {
            System.out.println("value is null");
            res = random.nextInt(numPartitions);
        } else {
//            System.out.println("value is " + value + "\n hashcode is " + value.hashCode());
            res = Math.abs(key.hashCode()) % numPartitions;
        }
        System.out.println("data partitions is " + res);
        return res;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
