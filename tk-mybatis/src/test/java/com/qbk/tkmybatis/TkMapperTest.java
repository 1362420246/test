package com.qbk.tkmybatis;

import com.qbk.tkmybatis.domain.TbUser;
import com.qbk.tkmybatis.mapper.TbUserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.List;

@SpringBootTest
public class TkMapperTest {

    @Autowired
    private TbUserMapper tbUserMapper;

    /**
     * 属性条件 ，查询条件使用等号
     */
    @Test
    public void select(){
        //根据实体中的属性进行查询，只能有一个返回值
        TbUser user = tbUserMapper.selectOne(TbUser.builder().loginName("quboka").build());
        System.out.println(user);
        //根据实体中的属性值进行查询
        List<TbUser> list = tbUserMapper.select(TbUser.builder().nickName("quboka").build());
        System.out.println(list);
        //查询全部结果
        List<TbUser> tbUsers = tbUserMapper.selectAll();
        System.out.println(tbUsers);
        //根据实体中的属性查询总数
        int count = tbUserMapper.selectCount(TbUser.builder().nickName("quboka").build());
        int count2 = tbUserMapper.selectCount(null);
        System.out.println(count);
        System.out.println(count2);
        //根据主键字段进行查询
        TbUser user1 = tbUserMapper.selectByPrimaryKey(1);
        System.out.println(user1);
    }

    /**
     * 自定义条件
     */
    @Test
    public void example(){
        //综合条件查询
        //SELECT ... FROM tb_user WHERE ( id > ? and id < ? ) or ( id < ? ) ORDER BY id DESC FOR UPDATE
        Example example = new Example(TbUser.class);
        example.setForUpdate(true);//锁
        example.createCriteria().andGreaterThan("id", 1).andLessThan("id",3);//大于1 小于3
        example.or().andLessThan("id", 2);// 或者 大于2
        example.orderBy("id").desc();//id倒序
        List<TbUser> users = tbUserMapper.selectByExample(example);
        System.out.println(users);

        //动态 SQL
        //SELECT .... FROM tb_user WHERE ( ( nick_name like ? ) )
        TbUser user = TbUser.builder().loginName("quboka").build();
        Example example2 = new Example(TbUser.class);
        Example.Criteria criteria2 = example2.createCriteria();
        if(user.getLoginName() != null){
            criteria2.andLike("nickName",  "zhang" + "%");
        }
        List<TbUser> users2 = tbUserMapper.selectByExample(example2);
        System.out.println(users2);

        //排序
        // ....FROM tb_user order by id DESC,nick_name ASC
        Example example3 = new Example(TbUser.class);
        example3.orderBy("id").desc().orderBy("nickName").asc();
        List<TbUser> users3 = tbUserMapper.selectByExample(example3);
        System.out.println(users3);

        //去重
        //SELECT distinct .... FROM tb_user
        Example example4 = new Example(TbUser.class);
        //设置 distinct
        example4.setDistinct(true);
        List<TbUser> users4 = tbUserMapper.selectByExample(example4);
        System.out.println(users4);

        //设置查询列
        // SELECT id , nick_name FROM tb_user
        Example example5 = new Example(TbUser.class);
        example5.selectProperties("id", "nickName");
        List<TbUser> users5 = tbUserMapper.selectByExample(example5);
        System.out.println(users5);

       // Example.builder 方式
        //SELECT nick_name FROM tb_user WHERE ( ( id > ? and nick_name like ? ) ) order by id Asc FOR UPDATE
        Example example6 = Example.builder(TbUser.class)
                .select("nickName")
                .where(Sqls.custom().andGreaterThan("id", 1)
                        .andLike("nickName","q" + "%"))
                .orderByAsc("id")
                .forUpdate()
                .build();
        List<TbUser> users6 = tbUserMapper.selectByExample(example6);
        System.out.println(users6);
    }
}
