package com.qbk.lockweb;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * lock 限流测试
 * trylock 尝试获取锁
 * https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/locks/Lock.html
 */
@RestController
@RequestMapping("/lock")
public class LockController {

    private final static Lock LOCK = new ReentrantLock();

    @GetMapping("/get")
    public String get(){
        /*
            lock.tryLock()
            仅当锁在调用时处于空闲状态时才获取锁。
            如果锁可用，则获取锁，并立即返回值true。如果锁不可用，则此方法将立即返回值false。
         */
        if (LOCK.tryLock()) {
            /*
            【强制】在使用阻塞等待获取锁的方式中，必须在 try 代码块之外，并且在加锁方法与 try 代码块之间没有任何可能抛出异常的方法调用，
                    避免加锁成功后，在 finally 中无法解锁。
            说明一：如果在 lock 方法与 try 代码块之间的方法调用抛出异常，那么无法解锁，造成其它线程无法成功获取锁。
            说明二：如果 lock 方法在 try 代码块之内，可能由于其它方法抛出异常，导致在 finally 代码块中，
                unlock 对未加锁的对象解锁，它会调用 AQS 的 tryRelease 方法（取决于具体实现类），抛出IllegalMonitorStateException 异常。
            说明三：在 Lock 对象的 lock 方法实现中可能抛出 unchecked 异常，产生的后果与说明二相同
             */
            /*
            【强制】在使用尝试机制来获取锁的方式中，进入业务代码块之前，必须先判断当前线程是否持有锁。锁的释放规则与锁的阻塞等待方式相同。
            说明：Lock 对象的 unlock 方法在执行时，它会调用 AQS 的 tryRelease 方法（取决于具体实现类），如果
                当前线程不持有锁，则抛出 IllegalMonitorStateException 异常。
             */
            try {
                TimeUnit.SECONDS.sleep(2);
                //int i = 10 / 0;
                //lock.tryLock();
                return "成功";
            }catch (Exception e){
                return "失败";
            }finally {
                //释放锁写在finally里，无论try中还是catch中的return最后都会走finally
                LOCK.unlock();
            }
        } else {
            //没有获取到锁
            return "稍后再试";
        }
    }
}
