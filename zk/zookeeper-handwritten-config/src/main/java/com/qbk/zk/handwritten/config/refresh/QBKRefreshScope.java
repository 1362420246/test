package com.qbk.zk.handwritten.config.refresh;

import java.lang.annotation.*;

/**
 * 用于刷新的注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QBKRefreshScope {
}