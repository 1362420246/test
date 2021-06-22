package com.qbk.web.servletasync.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

/**
 * 方式三：和方式二差不多，在Callable外包一层，给WebAsyncTask设置一个超时回调，即可实现超时处理
 */
@RestController
public class WebAsyncTaskController {

        @GetMapping("/async/webAsyncTask")
        public WebAsyncTask<String> webAsyncTask () {
            Callable<String> callable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    Thread.sleep(5000);
                    System.out.println(LocalDateTime.now().toString() + " ,内部线程：" + Thread.currentThread().getName());
                    return LocalDateTime.now().toString() + "这是异步的请求返回";
                }
            };
            WebAsyncTask<String> wat = new WebAsyncTask<>(6000L, callable);
            wat.onTimeout(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return "超时";
                }
            });
            //此时 容器 线程连接 已经释放了
            System.out.println(LocalDateTime.now().toString() + " ,主线程：" + Thread.currentThread().getName());
            return wat;
        }
}
