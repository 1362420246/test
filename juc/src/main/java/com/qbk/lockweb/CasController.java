package com.qbk.lockweb;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Cas \ Volatile 限流测试
 *
 *  需求是 当一个用户请求接口时候  其他用户必须等他请求完才能执行逻辑，要不只能立刻返回稍后再试
 */
@RequestMapping("/cas")
@RestController
public class CasController {

    private static volatile boolean isTrue = true ;

    private static volatile AtomicBoolean isAtimicTrue = new AtomicBoolean(true) ;

    /**
     * 高并发下有线程安全问题，因为下面两部操作无法保证原子性
     * 1 if(isTrue){
     * 2    isTrue = false;
     */
    @GetMapping("/unsafety")
    public String unsafety(String name) throws InterruptedException {
        if(isTrue){
            isTrue = false;
            TimeUnit.SECONDS.sleep(2);
            isTrue = true;
        }else {
            return "稍后再试";
        }
        return "成功";
    }

    /**
     * 使用cas锁 确保原子性
     */
    @GetMapping("/get")
    public String get() {
        //参考lock锁写法，把加锁写在try外面，解锁写在finally中
        if(isAtimicTrue.compareAndSet(true,false)){
            try {
                TimeUnit.SECONDS.sleep(2);
                //int i = 10/0;
                return "成功";
            }catch (Exception e){
                return "失败";
            }finally {
                isAtimicTrue.set(true);
            }
        }else {
            return "稍后再试";
        }
    }

}
