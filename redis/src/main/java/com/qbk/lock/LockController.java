package com.qbk.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * Redisson 分布式锁
 */
@RestController
public class LockController {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * Redisson 分布式锁
     */
    @GetMapping("/syn")
    public String syn() throws InterruptedException {
        final long start = System.currentTimeMillis();
        //获取锁
        RLock rLock = redissonClient.getLock("syn");
        // 尝试加锁，最多等待3秒，上锁以后100秒自动解锁
        if(rLock.tryLock(3, 100, TimeUnit.SECONDS)){
            try {
                TimeUnit.SECONDS.sleep(50);
                final long end = System.currentTimeMillis();
                return "同步成功:" + TimeUnit.SECONDS.convert(end-start , TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                e.printStackTrace();
                return "同步失败";
            }finally {
                //释放锁
                rLock.unlock();
            }
        }else {
            final long end = System.currentTimeMillis();
            return "同步中，稍后再试:"+ TimeUnit.SECONDS.convert(end-start , TimeUnit.MILLISECONDS);
        }
    }
}
