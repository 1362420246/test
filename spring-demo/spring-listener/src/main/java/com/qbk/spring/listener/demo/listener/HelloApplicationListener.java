package com.qbk.spring.listener.demo.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.RequestHandledEvent;

/**
 * ApplicationContext事件机制是观察者设计模式的实现，通过ApplicationEvent类和ApplicationListener接口，可以实现ApplicationContext事件处理。
 *
 * 如果容器中有一个ApplicationListener Bean，每当ApplicationContext发布ApplicationEvent时，ApplicationListener Bean将自动被触发。这种事件机制都必须需要程序显示的触发。
 *
 * 其中spring有一些内置的事件，当完成某种操作时会发出某些事件动作。比如监听ContextRefreshedEvent事件，当所有的bean都初始化完成并被成功装载后会触发该事件，实现ApplicationListener<ContextRefreshedEvent>接口可以收到监听动作，然后可以写自己的逻辑。
 *
 * 同样事件可以自定义、监听也可以自定义，完全根据自己的业务逻辑来处理。
 */
@Component
public class HelloApplicationListener {

    /**
     * ContextRefreshedEvent 事件
     *
     * ApplicationContext 被初始化或刷新时，该事件被发布。
     * 这也可以在 ConfigurableApplicationContext接口中使用 refresh() 方法来发生。
     * 此处的初始化是指：所有的Bean被成功装载，后处理Bean被检测并激活，所有Singleton Bean 被预实例化，ApplicationContext容器已就绪可用。
     */
    @Bean
    public ApplicationListener<ContextRefreshedEvent> contextRefreshedEvent(){
        return event->{
            ApplicationContext applicationContext = event.getApplicationContext();
            System.out.println("ApplicationListener: -----ContextRefreshedEvent");
        };
    }

    /**
     * ContextStartedEvent 事件
     *
     * 当使用 ConfigurableApplicationContext （ApplicationContext子接口）接口中的 start() 方法启动 ApplicationContext 时，
     * 该事件被发布。你可以调查你的数据库，或者你可以在接受到这个事件后重启任何停止的应用程序。
     */
    @Bean
    public ApplicationListener<ContextStartedEvent> contextStartedEvent(){
        return event->{
            ApplicationContext applicationContext = event.getApplicationContext();
            System.out.println("ApplicationListener: -----ContextStartedEvent");
        };
    }

    /**
     * ContextStoppedEvent 事件
     *
     * 当使用 ConfigurableApplicationContext 接口中的 stop() 停止 ApplicationContext 时，发布这个事件。
     * 你可以在接受到这个事件后做必要的清理的工作。
     */
    @Bean
    public ApplicationListener<ContextStoppedEvent> contextStoppedEvent(){
        return event->{
            ApplicationContext applicationContext = event.getApplicationContext();
            System.out.println("ApplicationListener: -----ContextStoppedEvent");
        };
    }

    /**
     * ContextClosedEvent 事件
     *
     * 当使用 ConfigurableApplicationContext 接口中的 close() 方法关闭 ApplicationContext 时，该事件被发布。
     * 一个已关闭的上下文到达生命周期末端；它不能被刷新或重启。
     */
    @Bean
    public ApplicationListener<ContextClosedEvent> contextClosedEvent(){
        return event->{
            ApplicationContext applicationContext = event.getApplicationContext();
            System.out.println("ApplicationListener: -----ContextClosedEvent");
        };
    }

    /**
     * RequestHandledEvent
     *
     * 这是一个 web-specific 事件，告诉所有 bean HTTP 请求已经被服务。只能应用于使用DispatcherServlet的Web应用。
     * 在使用Spring作为前端的MVC控制器时，当Spring处理用户请求结束后，系统会自动触发该事件。
     */
    @Bean
    public ApplicationListener<RequestHandledEvent> requestHandledEvent(){
        return event->{
            String userName = event.getUserName();
            System.out.println("ApplicationListener: -----RequestHandledEvent:" + userName);
        };
    }

}
