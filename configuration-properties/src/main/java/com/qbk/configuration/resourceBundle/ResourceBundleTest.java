package com.qbk.configuration.resourceBundle;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 国际化测试
 */
public class ResourceBundleTest {

    public static void main(String[] args) {

        //中国地区的中文
        Locale zhLoc = new Locale("zh", "CN");

        //美国地区的英文
        Locale enLoc = new Locale("en", "US");

        //日本地区的日文
        Locale jaLoc = new Locale("ja", "JP");

        //找到中文的属性文件
        ResourceBundle zhrb = ResourceBundle.getBundle("Message", zhLoc);

        //找到英文的属性文件
        ResourceBundle enrb = ResourceBundle.getBundle("Message", enLoc);

        //找到日文的属性文件
        ResourceBundle jarb = ResourceBundle.getBundle("Message", jaLoc);

        //通过键值的方式读取属性文件内容

        System.out.println("中文："

                + zhrb.getString("info"));

        System.out.println("英文："

                + enrb.getString("info"));

        System.out.println("日文："

                + jarb.getString("info"));

    }
}
