package com.qbk.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ArrayList添加数据
 */
public class ArrayListAddTest {
    public static void main(String[] args) {
        //使用匿名内部类 + 代码块 的方式
        List<User> users = new ArrayList<User>() {{
            add(new User(2, "Van"));
            add(new User(1, "Tom"));
            add(new User(4, "Jesse"));
            add(new User(3, "Randy"));

        }};
        System.out.println(users);
        System.out.println(users.getClass());
        //构造一个包含指定元素的列表
        List<User> fewUsers = new ArrayList<User>(Arrays.asList(
                new User(2, "Van"),
                new User(5, "Lance"),
                new User(6, "Evan"),
                new User(4, "Jesse")
        ));
        System.out.println(fewUsers);
        System.out.println(fewUsers.getClass());
        //返回一个空列表(不可变)。这个列表是可序列化的。
        List<Integer> emptyList = Collections.emptyList();
        //emptyList.add(1);
        System.out.println(emptyList);
    }

    static class User{
        private Integer id;
        private String name;
        User(){}
        User(Integer id,String name){
            this.id = id;
            this.name = name;
        }
        public Integer getId() {
            return id;
        }
        public void setId(Integer id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
