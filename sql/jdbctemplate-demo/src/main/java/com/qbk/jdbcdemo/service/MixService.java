package com.qbk.jdbcdemo.service;

import com.qbk.jdbcdemo.domain.TabUser;
import com.qbk.jdbcdemo.mapper.TabUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * jdbctemplate mybatis 混合使用
 */
@Service
public class MixService {

    @Autowired
    private TabUserMapper userMapper;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private NestedService nestedService;

    /**
     * 混合查询
     */
    @Transactional
    public void mixQuery() {
        final TabUser tabUser1 = userMapper.selectByPrimaryKey(1);
        System.out.println("mybatis query :" + tabUser1);

        final TabUser tabUser2 = jdbcTemplate.queryForObject("SELECT * FROM tab_user WHERE user_id = ?", new Object[]{2}, new BeanPropertyRowMapper<>(TabUser.class));
        System.out.println("jdbcTemplate query :" + tabUser2);
    }

    /**
     * 混合插入
     */
    @Transactional
    public void mixInsert() {
        for (int i = 0; i < 10; i++) {
            try {
                userMapper.insert(TabUser.builder().userName("kk").build());
                int a = 10/0;
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                jdbcTemplate.update("INSERT INTO tab_user(user_name) VALUES (?)","qbk");
                int a = 10/0;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void mixNested() {
        for (int i = 0; i < 10; i++) {
            try {
                userMapper.insert(TabUser.builder().userName("kk").build());
                int a = 10/0;
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                nestedService.nested();
                int a = 10/0;
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
