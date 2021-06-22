package com.qbk.mybatisdemo.Controller;

import com.qbk.mybatisdemo.domain.TabUser;
import com.qbk.mybatisdemo.mapper.TabUserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 *
 */
@RestController
public class TestController {

    @GetMapping
    public String get() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession session = sqlSessionFactory.openSession();
        TabUserMapper mapper = session.getMapper(TabUserMapper.class);
        TabUser tabUser = mapper.getOne(24);
        System.out.println(tabUser);

        mapper.add(tabUser);
        session.commit();


        int start = 0; // offset
        int pageSize = 5; // limit
        RowBounds rb = new RowBounds(start, pageSize);
        List<TabUser> list = mapper.list(rb);
        System.out.println(list);

        //session.close();

        return "S";
    }

}


