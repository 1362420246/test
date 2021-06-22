package com.qbk.actuator.indicator;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

/**
 *  自定义健康检查方式1
 *  DataSourceHealthIndicator / RedisHealthIndicator 都是这种写法
 */
@Component("my2")
public class MyAbstractHealthIndicator extends AbstractHealthIndicator{

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        int code = check();
        if (code != 0) {
            builder.down().withDetail("code", code).withDetail("version", "v1.0.0").build();
        }
        builder.withDetail("code", code)
                .withDetail("version", "v1.0.0").up().build();

    }

    private int check() {
        return 0;
    }
}
