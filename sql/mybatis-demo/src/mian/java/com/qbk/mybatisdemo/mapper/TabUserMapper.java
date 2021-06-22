package com.qbk.mybatisdemo.mapper;

import com.qbk.mybatisdemo.domain.TabUser;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 *
 */
public interface TabUserMapper {


    TabUser getOne(int userId);

    boolean add(TabUser tabUser);

    List<TabUser> list(RowBounds rb);

}
