package com.qbk.collection;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ：quboka
 * @description： jdk1.8 list交集差集
 * @date ：2019/12/4 10:36
 */
public class ListRepeat {
    public static void main(String[] args) {
        List<String> list1 = Arrays.asList("1","2","3","5","6");
        List<String> list2 = Arrays.asList("2","3","7","8");

        // 交集
        List<String> intersection = list1.stream().filter(list2::contains).collect(Collectors.toList());
        System.out.println("---交集 intersection---");
        intersection.parallelStream().forEach(System.out :: println);

        // 差集 (list1 - list2)
        List<String> reduce1 = list1.stream().filter(item -> !list2.contains(item)).collect(Collectors.toList());
        System.out.println("---差集 reduce1 (list1 - list2)---");
        reduce1.parallelStream().forEach(System.out :: println);

        // 差集 (list2 - list1)
        List<String> reduce2 = list2.stream().filter(item -> !list1.contains(item)).collect(Collectors.toList());
        System.out.println("---差集 reduce2 (list2 - list1)---");
        reduce2.parallelStream().forEach(System.out :: println);

        // 并集
        List<String> listAll = Stream.of(list1,list2).flatMap(Collection::stream).collect(Collectors.toList());
        // 去重并集
        List<String> listAll2 = Stream.of(list1,list2).flatMap(Collection::stream).distinct().collect(Collectors.toList());
        System.out.println("---并集 listAll---");
        listAll.parallelStream().forEachOrdered(System.out :: println);
        System.out.println("---并集 listAll---去重");
        listAll2.parallelStream().forEachOrdered(System.out :: println);

        System.out.println("---原来的List1---");
        list1.parallelStream().forEachOrdered(System.out :: println);
        System.out.println("---原来的List2---");
        list2.parallelStream().forEachOrdered(System.out :: println);
    }
}
