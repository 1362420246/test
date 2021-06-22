package com.qbk.spring.demo.event.listener;
import com.qbk.spring.demo.event.event.AbstractEvent;
import com.qbk.spring.demo.event.event.IpEvent;
import com.qbk.spring.demo.event.event.LoginEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 *  ip监听
 */
@Component
public class IpListener implements ApplicationListener<AbstractEvent> {

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(AbstractEvent event) {
        if(event instanceof IpEvent){
            IpEvent ipEvent = (IpEvent)event;
            System.out.println("ip监听收到: " + ipEvent.getSource());
        }else {
            System.out.println("ip监听收到: " + "其他事件");
        }
    }
}
