package com.qbk.test4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * springboot 集成 Junit4
 */
@SpringBootTest
public class JunitTest4 {

    @Before
    public void init(){
        System.out.println("测试前");
    }
    @After
    public void destoryStatic(){
        System.out.println("测试后");
    }

    /**
     * junit4
     */
    @Test
    public void junitTest4() {
        int statusCode = 200;
        String name = "kk";
        int age = 29;
        Integer nul = 2;

        // org.junit.Assert
        Assert.assertEquals("请求错误",200,statusCode);
        Assert.assertEquals("名称错误","kk",name);
        Assert.assertTrue("年龄错误",age == 29);
        Assert.assertNotNull("nul不得为空",nul);
        System.out.println("测试成功");
    }
}
