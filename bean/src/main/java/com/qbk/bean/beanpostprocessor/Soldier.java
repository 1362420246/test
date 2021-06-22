package com.qbk.bean.beanpostprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.*;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 测试Aware、生命周期接口 执行顺序
 */
@Component
public class Soldier implements
        ApplicationContextAware
        , DisposableBean
        , BeanNameAware
        , BeanFactoryAware
        , MessageSourceAware
        , ApplicationEventPublisherAware
        , ResourceLoaderAware
        , InitializingBean {

    public Soldier() {
        System.out.println("Soldier 构造方法...");
    }

    @PostConstruct
    public void postPostConstruct() {
        System.out.println("@PostConstruct");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("@PreDestroy...");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("ApplicationContextAware.setApplicationContext.......");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean.afterPropertiesSet...");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("DisposableBean.destroy...");
    }

    @Override
    public void setBeanName(String s) {
        System.out.println("BeanNameAware.setBeanName:" + s + "...");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("BeanFactoryAware.setBeanFactory...");
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        System.out.println("ApplicationEventPublisherAware.setApplicationEventPublisher...");
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        System.out.println("MessageSourceAware.setMessageSource...");
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        System.out.println("ResourceLoaderAware.setResourceLoader...");
    }
}