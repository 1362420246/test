package com.qbk.springencrypt.encryption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 解密数据
 */
@Slf4j
@RestControllerAdvice
public class ParamEncryptRequestBodyAdvice implements RequestBodyAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        Method method = methodParameter.getMethod();
        if (Objects.isNull(method)) {
            return false;
        }
        boolean isEncrypt = method.isAnnotationPresent(Encrypt.class);
        if(isEncrypt){
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            request.setAttribute("isEncrypt" , isEncrypt);
        }
        return isEncrypt;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        return new HttpInputMessage() {
            @Override
            public InputStream getBody() throws IOException {
                String content = new BufferedReader(new InputStreamReader(httpInputMessage.getBody()))
                        .lines().collect(Collectors.joining(System.lineSeparator()));
                //.replace("\"", "")
                String decryptBody;
                // 未加密内容
                if (content.startsWith("{")) {
                    decryptBody = content;
                } else {
                    try {
                        //加密内容 解密
                        decryptBody = AESUtil.decrypt(content);
                    } catch (Exception e) {
                        log.error("参数解密失败",e);
                        return httpInputMessage.getBody();
                    }

                }
                return new ByteArrayInputStream(decryptBody.getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                return httpInputMessage.getHeaders();
            }
        };
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}
