package com.qbk.sql.transaction.service;

import com.qbk.sql.transaction.domain.TabUser;
import com.qbk.sql.transaction.mapper.TabUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 事务
 */
@Service
public class TransactionService {

    @Autowired
    public TabUserMapper userMapper;

    /**
     * 避免同一个类内部调用aop事务不生效
     */
    @Autowired
    public TransactionService service;

    /**
     * @REQUIRED : 默认事务类型，如果没有，就新建一个事务；如果有，就加入当前事务
     *
     * @REQUIRES_NEW : 如果没有，就新建一个事务；如果有就将当前事务挂起。
     * 当内部事务开始执行时, 外部事务将被挂起, 内务事务结束时, 外部事务将继续执行.
     *
     * @NESTED : 如果没有，就新建一个事务；如果有，就在当前事务中嵌套其他事务。
     */

    /**
     * 捕获异常 - 嵌套事务
     *
     * @REQUIRED:抛出异常
     * UnexpectedRollbackException: Transaction rolled back because it has been marked as rollback-only
     * 字面意思是"事务回滚了，因为它被标记了必须回滚"
     *
     * @REQUIRES_NEW：内层回滚，外层提交
     *
     * @NESTED：内层回滚，外层提交
     */
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED )
    public void catchException(){
        TabUser user = TabUser.builder().userName("捕获异常用户").build();
        this.userMapper.insertSelective(user);
        try {
            //内层异常
            service.innerException();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 内层异常
     */
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED )
    public void innerException(){
        TabUser user = TabUser.builder().userName("内层异常用户").build();
        this.userMapper.insertSelective(user);
        System.out.println(1/0);
    }

    /**
     * 外层异常 - 嵌套事务
     *
     * @REQUIRED：内外层都回滚
     *
     * @REQUIRES_NEW：内层提交，外层回滚
     *
     * @NESTED：内外层都回滚
     *
     */
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED )
    public void outerException(){
        TabUser user = TabUser.builder().userName("外层异常用户").build();
        this.userMapper.insertSelective(user);
        //正常插入
        service.insertUser();
        System.out.println(1/0);
    }

    /**
     * 正常插入
     */
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED )
    public void insertUser(){
        TabUser user = TabUser.builder().userName("正常插入用户").build();
        this.userMapper.insertSelective(user);
    }
}
