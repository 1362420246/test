package com.qbk.myclient;

/**
 * 测试自定义redis客户端
 * 无密码配置
 **/
public class MainClient {
    public static void main(String[] args) {
        CustomerRedisClient customerRedisClient=new CustomerRedisClient("127.0.0.1",6379);
        System.out.println(customerRedisClient.set("name","qbk"));
        System.out.println(customerRedisClient.get("name"));
    }
}
