package com.qbk.jvmtest.introspector;

import lombok.Data;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * java内省
 * Java内省机制，其实就是获取类的属性和属性类型
 */
public class IntrospectorTest {

    public static void main(String[] args) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        User user = new User();
        //操作单个属性
        PropertyDescriptor pd = new PropertyDescriptor("name", User.class);
        //获取属性的setter方法
        Method w = pd.getWriteMethod();
        w.invoke(user, "kk");

        //获取属性的getter方法
        Method r = pd.getReadMethod();
        Object invoke = r.invoke(user, null);
        System.out.println(invoke);

        System.out.println("----------------------------------");

        //1，获取JavaBean的描述对象
        BeanInfo bi = Introspector.getBeanInfo(User.class);
        //2，获取JavaBean中的属性的描述器
        PropertyDescriptor[] pds = bi.getPropertyDescriptors();
        //3，打印出来
        for (PropertyDescriptor p : pds) {
            System.out.println("属性名：" + p.getName());
            System.out.println("Getter：" + p.getReadMethod());
            System.out.println("Setter：" + p.getWriteMethod());
            System.out.println("----------------------------------");
            //属性是可以执行的，这应该就是反射了
            System.out.println(p.getReadMethod().invoke(user));
            System.out.println("----------------结束---------------");
        }
    }
}

@Data
class User {
    private String name;
    private int age;
    private Date birthday;
}
