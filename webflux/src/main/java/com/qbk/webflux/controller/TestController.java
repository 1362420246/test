package com.qbk.webflux.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 *  对比 异步接口 和 传统接口
 */
@RestController
public class TestController {

    /**
     * 传统
     */
    @GetMapping("/1")
    public String get1(){
        long startTime = System.currentTimeMillis();
        String str = createStr();
        long endTime = System.currentTimeMillis();
        System.out.println("传统耗时:" + (endTime - startTime));
        return str;
    }

    /**
     * @return Mono 返回 0 - 1 个元素
     */
    @GetMapping("/2")
    public Mono<String> get2(){
        long startTime = System.currentTimeMillis();
        Mono<String> result = Mono.fromSupplier(this::createStr);
        long endTime = System.currentTimeMillis();
        System.out.println("异步耗时:" + (endTime - startTime));
        return result;
    }

    /**
     * @return Flux 返回 0 - n个元素
     */
    @GetMapping(value = "/3",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> get3(){
        long startTime = System.currentTimeMillis();
        Flux<String> result =
                Flux.fromStream(
                        IntStream.range(1,5).mapToObj(
                                i -> createStr() + i
                        )
                );
        long endTime = System.currentTimeMillis();
        System.out.println("异步耗时:" + (endTime - startTime));
        return result;
    }

    /**d
     * 主线程耗时操作
     */
    private String createStr(){
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "qbk";
    }
}
