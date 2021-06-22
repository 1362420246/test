package com.qbk.nosql.hbase.demo;

import com.google.common.collect.Maps;
import com.qbk.nosql.hbase.demo.utils.HBaseUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * HBaseUtil 测试
 */
@SpringBootTest(classes = HbaseDemoApplication.class)
public class HBaseUtilTest {

    @Autowired
    private Connection connection;

    /**
     * 是否存在
     */
    @Test
    public void testExists() throws IOException {
        Admin admin = connection.getAdmin();
        boolean b = admin.tableExists(TableName.valueOf("test"));
        System.out.println("是否存在:" + b);
        admin.close();
    }

    /**
     * 插入一个键值对
     */
    @Test
    public void testPut() {
        boolean b = HBaseUtil.insertOrUpdate("test", "cs001", "f1", "k1", "v1");
        System.out.println(b);
    }

    /**
     * 插入多个键值对
     */
    @Test
    public void testPutBatch() {
        Map<String,String> map = Maps.newHashMap();
        map.put("k2","k2");
        map.put("k1","k11");
        boolean b = HBaseUtil.insertFamilyBatch("test", "cs001", "f1",map );
        System.out.println(b);
    }

    /**
     *  多行 单列族插入
     */
    @Test
    public void insertBatch() {
        Map<String,Map<String,String>> map = Maps.newHashMap();

        Map<String,String> map1 = Maps.newHashMap();
        map1.put("k1","k1111");
        map1.put("a1","a1");
        Map<String,String> map2 = Maps.newHashMap();
        map2.put("b1","b1");

        map.put("cs002",map1);
        map.put("cs003",map2);
        boolean b = HBaseUtil.insertBatch("test", "f2",map);
        System.out.println(b);
    }

    /**
     * scan全表数据
     */
    @Test
    public void getResultScann() {
        Map<String, Map<String, String>> test = HBaseUtil.getResultScann("test");
        System.out.println("-----------------------");
        System.out.println(test);
    }

    /**
     * 扫描部分行
     */
    @Test
    public void getValueByStartStopRowKey() {
        Map<String, Map<String, String>> test = HBaseUtil.getValueByStartStopRowKey("test","qbk");
        System.out.println("-----------------------");
        System.out.println(test);
    }

    /**
     * 获取单行
     */
    @Test
    public void getFamilyListValue() {
        Map<String, Map<String, String>> test = HBaseUtil.getFamilyListValue("test","cs002");
        System.out.println("-----------------------");
        System.out.println(test);
    }

    /**
     * 获取多行
     */
    @Test
    public void getListValue() {
        Map<String, Map<String, String>> test = HBaseUtil.getListValue("test", Arrays.asList("cs002","cs001"));
        System.out.println("-----------------------");
        System.out.println(test);
    }
}
