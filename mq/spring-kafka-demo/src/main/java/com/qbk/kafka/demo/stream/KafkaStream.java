package com.qbk.kafka.demo.stream;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

/**
 * Kafka Streams
 *
 * Kafka Streams是一个客户端库，用于构建任务关键型实时应用程序和微服务，其中输入和/或输出数据存储在Kafka集群中。
 *
 * Kafka Streams结合了在客户端编写和部署标准Java和Scala应用程序的简单性以及Kafka服务器端集群技术的优势，
 * 使这些应用程序具有高度可扩展性，弹性，容错性，分布式等等。
 *
 */
@Configuration
@EnableKafkaStreams
public class KafkaStream {

    @Bean
    public KStream<String, String> kStream(StreamsBuilder streamsBuilder) {
        KStream<String, String> stream = streamsBuilder.stream("Stream");
        stream.map(
                (key, value) -> {
                    value += "--test--";
                    return new KeyValue<>(key, value);
                }
        ).to("AnotherTopic", Produced.with(Serdes.String(),Serdes.String()));
        return stream;
    }

}
