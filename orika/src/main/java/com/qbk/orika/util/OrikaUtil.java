package com.qbk.orika.util;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ConcurrentReferenceHashMap;
import com.alibaba.fastjson.JSON;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

/**
 *  Orika
 */
public class OrikaUtil {

    private final static Logger logger = LoggerFactory.getLogger(OrikaUtil.class);

    /**
     * 全局缓存，提高效率并防止orika造成metaspace内存溢出
     */
    private static Map<String, SoftReference<MapperFacade>> cacheMap =
            new ConcurrentReferenceHashMap<>();

    /**
     * 单个对象互转(注意判空)
     */
    public static <T> T tran(Object source, Class<T> target, Map<String, String> map) {
        if (source == null || map == null) {
            return null;
        }

        try {
            // 获取当前转换的签名，作为缓存的key
            String sign =
                    source.getClass().getSimpleName() + "->" + target.getSimpleName() + ":" + JSON.toJSONString(map);

            // 先尝试从缓存中读取
            MapperFacade mapper = null;
            SoftReference<MapperFacade> mapperReference = cacheMap.get(sign);
            if (mapperReference != null) {
                mapper = mapperReference.get();
            }

            if (mapper == null) {
                //构造一个MapperFactory
                MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
                ClassMapBuilder<?, T> cmb = mapperFactory.classMap(source.getClass(), target);
                // 处理字段映射
                map.forEach(cmb::field);
                cmb.byDefault().register();
                mapper = mapperFactory.getMapperFacade();

                // 放入缓存
                cacheMap.put(sign, new SoftReference<>(mapper));
            }

            return mapper.map(source, target);
        } catch (Exception e) {
            logger.error("对象互转发生异常：", e);
        }

        return null;
    }

    /**
     * 对象列表互转
     */
    public static <T> List<T> tranList(List<?> sourceList, Class<T> target, Map<String, String> map) {
        List<T> ret = new ArrayList<>();
        sourceList.forEach(o -> ret.add(tran(o, target, map)));
        return ret;
    }

    /**
     * 根据key、value字符串生成map，格式：key->value
     */
    public static Map<String, String> getMap(String... keyAndValues) {
        Map<String, String> map = new HashMap<>();
        try {
            for (String keyAndValue : keyAndValues) {
                if (keyAndValue.contains("->")) {
                    map.put(keyAndValue.split("->")[0], keyAndValue.split("->")[1]);
                }
            }
        } catch (Exception e) {
            logger.error("获取转换Map发生异常：", e);
        }
        return map;
    }

}
