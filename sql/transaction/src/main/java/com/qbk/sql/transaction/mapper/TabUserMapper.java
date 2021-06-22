package com.qbk.sql.transaction.mapper;

import com.qbk.sql.transaction.domain.TabUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Created by Mybatis Generator 2020/04/25
*/
public interface TabUserMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(TabUser record);

    int insertSelective(TabUser record);

    TabUser selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(TabUser record);

    int updateByPrimaryKey(TabUser record);

    TabUser getUserName(@Param("userName") String userName);

    int updateBatch(@Param("list") List<TabUser> list );

    void forUpdate(@Param("userName")String userName);
}