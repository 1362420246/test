package com.qbk.jpa.demo.dao;

import com.qbk.jpa.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * 继承 JpaRepository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * 查询第一个
     * 按照年龄正序
     */
    User findFirstByOrderByAgeAsc();

    /**
     * 查询第一个
     * 按照年龄倒叙
     */
    User findTopByOrderByAgeDesc();

    /**
     * 年龄区间查询
     */
    List<User> findByAgeBetween(int startAge ,int endAge);

    /**
     * 按照名称查询一个
     */
    User findTopByUserNameOrderByIdDesc(String userName);

    /**
     * in查询
     */
    List<User> findByAgeIn(Set<Integer> ags);

    /**
     * 查询一条
     */
    User queryFirstByUserNameAndAge(String userName,int age);

    /**
     *  自定义语句查询
     *  无法使用limit 1
     *  原因是limit属于MySQL特有的特性，而@Query默认只接受通用SQL
     */
    @Query(value = "select u from User u where u.userName = :userName and u.age = :age")
    User getUserByUserNameAndAge2(@Param("userName")String userName,@Param("age") int age);

    /**
     * 自定义语句查询
     * 原生查询Native Queries
     */
    @Query(value = "select * from tab_user u where u.user_name = :userName and u.user_age = :age limit 1" ,
            nativeQuery = true)
    User queryByUserNameAndAge(@Param("userName")String userName,@Param("age") int age);

}
