package com.qbk.bloomfilter;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 布隆过滤器
 **/
@Slf4j
@Component
public class BloomFilterDataLoadApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //创建布隆过滤器
        BloomFilter<String> bloomFilter =
                BloomFilter.create(
                        Funnels.stringFunnel(Charsets.UTF_8),
                        100000000, //元素个数
                        0.03  //误判率
                );

        List<Integer> data = Arrays.asList(1,2,3);
        //添加
        data.forEach(id-> bloomFilter.put("user:"+ id));
        TestContoller.bloomFilterCache = bloomFilter;
    }
}
