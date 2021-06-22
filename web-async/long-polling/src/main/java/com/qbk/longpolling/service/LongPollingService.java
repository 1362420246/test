package com.qbk.longpolling.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.*;

/**
 * 长轮询
 */
@Slf4j
@Service
public class LongPollingService {

    /**
     * 长轮询线程池
     */
    private static final ScheduledExecutorService LONG_POLLING_EXECUTOR = Executors.newSingleThreadScheduledExecutor();

    /**
     * 客户端长轮询 队列
     */
    Queue<ClientLongPolling> allSubs = new ConcurrentLinkedQueue<>();;

    /**
     * 数据变更 事件
     */
    @Getter
    public class LocalDataChangeEvent extends ApplicationEvent {
        public LocalDataChangeEvent(Object source) {
            super(source);
        }
    }

    /**
     * 监听 LocalDataChangeEvent 事件
     */
    @EventListener
    public void handleAbstractEvent(LocalDataChangeEvent event){
        //执行 数据变更任务
        LONG_POLLING_EXECUTOR.execute(new DataChangeTask());
    }

    /**
     * 客户端长轮询 任务
     */
    class ClientLongPolling implements Runnable {
        /**
         * 异步超时任务 结果
         */
        Future<?> asyncTimeoutFuture;

        AsyncContext asyncContext;

        ClientLongPolling(AsyncContext asyncContext) {
            this.asyncContext = asyncContext;
        }

        @Override
        public void run() {
            // 执行异步超时 定时任务  定时30s
            asyncTimeoutFuture = LONG_POLLING_EXECUTOR.schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 从 客户端长轮询队列 中 删除 此次长轮询任务
                        allSubs.remove(ClientLongPolling.this);

                        //省略比对配置逻辑

                        // 响应
                        sendResponse("配置无更新");

                    } catch (Throwable t) {
                        log.error("long polling error:" + t.getMessage(), t.getCause());
                    }
                }
            }, 30, TimeUnit.SECONDS);

            //添加 客户端长轮询 队列
            allSubs.add(this);
        }

        /**
         * 响应
         */
        void sendResponse(String respString) {
            // 取消超时任务
            if (null != asyncTimeoutFuture) {
                asyncTimeoutFuture.cancel(false);
            }
            HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
            try {
                // Disable cache.
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);
                response.setHeader("Cache-Control", "no-cache,no-store");
                response.setCharacterEncoding("utf-8");
                response.setContentType("text/html;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(respString);
                asyncContext.complete();
            } catch (Exception ex) {
                log.error(ex.toString(), ex);
                asyncContext.complete();
            }
        }
    }

    /**
     * 数据变更任务
     */
    class DataChangeTask implements Runnable {
        @Override
        public void run() {
            try {
                //从  客户端长轮询 队列 中 获取长训轮询 任务
                for (Iterator<ClientLongPolling> iter = allSubs.iterator(); iter.hasNext(); ) {
                    ClientLongPolling clientSub = iter.next();

                    //省略 比对配置逻辑 和 队列里任务匹配逻辑

                    // 从 客户端长轮询队列 中 删除 此次长轮询任务
                     iter.remove();

                     //响应
                     clientSub.sendResponse("配置有更新");
                }
            } catch (Throwable t) {
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                final PrintStream ps = new PrintStream(out);
                t.printStackTrace(ps);
                ps.flush();
                log.error("data change error: {}",new String(out.toByteArray()));
            }
        }
    }

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 添加长轮询客户端
     */
    public void addLongPollingClient(HttpServletRequest request, HttpServletResponse response) {

        //必须通过http线程调用，或发送响应。
        final AsyncContext asyncContext = request.startAsync();

        // settimeout()是不正确的，由自己控制
        asyncContext.setTimeout(0L);

        //执行 客户端长轮询 任务
        LONG_POLLING_EXECUTOR.execute(new ClientLongPolling(asyncContext));
    }

    /**
     * 变更数据
     */
    public void dataChange() {
        //发布 数据变更 事件
        applicationContext.publishEvent(new LocalDataChangeEvent(""));
    }
}
