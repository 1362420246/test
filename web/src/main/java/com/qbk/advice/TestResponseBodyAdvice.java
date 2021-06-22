package com.qbk.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * ResponseBodyAdvice是对请求响应后对结果的处理，一般使用环境是处理加密的json串。
 * 需要在controller层中调用方法上加入@RequstMapping和@ResponseBody;后者若没有，则ResponseBodyAdvice实现方法不执行。
 */
@RestControllerAdvice
public class TestResponseBodyAdvice implements ResponseBodyAdvice<Map> {
    /**
     *  判断是否支持
     */
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        System.out.println("ResponseBodyAdvice"+1);
        //返回值类型是否带泛型
        Type type = returnType.getGenericParameterType();
        return type instanceof ParameterizedType;
    }

    /**
     * 选择{@code HttpMessageConverter}之后和之前调用
     */
    @Override
    public Map beforeBodyWrite(Map body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        System.out.println("ResponseBodyAdvice"+2);
        System.out.println("返回结果："+body);
        return body;
    }
}
