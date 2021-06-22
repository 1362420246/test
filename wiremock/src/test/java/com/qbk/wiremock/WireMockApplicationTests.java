package com.qbk.wiremock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * @author ：quboka
 * @description：测试wiremock中的数据 （需要提前把数据加入进wiremock中）
 * @date ：2019/12/5 18:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WireMockApplicationTests {

    @Before
    public void init(){
          /*
         设置基础路径
         */
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8062;
        //指定url是否应该自动对url进行编码
        RestAssured.urlEncodingEnabled = true;
    }

    @Test
    public void test() throws Exception {

        /*
            given阶段配置Headers和请求参数
            when阶段发送请求 (GET, POST, PUT, DELETE等)
            then阶段校验结果
         */
        RequestSpecification when = RestAssured.given()
                        //.header("Content-Type", "application/xml")//设置请求头
                        .contentType("application/json;charset=UTF-8")
                        .log().all()//设置日志 所有内容，包括标题，cookies，正文
                        .when();
        /*
            获取Response
            直接通过get(url), 类似的有post, put, delete等
            在then阶段, 使用extract().response(), 即可以在校验完成之后再额外提取响应结果
         */
        Response response = when.get("/order/2");
        System.out.println("-----------------------------------");
        //打印响应体并以字符串形式返回
        response.prettyPrint();
    }

}
