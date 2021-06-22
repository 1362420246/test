package com.qbk.sql.transaction.service;

import com.qbk.sql.transaction.domain.TabUser;
import com.qbk.sql.transaction.mapper.TabUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 锁与事务
 */
@Service
public class SynAndTranstactionService {

    @Autowired
    private TabUserMapper userMapper;

    /**
     * 测试方法锁  能不能锁住事务
     * 场景 先查询 再插入。 是否能插入多条
     *
     *  结果 方法锁是锁不住事务的，即方法结束锁释放，但因为事务使用aop，可能还未提交。
     *      别的线程获取了锁进入线程，因为事务的隔离级别不能读取到别的事务未提交的数据。
     *      导致后进来的线程 开启的新的事务 也查询不到数据，从而两个事务都执行插入，导致多线程并发安全问题。
     *
     * 排查思路： 开启多线程断点模式
     * 方法外一个断点
     * 方法内一个断点
     * org.springframework.transaction.interceptor.TransactionAspectSupport #invokeWithinTransaction  一个断点
     * org.springframework.jdbc.datasource.DataSourceTransactionManager #doCommit 一个断点
     * com.mysql.cj.jdbc.ConnectionImpl #commit() 一个断点
     */
    @Transactional
    public synchronized String selectAndInsert() {
       TabUser user = userMapper.getUserName("kaka");
       if(Objects.nonNull(user)){
           return "用户已存在";
       }
       user = new TabUser();
       user.setUserName("kaka");
       userMapper.insert(user);
       return "添加用户成功";
    }
}
