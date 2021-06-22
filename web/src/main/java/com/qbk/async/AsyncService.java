package com.qbk.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

@Slf4j
@Service
@EnableScheduling
public class AsyncService {

    private DeferredResult<String> deferredResult;
    private String orderNumber;

    public DeferredResult<String> getAsyncUpdate(String orderNumber) {
        this.orderNumber = orderNumber ;
        deferredResult = new DeferredResult<>();
        return deferredResult;
    }

    /**
     * 定时器5秒一次
     */
    @Scheduled(fixedDelay = 5000)
    public void refresh(){
        if(deferredResult != null){
            //定时器线程执行 异步任务
            log.info("定时器线程执行订单号："+orderNumber);
            //设置异步结果 （触发接口返回）
            deferredResult.setResult(Long.toString(System.currentTimeMillis()));
            deferredResult = null;
        }
    }
}
