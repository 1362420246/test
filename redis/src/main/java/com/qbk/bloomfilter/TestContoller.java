package com.qbk.bloomfilter;

import com.google.common.hash.BloomFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试布隆过滤器
 */
@RestController
public class TestContoller {

    public static BloomFilter<String> bloomFilterCache ;

    @GetMapping("/bloom_filter")
    public String bloomFilter(Integer id){
        //判断 可能存在
        if(bloomFilterCache.mightContain("user:" + id)){
            // 执行redis操作
            return "可能存在";
        }else {
            return "一定不存在";
        }
    }
}
