package com.qbk.jdbcdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用于嵌套事务
 */
@Service
public class NestedService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional(propagation = Propagation.NESTED)
    public void nested(){
        jdbcTemplate.update("INSERT INTO tab_user(user_name) VALUES (?)","qbk");
    }
}
