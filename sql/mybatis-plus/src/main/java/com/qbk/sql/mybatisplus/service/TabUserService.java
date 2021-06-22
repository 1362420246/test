package com.qbk.sql.mybatisplus.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbk.sql.mybatisplus.domain.TabUser;
import com.qbk.sql.mybatisplus.mapper.TabUserMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TabUserService extends ServiceImpl<TabUserMapper, TabUser>{

    @Transactional
    public TabUser testTransaction(TabUser user) {
        TabUser user1 = this.getOne(Wrappers.<TabUser>lambdaQuery().eq(TabUser::getUserName,user.getUserName()));
        System.out.println(user1);
        final boolean insert = this.save(user);
        System.out.println("插入结果：" + insert);
        user1 = this.getOne(Wrappers.<TabUser>lambdaQuery().eq(TabUser::getUserName,user.getUserName()),false);
        System.out.println(user1);
        //int i = 10 /0 ;
        return user1;
    }

    private static AtomicInteger id = new AtomicInteger(1);

    /**
     *自己注入自己  解决 this调用时 事务REQUIRES_NEW 不起作用
     */
    @Autowired
    private TabUserService tabUserService;

    /*
      REQUIRES_NEW : 如果没有，就新建一个事务；如果有，就将当前事务挂起。
       启动一个新的, 不依赖于环境的 "内部" 事务. 这个事务将被完全 commited 或 rolled back 而不依赖于外部事务,
       它拥有自己的隔离范围, 自己的锁, 等等. 当内部事务开始执行时, 外部事务将被挂起, 内务事务结束时, 外部事务将继续执行.
       结果： 内部事务发生异常 外部事务一起回滚，  外部事务发生异常  内部事务不回滚

       NESTED : 如果没有，就新建一个事务；如果有，就在当前事务中嵌套其他事务。
       结果： 内部事务发生异常 外部事务一起回滚，  外部事务发生异常  内部事务一起回滚
     */

    /**
     * 测试事务嵌套
     */
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.NESTED )
    public void testAdd() {
        TabUser user = TabUser.builder().userId(id.getAndIncrement()).userName("卡卡").build();
        this.baseMapper.testAdd(user);

        tabUserService.testAdd2();
//        this.testAdd2();

        int i = 10 /0 ;
    }

    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW )
    public void testAdd2() {
        TabUser user = TabUser.builder().userId(id.getAndIncrement()).userName("卡卡").build();
        this.baseMapper.testAdd(user);

//        int i = 10 /0 ;
    }

    /**
     * 测试 insert ignore 返回行数
     */
    public int testIgnore(String username) {
        return this.baseMapper.testIgnore(username);
    }
}
