package com.qbk.bean.beanpostprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;

/**
 * 测试后置处理器 执行顺序
 */
@Component
public class FullyBeanPostProcessor implements
        BeanPostProcessor,
        BeanFactoryPostProcessor,
        InstantiationAwareBeanPostProcessor,
        SmartInstantiationAwareBeanPostProcessor,
        MergedBeanDefinitionPostProcessor,
        DestructionAwareBeanPostProcessor {

    /**
     * BeanFactoryPostProcessor
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException{
        System.out.println("0-->BeanFactoryPostProcessor");
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName)
            throws BeansException {
        if (beanClass == Soldier.class) {
            System.out.println(
                    "1 --> InstantiationAwareBeanPostProcessor.postProcessBeforeInstantiation(beanClass, beanName)");
        }
        return null;
    }

    @Override
    public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName)
            throws BeansException {
        if (beanClass == Soldier.class) {
            System.out.println(
                    "2 --> SmartInstantiationAwareBeanPostProcessor.determineCandidateConstructors(beanClass, beanName)");
        }
        return null;
    }

    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType,
                                                String beanName) {
        if (beanType == Soldier.class) {
            System.out.println(
                    "3 --> MergedBeanDefinitionPostProcessor.postProcessMergedBeanDefinition(mbd, beanType, beanName)");
        }

    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if (bean instanceof Soldier) {
            System.out.println(
                    "4 --> InstantiationAwareBeanPostProcessor.postProcessAfterInstantiation(bw.getWrappedInstance(), beanName)");
        }
        return true;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName)
            throws BeansException {
        if (bean instanceof Soldier) {
            System.out.println(
                    "5 --> InstantiationAwareBeanPostProcessor.postProcessProperties(pvs, filteredPds, bw.getWrappedInstance(), beanName)");
        }
        return null;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        if (bean instanceof Soldier) {
            System.out
                    .println("6 --> BeanPostProcessor.postProcessBeforeInitialization(result, beanName)");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Soldier) {
            System.out
                    .println("7 --> BeanPostProcessor.postProcessAfterInitialization(result, beanName)");
        }
        return bean;
    }

    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        if (bean instanceof Soldier) {
            System.out.println(
                    "8 --> DestructionAwareBeanPostProcessor.postProcessBeforeDestruction(Object bean, String beanName)");
        }
    }

    @Override
    public Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }


    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
