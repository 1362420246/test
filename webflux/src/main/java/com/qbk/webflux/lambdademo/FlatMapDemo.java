package com.qbk.webflux.lambdademo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 将流展开 ，拆解流,将流中每一个元素拆解成一个流
 */
public class FlatMapDemo {
    public static void main(String[] args) {
        List<String[]> collect = new ArrayList<>();
        String[] arr1 = {"a","b"};
        String[] arr2 = {"c","d"};
        collect.add(arr1);
        collect.add(arr2);
        List<String> list = collect.stream().flatMap(Arrays::stream).collect(Collectors.toList());
        System.out.println(list);
    }
}
