package com.qbk.jpa.demo;

import com.qbk.jpa.demo.dao.UserRepository;
import com.qbk.jpa.demo.entity.Sex;
import com.qbk.jpa.demo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
public class SpringDataJpaDemoTest {

    @Autowired
    private UserRepository userRepository;

    /*
    *  Spring Data 默认方法
    *        findAll()
    *        findAll(Sort)
    *        findAll（(Example<s>)
    *        findAll〔Example<s>, sort)
    *        findAll(Pageable)
    *        findAll(Example<s>, Pageable)
    *        findAllById(Iterable<ID> ids)
    *
    *        getOne(ID id);
    *        findOne(Example<S> example);
    *
    *        long count();
    *        long count(Example<S> example);
    *
    *        boolean exists(Example<S> example);
    *
    *        save(S entity);
    *        saveAndFlush(S entity);
    *        saveAll(Iterable<S> entities);
    *
    *        deleteById(ID id);
    * */
    @Test
    public void testBaseQuery() throws Exception {

        //查询所有
        final List<User> all = userRepository.findAll();
        System.out.println("查询所有:" + all);

        //排序
        Sort sort = Sort.by( Sort.Order.desc("id"));
        final List<User> allsort = userRepository.findAll(sort);
        System.out.println("排序:" + allsort);

        //根据ids查询
        final List<User> allById = userRepository.findAllById(Arrays.asList(1, 3));
        System.out.println("根据ids查询:" + allById);

        //条件查询
        User user = new User();
        user.setUserName("quboka");
        Example<User> example = Example.of(user);
        final List<User> allByexample = userRepository.findAll(example);
        System.out.println("动态条件查询:" + allByexample);

        User user2 = new User();
        user2.setUserName("q");
        ExampleMatcher matcher2 = ExampleMatcher.matching()
                .withMatcher("userName", ExampleMatcher.GenericPropertyMatchers.startsWith());//模糊查询匹配开头，即{userName}%
        Example<User> example2 = Example.of(user2 ,matcher2);
       // List<User> list = userRepository.findAll(example2);
        List<User> list = userRepository.findAll(example2,sort);
        System.out.println("复杂条件查询:" + list);

        User user3 = new User();
        user3.setUserName("b");
        //lambda 写法
        ExampleMatcher matcher3 = ExampleMatcher.matching().withMatcher("userName", m -> m.contains());//全模糊搜索
        final Example<User> example3 = Example.of(user3, matcher3);
        final List<User> list2 = userRepository.findAll(example3);
        System.out.println("复杂条件查询2:" + list2);

        //分页查询
        Sort sort2 = new Sort(Sort.Direction.DESC,"id");
        //分页起始页为0
        Pageable pageable = PageRequest.of(0,2,sort2);
        final Page<User> page = userRepository.findAll(example3, pageable);
        System.out.println("分页复杂条件查询:" + page.get().collect(Collectors.toList()));
        System.out.println("分页复杂条件查询:" + page.getContent());
        System.out.println("分页复杂条件查询:" + page);

        //通过id查询一个
        final User user4 = userRepository.getOne(4);
        System.out.println(user4);

        //通过条件查询一个 多个会抛异常
        User user5 = new User();
        user5.setUserName("quboka");
        Example<User> example5 = Example.of(user5);
        Optional<User> optionalUser = userRepository.findOne(example5);
        System.out.println(optionalUser.orElseThrow(Exception::new));

        //总数
        final long count = userRepository.count(example5);
        System.out.println("总数:" + count);

        //是否存在
        final boolean exists = userRepository.exists(example5);
        System.out.println("是否存在:" + exists);

        //添加 / 修改
        User user6 = new User();
        user6.setId(15);
        user6.setUserName("kk1");
        user6.setAge(1);
        user6.setSex(Sex.MAN);
        user6.setCreateDatetime(new Date());
       // final User save = userRepository.save(user6);
        final User save = userRepository.saveAndFlush(user6);
        System.out.println("添加:" + save);

        //批量添加 / 修改
        User user7 = new User();
        user7.setUserName("kk2");
        user7.setAge(1);
        user7.setSex(Sex.MAN);
        user7.setCreateDatetime(new Date());
        User user8 = new User();
        user8.setUserName("kk3");
        user8.setAge(1);
        user8.setSex(Sex.MAN);
        user8.setCreateDatetime(new Date());
        final List<User> users = userRepository.saveAll(Arrays.asList(user8, user7));
        System.out.println("批量添加:" + users);

        //删除
        final boolean exists2 = userRepository.existsById(17);
        if(exists2){
            //不存在会报错
            userRepository.deleteById(17);
        }
    }

    /*
     *  自定义方法查询
     */
    @Test
    public void testQuery() throws Exception {
        final User user1 = userRepository.findFirstByOrderByAgeAsc();
        final User user2 = userRepository.findTopByOrderByAgeDesc();
        System.out.println(user1);
        System.out.println(user2);

        final List<User> byAgeBetween = userRepository.findByAgeBetween(30, 40);
        System.out.println(byAgeBetween);

        final User user3 = userRepository.findTopByUserNameOrderByIdDesc("qbk");
        System.out.println(user3);

        final List<User> byAgeIn = userRepository.findByAgeIn(new HashSet<>(Arrays.asList(17, 18, 19)));
        System.out.println(byAgeIn);
    }

    /*
  *  自定义sql查询
  */
    @Test
    public void testSqlQuery() throws Exception {
        final User user1 = userRepository.queryFirstByUserNameAndAge("kk3", 1);
        System.out.println(user1);

        final User user2 = userRepository.queryByUserNameAndAge("kk3", 1);
        System.out.println(user2);

        final User user3 = userRepository.getUserByUserNameAndAge2("kk3", 35);
        System.out.println(user3);
    }
}
