package com.qbk.data.structure;

import java.util.*;
import java.util.concurrent.*;
/*
* 跳表（SkipList）这种数据结构算是以前比较少听说过，它所实现的功能与红黑树，AVL树都差不太多，说白了就是一种基于排序的索引结构，
* 它的统计效率与红黑树差不多，但是它的原理，实现难度以及编程难度要比红黑树简单。
* 另外它还有一个平衡的树形索引机构没有的好处，这也是引导自己了解跳表这种数据结构的原因，就是在并发环境下其表现很好.
* 这里可以想象，在没有了解SkipList这种数据结构之前，如果要在并发环境下构造基于排序的索引结构，那么也就红黑树是一种比较好的选择了，
* 但是它的平衡操作要求对整个树形结构的锁定，因此在并发环境下性能和伸缩性并不好.
* 在Java中，skiplist提供了两种：
* ConcurrentSkipListMap 和 ConcurrentSkipListSet
* 两者都是按自然排序输出。
*/
public class SkipListDemo {

    public static void main(String[] args) {
        skipListMapshow();
        skipListSetshow();
    }

    private static void skipListMapshow(){
        Map<Integer,String> map= new ConcurrentSkipListMap<>();
        map.put(1, "1");
        map.put(23, "23");
        map.put(3, "3");
        map.put(2, "2");

        for(Integer key : map.keySet()){
            System.out.println(map.get(key));
        }
    }

    private static void skipListSetshow(){
        Set<Integer> mset= new ConcurrentSkipListSet<>();
        mset.add(1);
        mset.add(21);
        mset.add(6);
        mset.add(2);
        //输出是有序的，从小到大。
        //skipListSet result=[1, 2, 6, 21]
        System.out.println("ConcurrentSkipListSet result="+mset);

        Set<String> myset = new ConcurrentSkipListSet<>();
        System.out.println(myset.add("abc"));
        System.out.println(myset.add("fgi"));
        System.out.println(myset.add("def"));
        System.out.println(myset.add("Abc"));
        /*
         * 输出是有序的:ConcurrentSkipListSet contains=[Abc, abc, def, fgi]
         */
        System.out.println("ConcurrentSkipListSet contains="+myset);
    }
}