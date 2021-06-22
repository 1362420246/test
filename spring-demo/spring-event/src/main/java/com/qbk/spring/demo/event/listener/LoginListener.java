package com.qbk.spring.demo.event.listener;
import com.qbk.spring.demo.event.event.AbstractEvent;
import com.qbk.spring.demo.event.event.LoginEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 *  登陆监听
 */
@Component
public class LoginListener {

    @EventListener
    public void handleAbstractEvent(AbstractEvent event){
        if(event instanceof LoginEvent){
            LoginEvent loginEvent = (LoginEvent)event;
            System.out.println("登陆监听收到: " +loginEvent.getSource());
        }else {
            System.out.println("登陆监听收到: " + "其他事件");
        }
    }

    @Async
    @EventListener
    public void handleLoginEvent(LoginEvent event){
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("只监听登陆事件:" + event.getSource() + "， 时间：" + LocalDateTime.now().toString() );
    }
}
