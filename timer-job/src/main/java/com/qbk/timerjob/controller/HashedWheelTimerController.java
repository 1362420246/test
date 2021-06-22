package com.qbk.timerjob.controller;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

/**
 * 时间轮
 */
@RequestMapping("/wheel")
@RestController
public class HashedWheelTimerController {

    /**
     * 时间轮
     */
    private static HashedWheelTimer wheelTimer = new HashedWheelTimer();

    /**
     * 延时执行
     */
    @GetMapping("/")
    public String delayed(String name ,int second){
        //任务
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                System.out.println(name +":" + LocalTime.now().toString());
            }
        };
        wheelTimer.newTimeout(timerTask, second ,TimeUnit.SECONDS );
        return LocalTime.now().toString();
    }
}
