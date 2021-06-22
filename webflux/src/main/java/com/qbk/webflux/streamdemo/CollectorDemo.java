package com.qbk.webflux.streamdemo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.MapUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 收集器demo
 */
public class CollectorDemo {

    public static void main(String[] args) {
        List<User> list = new ArrayList<User>(){
            {
                add(User.builder().id(1).age(18).sex(true).name("kk1").build());
                add(User.builder().id(2).age(19).sex(true).name("kk2").build());
                add(User.builder().id(3).age(21).sex(false).name("kk3").build());
                add(User.builder().id(2).age(33).sex(false).name("kk4").build());
                add(User.builder().id(1).age(22).sex(false).name("kk5").build());
                add(User.builder().id(1).age(11).sex(true).name("kk6").build());
                add(User.builder().id(3).age(23).sex(false).name("kk7").build());
                add(User.builder().id(1).age(9).sex(true).name("kk8").build());
            }
        };

        //1.转换 集合
        Set<String> names = list.stream().map(User::getName).collect(Collectors.toSet());
        System.out.println("名字集合:" + names);

        //2.统计汇总
        IntSummaryStatistics ageStatistics = list.stream().collect(Collectors.summarizingInt(User::getAge));
        final long count = ageStatistics.getCount();
        final int max = ageStatistics.getMax();
        final int min = ageStatistics.getMin();
        final long sum = ageStatistics.getSum();
        final double average = ageStatistics.getAverage();
        System.out.println("年龄统计回总:" + ageStatistics);

        //3.分块
        Map<Boolean, List<User>> sexs = list.stream().collect(Collectors.partitioningBy(User::getSex));
        System.out.println("男女列表：" + sexs);
        //换一种直观的输出方式
        MapUtils.verbosePrint(System.out,"男女列表",sexs);

        //4.分组
        Map<Integer, List<User>> ids = list.stream().collect(Collectors.groupingBy(User::getId));
        MapUtils.verbosePrint(System.out,"按照id分组",ids);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    static class User{
        private Integer id;
        private Integer age;
        private Boolean sex;
        private String name;
    }

}
