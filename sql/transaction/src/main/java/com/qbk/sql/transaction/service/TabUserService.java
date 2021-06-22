package com.qbk.sql.transaction.service;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.qbk.sql.transaction.mapper.TabUserMapper;
import com.qbk.sql.transaction.domain.TabUser;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TabUserService{

    @Resource
    private TabUserMapper tabUserMapper;

    /**
     * 测试幻读
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public TabUser testTransaction(TabUser user) {
        TabUser user1 = tabUserMapper.selectByPrimaryKey(user.getUserId());
        System.out.println(user1);
        final int insert = tabUserMapper.insert(user);
        System.out.println("插入结果：" + insert);
        user1 = tabUserMapper.selectByPrimaryKey(user.getUserId());
        System.out.println(user1);
        //int i = 10 /0 ;
        return user1;
    }

    /**
     * 插件测试
     */
    public TabUser get(int i) {
        return tabUserMapper.selectByPrimaryKey(i);
    }

    public int deleteByPrimaryKey(Integer userId){
        return tabUserMapper.deleteByPrimaryKey(userId);
    }

    public int insert(TabUser record){
        return tabUserMapper.insert(record);
    }

    public int insertSelective(TabUser record){
        return tabUserMapper.insertSelective(record);
    }

    public TabUser selectByPrimaryKey(Integer userId){
        return tabUserMapper.selectByPrimaryKey(userId);
    }

    public int updateByPrimaryKeySelective(TabUser record){
        return tabUserMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TabUser record){
        return tabUserMapper.updateByPrimaryKey(record);
    }

    /**
     * 测试查询加锁
     */
    @Transactional(rollbackFor = Exception.class)
    public void forUpdate( String userName) {
        System.out.println("1");
        tabUserMapper.forUpdate(userName);
        TabUser user = new TabUser();
        user.setUserName("kk");
        tabUserMapper.insertSelective(user);
        System.out.println("2");
    }
}
