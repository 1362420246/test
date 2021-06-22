package com.qbk.jdk9demo.collections;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Java 9 List，Set 和 Map 接口中，新的静态工厂方法可以创建这些集合的不可变实例。
 */
public class Test {
    public static void main(String[] args) {
        Set<String> set = Set.of("A", "B", "C");
        System.out.println(set);

        List<String> list = List.of("A", "B", "C");
        System.out.println(list);

        Map<String, String> map = Map.of("A","Apple","B","Boy","C","Cat");
        System.out.println(map);

        Map<String, String> map1 = Map.ofEntries (
                new AbstractMap.SimpleEntry<>("A","Apple"),
                new AbstractMap.SimpleEntry<>("B","Boy"),
                new AbstractMap.SimpleEntry<>("C","Cat"));
        System.out.println(map1);
    }
}
