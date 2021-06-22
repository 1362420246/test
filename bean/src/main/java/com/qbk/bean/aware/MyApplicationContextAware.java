package com.qbk.bean.aware;

import com.qbk.bean.service.MyService;
import com.qbk.bean.service.impl.MyServiceImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 测试2种aop和无aop情况下getBean获取对象情况
 */
@Component
public class MyApplicationContextAware implements ApplicationContextAware {

    private static ApplicationContext context ;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext ;
        System.out.println("获取applicationContext成功");
    }

    public static ApplicationContext getApplicationContext(){
        return context;
    }

    @Autowired
    private MyService myService;

    @PostConstruct
    public void init(){

        //结合aop查看 代理对象类型
        //MyServiceImpl$$EnhancerBySpringCGLIB$$d38e77a7
        //$Proxy60
        System.out.println(myService.getClass().getSimpleName());

        /*
        getBean 实际调用的是 DefaultSingletonBeanRegistry 中的三个缓存
        private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
        private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);
        private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);
         */

        /*查看 无aop、aop-cjlb、aop-jdk 三种不同模式下的 对象类型，其中，aop-jdk 抛出转换异常
          如果是使用Jdk动态代理实现Spring AOP,Spring容器的getBean方法获得的对象是不能转型成该Bean定义的Class类型
         */
//        MyServiceImpl myService = (MyServiceImpl) context.getBean("myServiceImpl");
        MyService myService = (MyService) context.getBean("myServiceImpl");

        //结合aop查看 代理对象类型
        System.out.println(myService.getClass().getSimpleName());

        myService.fun();
    }
}