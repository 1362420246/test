package com.qbk.sql.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qbk.sql.mybatisplus.domain.TabUser;

/**
* Created by Mybatis Generator 2020/04/25
*/
public interface TabUserMapper extends BaseMapper<TabUser> {
    void testAdd(TabUser user);

    /**
     * 测试 insert ignore 返回行数
     */
    int testIgnore(String username);
}