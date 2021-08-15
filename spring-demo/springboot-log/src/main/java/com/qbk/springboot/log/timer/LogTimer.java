package com.qbk.springboot.log.timer;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogTimer {

    private static final Logger applog = LoggerFactory.getLogger("app");
    private static final Logger applog2 = LoggerFactory.getLogger("app2");

    /**
     * TimeBasedRollingPolicy 设置每分钟分割日志，但是每两分钟输出一次，观察不输出时漏掉的日志文件
     */
    @Scheduled(cron = "0 */2 * * * ?")
    public void log(){
        String str = "a";
        System.out.println(str);
        log.debug(str);
        log.info(str);
        log.error(str);
        applog.info(1+str);
        applog2.info(2+str);
    }
}
