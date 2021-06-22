package com.qbk.webfilter.exception.handler;

import com.qbk.webfilter.exception.BusinessException;
import com.qbk.webfilter.i18n.MessageSourceService;
import com.qbk.webfilter.resp.Result;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.mail.MailException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static com.qbk.webfilter.exception.handler.CodeConstant.*;

/**
 * 异常处理类
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandle {

    /**
     * 英文空格
     */
    private static final String STRING_BLANK = " ";

    /**
     * 正则包名过滤
     */
    private static final String REG_FRAMEWORK_PKG = "com.qbk.(?!plat).+";

    /**
     * MessageSourceService
     */
    @Autowired
    private MessageSourceService messageSourceService;

    /**
     * 业务异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        log.warn("业务异常!框架捕获到异常:[{}][{}]", e.getCode(), e.getMessage());

        Result<Object> result = newErrResponse(e);
        result.setCode(e.getCode());
        result.setMessage(e.getMessage());
        result.setData(e.getBody());
        return result;
    }

    /**
     * Validation参数校验错误
     *
     * @param e 异常
     * @return 异常结果
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({
            BindException.class,
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class
    })
    public Result<?> handleValidationException(Exception e) {
        log.warn("框架捕获到参数校验异常:{}", e.getMessage());

        Result<Object> result = newErrResponse(e);

        if (e instanceof ConstraintViolationException) {
            result.setCode(ERR_METHOD_ARGUMENT_NOT_VALID);
            ConstraintViolationException ex = (ConstraintViolationException) e;
            Set<ConstraintViolation<?>> cvs = ex.getConstraintViolations();
            if (CollectionUtils.isEmpty(cvs)) {
                result.setMessage(messageSourceService.getMessage(result.getCode()));
            } else {
                result.setMessage(cvs.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(",")));
            }
            result.getTrace().setSubMsg(cvs.stream().map(cv -> "[" + cv.getPropertyPath() + "]" + cv.getMessage()).collect(Collectors.joining(",")));
            return result;
        }
        List<FieldError> fieldErrors;
        if (e instanceof BindException) {
            result.setCode(ERR_BIND);
            BindException ex = (BindException) e;
            fieldErrors = ex.getBindingResult().getFieldErrors();
        } else {
            // 参数无效，如JSON请求参数违反约束
            result.setCode(ERR_METHOD_ARGUMENT_NOT_VALID);
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            fieldErrors = ex.getBindingResult().getFieldErrors();
        }
        if (!CollectionUtils.isEmpty(fieldErrors)) {
            result.setMessage(fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(",")));
            result.getTrace().setSubMsg(fieldErrors.stream().map(fieldError -> "[" + fieldError.getField() + "]" + fieldError.getDefaultMessage()).collect(Collectors.joining(",")));
        } else {
            result.setMessage(messageSourceService.getMessage(result.getCode()));
        }
        return result;
    }

    /**
     * Controller上一层相关异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            MissingPathVariableException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            HttpMediaTypeNotAcceptableException.class,
            ServletRequestBindingException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class
    })
    public Result<?> handleServletException(Exception e) {
        log.error("框架捕获到系统异常:", e);

        Result<Object> result = newErrResponse(e);

        if (e instanceof NoHandlerFoundException) {
            // 找不到处理器异常
            result.setCode(ERR_NO_HANDLER_FOUND);
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            // 请求方法不支持异常
            result.setCode(ERR_HTTP_REQUEST_METHOD_NOT_SUPPORTED);
        } else if (e instanceof HttpMediaTypeNotSupportedException) {
            // 请求类型不支持异常
            result.setCode(ERR_HTTP_MEDIA_TYPE_NOT_SUPPORTED);
        } else if (e instanceof MissingPathVariableException) {
            // 缺失路径变量异常
            result.setCode(ERR_MISSING_PATH_VARIABLE);
        } else if (e instanceof TypeMismatchException) {
            // 缺失路径变量异常
            result.setCode(ERR_TYPE_MISMATCH);
        } else if (e instanceof HttpMessageNotReadableException) {
            // HttpMessage不可读异常
            result.setCode(ERR_HTTP_MESSAGE_NOT_READABLE);
        } else if (e instanceof HttpMessageNotWritableException) {
            // HttpMessage不可写异常
            result.setCode(ERR_HTTP_MESSAGE_NOT_WRITABLE);
        } else if (e instanceof HttpMediaTypeNotAcceptableException) {
            // 请求类型不接受异常
            result.setCode(ERR_HTTP_MEDIA_TYPE_NOT_ACCEPTABLE);
        } else if (e instanceof ServletRequestBindingException) {
            // Servlet请求绑定异常
            result.setCode(ERR_REQUEST_BINDING);
        } else if (e instanceof MissingServletRequestPartException) {
            // 缺失Servlet请求异常
            result.setCode(ERR_MISSING_SERVLET_REQUEST_PART);
        } else if (e instanceof AsyncRequestTimeoutException) {
            // 异步请求超时异常
            result.setCode(ERR_ASYNC_REQUEST_TIMEOUT);
        }
        result.setMessage(messageSourceService.getMessage(result.getCode()));
        return result;
    }

    /**
     * 其他常见异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({
            NullPointerException.class,
            IllegalArgumentException.class,
            IllegalStateException.class,
            ArithmeticException.class,
            ClassCastException.class,
            NegativeArraySizeException.class,
            ArrayIndexOutOfBoundsException.class,
            NoSuchMethodException.class,
            MailException.class,
            SQLException.class,
            IOException.class,
    })
    public Result<?> handleCommonException(Exception e) {
        log.error("框架捕获到异常:", e);

        Result<Object> result = newErrResponse(e);
        if (e instanceof NullPointerException) {
            // 空指针异常
            result.setCode(ERR_NULL_POINTER);
        } else if (e instanceof IllegalArgumentException) {
            // 非法参数异常
            result.setCode(ERR_ILLEGAL_ARGUMENT);
        } else if (e instanceof IllegalStateException) {
            // 非法状态异常
            result.setCode(ERR_ILLEGAL_STATE);
        } else if (e instanceof ArithmeticException) {
            // 计算异常
            result.setCode(ERR_ARITHMETIC);
        } else if (e instanceof ClassCastException) {
            // 类型转换异常
            result.setCode(ERR_CLASS_CAST);
        } else if (e instanceof NegativeArraySizeException) {
            // 集合负数异常
            result.setCode(ERR_NEGATIVE_ARRAY_SIZE);
        } else if (e instanceof ArrayIndexOutOfBoundsException) {
            // 集合超出范围异常
            result.setCode(ERR_ARRAY_INDEX_OUT_OF_BOUNDS);
        } else if (e instanceof NoSuchMethodException) {
            // 方法未找到异常
            result.setCode(ERR_NO_SUCH_METHOD);
        } else if (e instanceof MailException) {
            // 邮件发送异常
            result.setCode(ERR_MAIL_SEND);
        } else if (e instanceof SQLException) {
            // SQL异常
            result.setCode(ERR_SQL);
        } else if (e instanceof IOException) {
            // 读写异常
            result.setCode(ERR_IO);
        }
        result.setMessage(messageSourceService.getMessage(result.getCode()));
        return result;
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler( Exception.class )
    public Result<?> exception(Exception e) {
        log.error("其他异常:", e);
        Result<Object> result = newErrResponse(e);
        // 读写异常
        result.setCode(ERR_UNKNOWN);
        result.setMessage(messageSourceService.getMessage(result.getCode()));
        return result;
    }

    public static Result<Object> newErrResponse(Exception e) {
        Result<Object> result = new Result<>();
        Result.Trace trace = new Result.Trace();
        result.setTrace(trace);
        trace.setErrType(e.getClass().getName());
        trace.setStackTraceElements(filterStackTraceElement(e.getStackTrace()));
        trace.setErrTrace(e.getMessage());
        return result;
    }

    /**
     * 获取异常调用栈信息
     *
     * @param stackTraceElements 异常调用栈列表
     * @return 过滤后的调用栈
     */
    public static StackTraceElement filterStackTraceElement(StackTraceElement[] stackTraceElements) {
        if (CollectionUtil.isNotEmpty(Arrays.asList(stackTraceElements))) {
            return Arrays.stream(stackTraceElements).filter(e -> e.getClassName().matches(REG_FRAMEWORK_PKG)).limit(1)
                    .findFirst().orElse(null);
        }
        return null;
    }
}
