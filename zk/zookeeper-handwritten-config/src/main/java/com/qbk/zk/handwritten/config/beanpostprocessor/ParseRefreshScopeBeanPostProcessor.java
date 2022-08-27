package com.qbk.zk.handwritten.config.beanpostprocessor;

import com.qbk.zk.handwritten.config.refresh.FieldDetail;
import com.qbk.zk.handwritten.config.refresh.FieldDetailSingle;
import com.qbk.zk.handwritten.config.refresh.QBKRefreshScope;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * 解析@Value注解的值，并存储。后置处理器
 */
@Component
public class ParseRefreshScopeBeanPostProcessor implements BeanPostProcessor {

    /**
     * 在bean初始化之后执行
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        //类型 是否包含自定义刷新注解 QBKRefreshScope
        if (clazz.isAnnotationPresent(QBKRefreshScope.class)) {
            //获得类的所有声明的字段
            for (Field field : clazz.getDeclaredFields()) {
                //字段 是否 Value注解
                if (field.isAnnotationPresent(Value.class)) {
                    Value value = field.getAnnotation(Value.class);
                    // 获取到了对应的value值 ${id}
                    String val = value.value();
                    // 获取到了对应的value值 id
                    val = val.substring(2, val.indexOf("}"));
                    //存储 value值、字段、对象
                    FieldDetailSingle.VALUE_MAP.put(val, new FieldDetail(field, bean));
                }
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
