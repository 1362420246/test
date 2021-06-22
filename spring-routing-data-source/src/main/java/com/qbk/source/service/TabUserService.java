package com.qbk.source.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.qbk.source.mapper.TabUserMapper;
import com.qbk.source.domain.TabUser;

@Service
public class TabUserService{

    @Resource
    private TabUserMapper tabUserMapper;

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

    public List<TabUser> selectList() {
        return tabUserMapper.selectList();
    }
}
