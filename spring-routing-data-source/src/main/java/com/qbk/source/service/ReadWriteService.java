package com.qbk.source.service;

import com.qbk.source.annotation.DS;
import com.qbk.source.annotation.DSTransactional;
import com.qbk.source.annotation.Master;
import com.qbk.source.annotation.Slave;
import com.qbk.source.domain.TabUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 读写分离测试
 */
@Service
public class ReadWriteService {

    @Autowired
    private TabUserService tabUserService;

    /**
     * 测试 Slave 注解读
     */
    @Slave
    public List<TabUser> read() {
        return tabUserService.selectList();
    }

    /**
     * 测试 Master 注解写
     */
    @Master
    public List<TabUser> write(TabUser tabUser) {
        tabUserService.insert(tabUser);
        return tabUserService.selectList();
    }

    /**
     * 测试 DS 切换
     */
    @DS(value = "slave-2")
    public List<TabUser> slave() {
        return tabUserService.selectList();
    }

    @Autowired
    private ReadWriteService readWriteService;

    /**
     * 测试事务
     */
    @DSTransactional
    public void transaction() {
        TabUser user = new TabUser();
        user.setUserName("kk");
        readWriteService.insert1(user);
        readWriteService.insert2(user);
        int a = 10/0;
    }

    @Master
    public void insert1(TabUser user) {
        tabUserService.insert(user);
    }

    @Slave
    public void insert2(TabUser user) {
        tabUserService.insert(user);
    }
}
