package com.qbk.zk.handwritten.config.initializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qbk.zk.handwritten.config.refresh.FieldDetail;
import com.qbk.zk.handwritten.config.refresh.FieldDetailSingle;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.lang.reflect.Field;
import java.util.Map;

/**
 *  SPI的方式：按照约定优于配置的原则，将这个初始化器放到指定spring.factories文件中
 */
public class ZkConfigApplicationContextInitializer implements ApplicationContextInitializer {

    private final String zkServer = "101.43.76.164:2181";
    private final String scheme = "digest";
    private final byte[] auth = "qbk:123456".getBytes();
    private final String path = "/qbk-config/handwritten-demo";

    /**
     * propertySource 的名称
     */
    private final String propertySourceName = "demo-service-remote-env";

    /**
     * spring容器refresh刷新之前执行的一个回调函数
     */
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        //链接zk
        CuratorFramework curatorFramework = CuratorFrameworkFactory
                .builder()
                .connectionTimeoutMs(20000)
                .connectString(zkServer)
                .authorization(scheme, auth)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        curatorFramework.start();
        try {
            //从zk中读取配置
            byte[] bytes = curatorFramework.getData().forPath(path);
            // Json转成Map
            Map<String,Object> map = new ObjectMapper().readValue(new String(bytes), Map.class);
            System.out.println("从zookeeper server获取到的值为: " + map);
            // 将map转换成MapPropertySource
            MapPropertySource mapPropertySource = new MapPropertySource(propertySourceName, map);
            //把propertySource放到Environment中的第一位
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            environment.getPropertySources().addFirst(mapPropertySource);
            System.out.println("env新增MapPropertySource成功.");

            // 永久的监听
            CuratorCache curatorCache = CuratorCache.build(curatorFramework, path, CuratorCache.Options.SINGLE_NODE_CACHE);
            CuratorCacheListener listener = CuratorCacheListener.builder().forAll(new CuratorCacheListener() {
                //ZNode发生变化，就会回调这个方法
                @Override
                public void event(Type type, ChildData oldData, ChildData data) {
                    //事件类型
                    if (type.equals(Type.NODE_CHANGED)) {
                        System.out.println("ZNode数据更新了, 事件类型为: " + type);
                        try {
                            //获取变化数据
                            Map<String, Object> updateMap = new ObjectMapper().readValue(new String(data.getData()), Map.class);
                            System.out.println("更新后的数据map为: "+updateMap);

                            //1、刷新 environment
                            //更改Environment中PropertySource
                            environment.getPropertySources().replace(propertySourceName, new MapPropertySource(propertySourceName, updateMap));

                            //2、刷新 @Value
                            //拿到要刷新的字段集合
                            Map<String, FieldDetail> fieldDetailMap = FieldDetailSingle.VALUE_MAP;
                            for (String key : fieldDetailMap.keySet()) {
                                // 判断远端发送过来的map数据中的key
                                if(updateMap.containsKey(key)){
                                    final FieldDetail fieldDetail = fieldDetailMap.get(key);
                                    //对象
                                    final Object instance = fieldDetail.getInstance();
                                    //字段
                                    final Field field = fieldDetail.getField();
                                    //值
                                    Object value = environment.getProperty(key,field.getType());
                                    // 反射更新字段的值
                                    // 相当于AutowiredAnnotationBeanPostProcessor#AutowiredFieldElement#inject
                                    field.setAccessible(true);
                                    field.set(instance,value);
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }).build();
            curatorCache.listenable().addListener(listener);
            curatorCache.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
