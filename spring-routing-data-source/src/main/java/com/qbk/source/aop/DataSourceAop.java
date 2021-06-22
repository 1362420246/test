package com.qbk.source.aop;

import com.qbk.source.annotation.DS;
import com.qbk.source.annotation.DSTransactional;
import com.qbk.source.config.DbKeyEnum;
import com.qbk.source.config.datasource.DBContextHolder;
import com.qbk.source.config.datasource.DynamicDataSource;
import com.qbk.source.config.tx.ConnectionFactory;
import com.qbk.source.config.tx.TransactionContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 数据库切换切面
 */
@Aspect
@Component
//@EnableAspectJAutoProxy
public class DataSourceAop {

    @Autowired
    private DynamicDataSource dynamicDataSource;

    private final AtomicInteger dbCounter = new AtomicInteger(0);

    @Pointcut("@annotation(ds)")
    public void dynamicPointCut(DS ds) {}

    /**
     * 切换库
     */
    @Around(value = "dynamicPointCut(ds)", argNames = "joinPoint,ds")
    public Object switchDb(ProceedingJoinPoint joinPoint, DS ds) throws Throwable {
        DBContextHolder.push(ds.value());
        Object proceed = null;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        }finally {
            DBContextHolder.clear();
        }
        return proceed;
    }

    /**
     * 主库写
     */
    @Before("@annotation(com.qbk.source.annotation.Master)")
    public void write() {
        DBContextHolder.push(DbKeyEnum.MASTER.getKey());
    }

    @After("@annotation(com.qbk.source.annotation.Master)")
    public void writeClear() {
        DBContextHolder.clear();
    }

    /**
     * 从库读
     * 负载均衡
     */
    @Before("@annotation(com.qbk.source.annotation.Slave)")
    public void read() {
        List<String> dbKeyList = new ArrayList<>();
        Set<Object> dbKeySet = dynamicDataSource.getDynamicTargetDataSources().keySet();
        for (Object key: dbKeySet) {
            String keyStr = (String)key;
            if(keyStr.startsWith("slave")){
                dbKeyList.add(keyStr);
            }
        }
        int next = incrementAndGetModulo(dbKeyList.size());
        DBContextHolder.push(dbKeyList.get(next));
    }

    @After("@annotation(com.qbk.source.annotation.Slave)")
    public void readClear() {
        DBContextHolder.clear();
    }

    /**
     * cas 做的自旋锁
     */
    private int incrementAndGetModulo(int modulo) {
        for (;;) {
            int current = dbCounter.get();
            int next = (current + 1) % modulo;
            if (dbCounter.compareAndSet(current, next)){
                return next;
            }
        }
    }

    /*************事务**************/

    @Pointcut("@annotation(dsTransactional)")
    public void txPointCut(DSTransactional dsTransactional) {}

    /**
     * 事务环绕 ，提交或者回滚
     */
    @Around(value = "txPointCut(dsTransactional)", argNames = "joinPoint,dsTransactional")
    public Object txAround(ProceedingJoinPoint joinPoint, DSTransactional dsTransactional) throws Throwable {
        boolean state = true;
        Object o ;
        String xid = UUID.randomUUID().toString();
        TransactionContext.bind(xid);
        try {
            o = joinPoint.proceed();
        } catch (Throwable throwable) {
            state = false;
            throw throwable;
        } finally {
            ConnectionFactory.notify(state);
            TransactionContext.remove();
        }
        return o;
    }
}
