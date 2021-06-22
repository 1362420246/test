package com.qbk.nosql.hbase.demo.config;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.util.Threads;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * HBase配置
 */
@Configuration
public class HbaseAutoConfiguration {

    @Autowired
    private HbaseProperties hbaseProperties;

    /**
     *  配置
     */
    @Bean
    public org.apache.hadoop.conf.Configuration configuration() {
        org.apache.hadoop.conf.Configuration configuration = HBaseConfiguration.create();
        Map<String, String> config = hbaseProperties.getConfig();
        Set<String> keySet = config.keySet();
        for (String key : keySet) {
            configuration.set(key, config.get(key));
        }
        // 更改大小限制 , 默认10MB , 解决 KeyValue size too large
        configuration.set("hbase.client.keyvalue.maxsize","20971520");

        //失败重试间隔计算方式:
        //public static int RETRY_BACKOFF[] = { 1, 2, 3, 5, 10, 20, 40, 100, 100, 100, 100, 200, 200 };
        // long normalPause = pause * HConstants.RETRY_BACKOFF[ntries];
        // long jitter = (long)(normalPause * RANDOM.nextFloat() * 0.01f);

        //失败重试时等待时间 默认值为100ms
        configuration.set("hbase.client.pause","50");

        //失败时重试次数,默认为31次
        configuration.set("hbase.client.retries.number","5");
        return configuration;
    }

    /**
     *  连接
     * @param configuration 配置
     */
    @Bean
    public Connection connection (org.apache.hadoop.conf.Configuration configuration)throws IOException {

        int threads = 256 ;
        long keepAliveTime = 60 ;

        BlockingQueue<Runnable> workQueue =
                new LinkedBlockingQueue<>(threads * 100 );

        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
                threads,
                threads,
                keepAliveTime,
                TimeUnit.SECONDS,
                workQueue,
                Threads.newDaemonThreadFactory(toString() + "-shared"));

        // init pool
        poolExecutor.prestartCoreThread();

        return ConnectionFactory.createConnection(configuration,poolExecutor);
    }
}
