package com.qbk.postprocessor.context;

import com.qbk.postprocessor.annotation.TestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * 容器
 */
@Component
public class ApplicationContextTest {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 找到所有具有提供的{@link Annotation}类型的{@code Class}的bean，返回具有相应bean实例的bean名称映射。
     *  Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws BeansException;
     *
     *  在指定的bean上找到{@code annotationType}的{@link Annotation}，如果在给定的类本身上找不到注释，则遍历它的接口和超类。
     *  <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) throws NoSuchBeanDefinitionException;
     */

    @PostConstruct
    public void test(){
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(TestBean.class);

        for (Map.Entry<String, Object> entry : beans.entrySet()) {

            TestBean testAnnotation = applicationContext.findAnnotationOnBean(entry.getKey(), TestBean.class);
            String name = testAnnotation.name();

            System.out.println(name);
        }

    }

}
