package com.qbk.test4;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

/**
 * @RunWith(SpringRunner.class)， 就是指用SpringRunner来运行，其中 SpringJUnit4ClassRunner 和 SpringRunner 区别是什么？
 * SpringRunner是SpringJUnit4ClassRunner的别名
 *
 * @SpringBootTest 是SpringBoot的一个用于测试的注解，通过SpringApplication在测试中创建ApplicationContext。
 *
 * @AutoConfigureMockMvc 是用于自动配置MockMvc
 */
@SpringBootTest
@AutoConfigureMockMvc
public class MockTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 无须启动项目 可直接调用接口
     */
    @Test
    public void TestGet() throws Exception {
        /**
         * .perform() 执行一个MockMvcRequestBuilders请求。其中.get()表示发送get请求（可以使用get、post、put、delete等）；
         * .contentType()设置编码格式；.param()请求参数,可以带多个。
         *   andExpect()添加 MockMvcResultMatchers验证规则，验证执行结果是否正确。
         *   .andDo()添加 MockMvcResultHandlers结果处理器,这是可以用于打印结果输出。
         *   .andReturn()结果还回，然后可以进行下一步的处理。
         *
         *   MockMvcResultMatchers.status().isOk()  断言状态码
         *   MockMvcResultMatchers.jsonPath  断言结果 表达式JSON路径表达式 参考：https://github.com/json-path/JsonPath
         */
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/get")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("id", "15")
                        .param("name","kk")
                        .header("Authorization", "Bearer c3af4cf0-58c5-4953-aba5-e40e8c901a51")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))//断言数组长度为3
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        System.out.println(result);
        System.out.println("----------------");
        //返回值
        System.out.println(result.getResponse().getContentAsString());
        System.out.println("----------------");
        //状态
        System.out.println(result.getResponse().getStatus());
    }

    /**
     * post
     */
    @Test
    public void TestPost() throws Exception {
        String result = mockMvc.perform(
                MockMvcRequestBuilders.post("/post")
                        .contentType(MediaType.APPLICATION_JSON_UTF8) //json
                        .content("{\"name\":\"qbk\"}") //参数
        )
                .andExpect(MockMvcResultMatchers.status().isOk()) //断言状态码
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("201"))//断言josn 结果 code 值是201
                .andDo(MockMvcResultHandlers.print())//打印
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    /**
     * 上传
     */
    @Test
    public void whenUploadSuccess() throws Exception {
        String result = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/file")
                .file(new MockMultipartFile("file", "test.txt", "multipart/form-data", "hello upload".getBytes(StandardCharsets.UTF_8))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }


}
