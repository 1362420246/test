package com.qbk.advice;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * RequestBodyAdvice 是对请求响应的json串进行处理，一般使用环境是处理解密的json串。
 * 需要在controller层中调用方法上加入@RequestMapping和@RequestBody;后者若没有，则RequestBodyAdvice实现方法不执行。
 */
@RestControllerAdvice
public class TestRequestBodyAdvice implements RequestBodyAdvice {

    /**
     *  是否拦截
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        System.out.println("RequestBodyAdvice"+1);
        //判断是否有此注解
        //只有为true时才会执行afterBodyRead
        return methodParameter.getParameterAnnotation(RequestBody.class) != null;
    }

    /**
     *  在读取和转换请求体之前调用
     *  @param httpInputMessage 请求目标方法参数
     *  @param methodParameter 目标类型，不一定与方法相同
     *  @param type 目标类型，不一定与方法*参数类型相同，例如{@code HttpEntity<String>}。
     *  @param converterType 用于反序列化主体的转换器
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return new HttpInputMessage() {
            @Override
            public InputStream getBody() throws IOException {

                HttpServletRequest request = ((ServletRequestAttributes) Objects
                        .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

                BufferedReader br = new BufferedReader(
                        new InputStreamReader(httpInputMessage.getBody(), "utf-8")
                );

                StringBuilder buffer = new StringBuilder();
                String temp;
                while ((temp = br.readLine()) != null) {
                    buffer.append(temp);
                }
                br.close();
                String bodyData = buffer.toString();
                System.out.println("参数:" + bodyData);
                return new ByteArrayInputStream(bodyData.getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                return httpInputMessage.getHeaders();
            }
        };
    }

    //    @Override
//    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
//        System.out.println("RequestBodyAdvice"+2);
//        //获取请求体
//        InputStream inputStream = httpInputMessage.getBody();
//        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
//        byte[] bytes = new byte[2048];
//        //标记
//        bufferedInputStream.mark(0);
//        //读取
//        bufferedInputStream.read(bytes);
//        System.out.println(new String(bytes));
//        //重置
//        bufferedInputStream.reset();
//        //存储使用的Jackson视图
//        return new MappingJacksonInputMessage(bufferedInputStream, httpInputMessage.getHeaders());
//    }

    /**
     * 在请求体转换为对象后调用
     */
    @Override
    public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        System.out.println("RequestBodyAdvice"+3);
        //仅仅打印请求参数
        Map<String,Object> map = (Map<String, Object>) o;
        System.out.println("请求参数为:" +  map);
        return o;
    }

    /**
     * 请求体为空时调用
     */
    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        System.out.println("RequestBodyAdvice"+4);
        return null;
    }


}
