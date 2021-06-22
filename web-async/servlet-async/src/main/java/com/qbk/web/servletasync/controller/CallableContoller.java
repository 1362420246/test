package com.qbk.web.servletasync.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

/**
 * 方式二 ： 直接返回的参数包裹一层callable
 */
@RestController
public class CallableContoller {

    @GetMapping("/async/callable")
    public Callable<String> callable () {
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(5000);
                System.out.println(LocalDateTime.now().toString() + " ,内部线程：" + Thread.currentThread().getName());
                return LocalDateTime.now().toString() + "这是异步的请求返回";
            }
        };
        //此时 容器 线程连接 已经释放了
        System.out.println(LocalDateTime.now().toString() + " ,主线程：" + Thread.currentThread().getName());
        return callable;
    }


}
