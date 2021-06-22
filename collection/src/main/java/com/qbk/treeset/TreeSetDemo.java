package com.qbk.treeset;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

/**
 *
 * TreeSet\Comparator中返回0导致数据丢失
 *
 * 返回值0非常特殊，是个大坑！
 * 返回0，表示不交换顺序，但表示两个元素相同，而在map中比较的是key，如果比较key发现相同，则会发生覆盖，进而造成数据丢失。
 */
public class TreeSetDemo {
    public static void main(String[] args) {
        TreeSet<Student> set = new TreeSet<>((s1,s2)->{
            int i = s1.getAge() - s2.getAge();
            if(i > 0 ){
                return 1 ;
            } else if( i == 0){
                //直接返回0 会导致相同的比较字段 的不同对象被覆盖
//                return 0;
                if(s1.equals(s2)){
                    //如果 是同一个对象 在覆盖
                    return 0;
                }else {
                    //如果是不同对象 但是 比较字段相同 不覆盖 排序不变
                    return 1;
                }
//                 if((s1.getSex() - s2.getSex()) > 0){
//                     return -1;
//                 }else {
//                     return 1;
//                 }
            }else {
                return -1 ;
            }
        });
        Student student = new Student("张三", 26, 3);

        set.add(new Student("张三",18,1));
        set.add(new Student("张三",17,2));
        set.add(student);
        set.add(student);
        set.add(new Student("张三",12,4));
        set.add(new Student("张三",26,5));

        System.out.println(set);
        System.out.println(set.size());
    }

    @Data
    @AllArgsConstructor
    private static class Student{
        private String name;
        private Integer age;
        private Integer sex;
    }

}
