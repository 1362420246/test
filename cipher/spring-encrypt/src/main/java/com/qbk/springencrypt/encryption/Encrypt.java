package com.qbk.springencrypt.encryption;

import java.lang.annotation.*;

/**
 * 进行参数加密和解密
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Encrypt {
}
