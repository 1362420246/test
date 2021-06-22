package com.qbk.postprocessor.beanfactory;

import com.qbk.postprocessor.annotation.TestBean;
import com.qbk.postprocessor.handler.Handler;
import com.qbk.postprocessor.handler.ParentHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * BeanFactoryPostProcessor
 *
 * 是针对整个工厂生产出来的BeanDefinition作出修改或者注册。作用于BeanDefinition时期。从名称可以看出是容器级别的
 */
@Component
public class TestBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    /**
     * 修改应用程序上下文的内部bean factory 初始化。
     * 所有bean定义都将被加载，但没有加载bean 还没有被实例化。
     * 这允许重写或添加属性，
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //根据注释查找 spring容器 返回 注册的bean
        Map<String, Object> beans = beanFactory.getBeansWithAnnotation(TestBean.class);
        for (String key : beans.keySet()) {
            ParentHandler parentHandler = (ParentHandler) beans.get(key);

            //自定义对象
            Handler handler = new Handler();
            //把 springbean 注入到自定义对象的属性中
            handler.setParentHandler(parentHandler);

            //把自定义对象注册到 spring容器中
            beanFactory.registerSingleton(handler.getName(), handler);
        }
    }


}
