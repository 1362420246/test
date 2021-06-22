package com.qbk.lockweb;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Semaphore 限流
 */
@RestController
public class RateLimiterController {

    private Semaphore semaphore = new Semaphore(2);

    @GetMapping("/limiter")
    public String limiter() throws Exception {
        //仅在调用时可用时才获得许可。
        if (semaphore.tryAcquire()) {
            TimeUnit.SECONDS.sleep(30);
            //释放
            semaphore.release();
            return "访问正常";
        }
        return "你被限流了";
    }

}
