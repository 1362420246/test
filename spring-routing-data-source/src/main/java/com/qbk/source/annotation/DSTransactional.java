package com.qbk.source.annotation;

import java.lang.annotation.*;

/**
 * 多数据源事务
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DSTransactional {
}
