package com.qbk.guava.etrying.demo.service;

import com.github.rholder.retry.*;
import com.google.common.base.Predicates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * guava-retrying 重试
 */
@Slf4j
@Service
public class RetryerService {

    public Boolean test() throws Exception {
        //定义重试机制
        //RetryerBuilder是一个factory创建者，可以定制设置重试源且可以支持多个重试源，可以配置重试次数或重试超时时间，以及可以配置等待时间间隔，创建重试者Retryer实例。
        Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
                //retryIf 重试条件
                //RetryerBuilder的重试源支持Exception异常对象 和自定义断言对象，通过retryIfException 和retryIfResult设置，同时支持多个且能兼容。
                //retryIfException，抛出runtime异常、checked异常时都会重试，但是抛出error不会重试。
                .retryIfException()
                //retryIfRuntimeException只会在抛runtime异常的时候才重试，checked异常和error都不重试。
                .retryIfRuntimeException()
                //retryIfExceptionOfType允许我们只在发生特定异常的时候才重试，比如NullPointerException和IllegalStateException都属于runtime异常，也包括自定义的error
                .retryIfExceptionOfType(Exception.class)
                .retryIfException(Predicates.equalTo(new Exception()))
                //retryIfResult可以指定你的Callable方法在返回值的时候进行重试
                .retryIfResult(Predicates.equalTo(false))

                //等待策略：每次请求间隔1s
                .withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))
                //        WaitStrategy：等待时长策略（控制时间间隔），返回结果为下次执行时长：
                //        FixedWaitStrategy 固定等待时长策略。
                //        RandomWaitStrategy 随机等待时长策略（可以提供一个最小和最大时长，等待时长为其区间随机值）。
                //        IncrementingWaitStrategy 递增等待时长策略（提供一个初始值和步长，等待时间随重试次数增加而增加）。
                //        ExponentialWaitStrategy 指数等待时长策略。
                //        FibonacciWaitStrategy Fibonacci 等待时长策略。
                //        ExceptionWaitStrategy 异常时长等待策略。
                //        CompositeWaitStrategy 复合时长等待策略。

                //停止策略 : 尝试请求6次
                //StopStrategy：停止重试策略，提供三种:
                //StopAfterDelayStrategy 设定一个最长允许的执行时间；比如设定最长执行10s，无论任务执行次数，只要重试的时候超出了最长时间，则任务终止，并返回重试异常RetryException。
                //NeverStopStrategy 不停止，用于需要一直轮训知道返回期望结果的情况。
                //StopAfterAttemptStrategy 设定最大重试次数，如果超出最大重试次数则停止重试，并返回重试异常。
                .withStopStrategy(StopStrategies.stopAfterAttempt(6))
                //时间限制 : 某次请求不得超过2s , 类似: TimeLimiter timeLimiter = new SimpleTimeLimiter();
                .withAttemptTimeLimiter(AttemptTimeLimiters.fixedTimeLimit(2, TimeUnit.SECONDS))
                .build();

        //定义请求实现
        Callable<Boolean> callable = new Callable<Boolean>() {
            int times = 1;

            @Override
            public Boolean call() throws Exception {
                log.info("call times={}", times);
                times++;

                if (times == 2) {
                    throw new NullPointerException();
                } else if (times == 3) {
                    throw new Exception();
                } else if (times == 4) {
                    throw new RuntimeException();
                } else if (times == 5) {
                    return false;
                } else {
                    return true;
                }

            }
        };
        //利用重试器调用请求
        return retryer.call(callable);
    }
}
