package com.qbk.source.mapper;

import com.qbk.source.domain.TabUser;

import java.util.List;

/**
* Created by Mybatis Generator 2019/11/20
*/
public interface TabUserMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(TabUser record);

    int insertSelective(TabUser record);

    TabUser selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(TabUser record);

    int updateByPrimaryKey(TabUser record);

    List<TabUser> selectList();
}