package com.qbk.test;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * springboot2.2.0 集成的 junit5
 */
@SpringBootTest
class JunitTest5 {

    @BeforeAll
    static void initStatic(){
        System.out.println("测试前");
    }
    @AfterAll
    static void destoryStatic(){
        System.out.println("测试后");
    }

    /**
     * junit5
     */
    @Test
    @DisplayName("junit5测试用例")
    void junit5(){
        int statusCode = 200;
        String name = "kk";
        Integer age = 29;
        Assertions.assertAll("报错提醒",
                ()-> Assertions.assertEquals(200, statusCode),
                ()-> Assertions.assertEquals("kk", name),
                ()-> Assertions.assertNotNull(age)
        );
        System.out.println("测试成功");
    }

    /**
     * Spring Assert
     */
    @Test
    @DisplayName("SpringAssert测试用例")
    void springAssert(){
        int statusCode = 200;
        String name = "kk";
        Integer age = 29;
        Map sources = new HashMap(){{put("k","v");}};

        //org.springframework.util.Assert;
        Assert.notNull(age, "age must not be null");
        Assert.notEmpty(sources, "Sources must not be empty");
        Assert.isTrue(200 == statusCode,"请求错误" );
        Assert.isTrue("kk".equals(name),"名称错误" );
        System.out.println("测试成功");
    }

    /**
     * RestAssured 框架:用于轻松测试REST服务的Java DSL
     * REST Assured,是一个Java DSL,用于简化在HTTP构建器之上构建的REST基础服务的测试。
     * 它支持POST、GET、PUT、DELETE、OPTIONS、PATCH和HEAD请求,并可以用于验证和验证这些请求的响应。
     *
     * DSL（domain specific language），即领域专用语言：专门解决某一特定问题的计算机语言，比如大家耳熟能详的 SQL 和正则表达式。
     * 通用编程语言（如 Java、Kotlin、Android等），往往提供了全面的库来帮助开发者开发完整的应用程序，
     * 而 DSL 只专注于某个领域，比如 SQL 仅支持数据库的相关处理，而正则表达式只用来检索和替换文本，我们无法用 SQL 或者正则表达式来开发一个完整的应用。
     */
    @Test
    @DisplayName("RestAssured框架测试用例")
    void restassured(){
        /*
         待封装部分
         */
        Map<String,String> params = new HashMap<>();
        params.put("name","卡卡");
        String method = "post";
        String path = "/test5/post";

        /*
         设置基础路径
         */
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8099;
        //指定url是否应该自动对url进行编码
        RestAssured.urlEncodingEnabled = true;

        /*
            given阶段配置Headers和请求参数
            when阶段发送请求 (GET, POST, PUT, DELETE等)
            then阶段校验结果
         */
        RequestSpecification when =
                 given()//开始构建测试 restassure .specification的请求部分
                 //.header("Content-Type", "application/xml")//设置请求头
                 .contentType("application/json;charset=UTF-8")
                .log().all()//设置日志 所有内容，包括标题，cookies，正文
                .when();
        /*
            获取Response
            直接通过get(url), 类似的有post, put, delete等
            在then阶段, 使用extract().response(), 即可以在校验完成之后再额外提取响应结果
         */
        Response response = null;
        switch (method){
            case "get":
                for (Map.Entry<String, String> entry : params.entrySet()){
                    //get 参数
                    when = when.param(entry.getKey(),entry.getValue());
                }
                //发送请求
                response = when.get(path);
                break;
            case "post":
                //post参数
                when.body(params);
                //发送请求
                response = when.post(path);
                break;
            default:
                break;
        }
        System.out.println("-----------------------------------");
        //打印响应体并以字符串形式返回
        response.prettyPrint();
    }

}
