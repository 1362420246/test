package com.qbk.web.servletasync.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 方式一 ： Servlet方式实现异步请求
 */
@RestController
public class AsyncController {

    /**
     * 返回必须是 void
     */
    @GetMapping("/async/servlet")
    public void get(HttpServletRequest request){
        //通过request获取异步上下文AsyncContext
        AsyncContext asyncContext = request.startAsync();
        //设置异步监听器:可设置其开始、完成、异常、超时等事件的回调处理
        asyncContext.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent asyncEvent) throws IOException {
                //可以在这做 资源清理
                System.out.println(LocalDateTime.now().toString() + "执行完成" );
            }

            @Override
            public void onTimeout(AsyncEvent asyncEvent) throws IOException {
                System.out.println(LocalDateTime.now().toString() + "超时了...");
            }

            @Override
            public void onError(AsyncEvent asyncEvent) throws IOException {
                //向客户端返回错误响应
                System.out.println(LocalDateTime.now().toString() + "发生错误："+asyncEvent.getThrowable());
            }

            @Override
            public void onStartAsync(AsyncEvent asyncEvent) throws IOException {
                System.out.println(LocalDateTime.now().toString() + "线程开始");
            }
        });
        //设置超时时间
        asyncContext.setTimeout(20000);
        //异步线程执行耗时任务
        asyncContext.start(() -> {
            try {
                Thread.sleep(5000);
                System.out.println(LocalDateTime.now().toString() + " ,内部线程：" + Thread.currentThread().getName());
                //响应
                asyncContext.getResponse().setCharacterEncoding("utf-8");
                asyncContext.getResponse().setContentType("text/html;charset=UTF-8");
                asyncContext.getResponse().getWriter().println(LocalDateTime.now().toString() + "这是异步的请求返回");
            } catch (Exception e) {
                System.out.println("异常："+e);
            }
            //异步请求完成通知
            //此时整个请求才完成
            asyncContext.complete();
        });
        //此时 request的 容器 线程连接 已经释放了
        System.out.println(LocalDateTime.now().toString() + " ,主线程：" + Thread.currentThread().getName());
    }

}
