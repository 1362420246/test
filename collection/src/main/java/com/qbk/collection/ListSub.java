package com.qbk.collection;

import java.util.ArrayList;
import java.util.List;

/**
 * subList 快照 、快速失败（fail-fast ）
 * java中的subList是对list对象进行快照选取
 *
 * 快照：在新集合中添加或删除元素时，原集合也会发生相应改变。
 *
 * 快速失败：但是如果在原集合中删除或添加元素，调用原集合中的方法没问题，但当调用subList方法生成的集合的方法时就会产生异常。
 *
 */
public class ListSub {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        List<String> subList =  list.subList(0, 2);
        System.out.println("list = " + list); // [1, 2, 3]
        System.out.println("subList = " + subList); // [1, 2]

        //在新集合中添加或删除元素时，原集合也会发生相应改变。
        subList.add("4");
        System.out.println("list = " + list);//[1, 2, 4, 3]
        System.out.println("subList = " + subList);//[1, 2, 4]

        //在subList场景中，高度注意对原列表的修改，会导致子列表的遍历、增加、删除均产生ConcurrentModificationException异常
        list.add("5");
        System.out.println("list = " + list); // [1, 2, 4, 3, 5]
        System.out.println("subList = " + subList); //触发遍历 ConcurrentModificationException
    }
}
