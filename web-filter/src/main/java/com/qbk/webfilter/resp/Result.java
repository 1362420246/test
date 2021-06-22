package com.qbk.webfilter.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 统一返回值
 */
@Setter
@Getter
public class Result<T> implements Serializable {
    /**
     * 返回码
     */
    private int code;

    /**
     * 返回信息描述
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    private long currentTime;

    /**
     * 响应消息体(泛型)
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Trace trace;

    @Data
    public static class Trace {

        /**
         * 二级错误状态描述
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String subMsg;

        /**
         * 异常类型
         */
        private String errType;

        /**
         * 异常堆栈信息
         */
        private String errTrace;

        /**
         * 调用跟踪栈元素
         */
        private StackTraceElement stackTraceElements;
    }

    public static <T> Result<T> create(int errorCode, String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(errorCode);
        result.setData(data);
        result.setMessage(msg);
        result.setCurrentTime(System.currentTimeMillis());
        return result ;
    }
}

