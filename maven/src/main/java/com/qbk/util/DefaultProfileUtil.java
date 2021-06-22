package com.qbk.util;

import org.springframework.boot.SpringApplication;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * 工具类来加载一个默认使用的Spring配置文件
 * spring.profiles.default 默认值，优先级低。当active没有配置时，使用此变量。
 * spring.profiles.active 优先级高，指定当前容器使用哪个profile
 */
public final class DefaultProfileUtil {

    private static final String SPRING_PROFILE_DEFAULT = "spring.profiles.default";
    private static final String SPRING_PROFILE_DEVELOPMENT = "prd";

    private DefaultProfileUtil(){
    }

    /**
     * *设置一个默认值，在没有配置配置文件时使用。
     * * @param应用程序的spring应用程序
     */
    public static void addDefaultProfile(SpringApplication app) {
        Map<String, Object> defProperties =  new HashMap<>();
        defProperties.put(SPRING_PROFILE_DEFAULT, SPRING_PROFILE_DEVELOPMENT);
        app.setDefaultProperties(defProperties);
    }

    /**
     * *获取应用的配置文件，否则获取默认配置文件。
     */
    public static String[] getActiveProfiles(Environment env) {
        String[] profiles = env.getActiveProfiles();
        if (profiles.length == 0) {
            return env.getDefaultProfiles();
        }
        return profiles;
    }
}