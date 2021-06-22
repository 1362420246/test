package com.qbk.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * list删除
 */
public class ListDelDemo {

    /**
     * 输出
     */
    private static void print(List<String> list){
        for (String item : list) {
            System.out.println("元素值：" + item);
        }
    }

    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        list.add("beijing");
        list.add("shanghai");
        list.add("shanghai");
        list.add("guangzhou");
        list.add("shenzhen");
        list.add("hangzhou");
        remove1(list, "shanghai");

    }

    /**
     * 正确
     * 1.8 新方法
     */
    private static void remove1(List<String> list, String target){
        list.removeIf(target::equals);
        print(list);
    }

    /**
     * 正确
     * 1.8 新方法
     */
    private static void remove2(List<String> list, String target){
        list = list.stream().filter(
                item -> !target.equals(item)
        ).collect(Collectors.toList());
        print(list);
    }

    /*
     * 错误
     * java.lang.IndexOutOfBoundsException: Index: 5, Size: 5  数组越界异常
     */
    private static void remove11(List<String> list, String target){
        int size = list.size();
        for(int i = 0; i < size; i++){
            String item = list.get(i);
            if(target.equals(item)){
                list.remove(item);
            }
        }
        print(list);
    }

    /*
     * 错误
     * 字符串“shanghai”没有被删除，该方法解决了数组越界问题，但没有解决彻底删除数据的问题
     *
     * List 删除元素的逻辑是将目标元素之后的元素往前移一个索引位置，最后一个元素置为 null，同时 size - 1；这也就解释了为什么第二个“shanghai”没有被删除。
     *
     * remove11 与 remove12 是有区别的，remove12 中每次for(int i = 0; i < list.size(); i++)执行都会计算 size 值，比较耗性能。
     */
    private static void remove12(List<String> list, String target){
        for(int i = 0; i < list.size(); i++){
            String item = list.get(i);
            if(target.equals(item)){
                list.remove(item);
            }
        }
        print(list);
    }
    /*
     * 正确
     */
    public static void remove13(List<String> list, String target){
        int size = list.size();
        for(int i = size - 1; i >= 0; i--){
            String item = list.get(i);
            if(target.equals(item)){
                list.remove(item);
            }
        }
        print(list);
    }
    /*
     * 正确
     */
    public static void remove14(List<String> list, String target){
        for(int i = list.size() - 1; i >= 0; i--){
            String item = list.get(i);
            if(target.equals(item)){
                list.remove(item);
            }
        }
        print(list);
    }

    /*
     * 错误
     * java.util.ConcurrentModificationException 报并发安全的异常
     *
     * foreach 写法实际上是对的 Iterable、hasNext、next方法的简写
     *
     * 通过代码我们发现 Itr 是 ArrayList 中定义的一个私有内部类，在 next、remove方法中都会调用 checkForComodification 方法，
     * 该方法的作用是判断 modCount != expectedModCount是否相等，如果不相等则抛出ConcurrentModificationException异常。
     * 每次正常执行 remove 方法后，都会对执行expectedModCount = modCount赋值，保证两个值相等，那么问题基本上已经清晰了，
     * 在 foreach 循环中执行 list.remove(item);，对 list 对象的 modCount 值进行了修改，
     * 而 list 对象的迭代器的 expectedModCount 值未进行修改，因此抛出了ConcurrentModificationException异常。
     */
    public static void remove21(List<String> list, String target){
        for(String item : list){
            if(target.equals(item)){
                list.remove(item);
            }
        }
        print(list);
    }

    /*
     * 正确
     *
     * 通过 CopyOnWriteArrayList 解决了 List的并发问题。
     */
    public static void remove22(ArrayList<String> list, String target) {
        final CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<String>(list);
        for (String item : cowList) {
            if (item.equals(target)) {
                cowList.remove(item);
            }
        }
        print(cowList);
    }

    /*
     * 错误
     * 与执行 remove21 产生的异常一致，问题产生的原因也一致。
     */
    public static void remove31(List<String> list, String target){
        Iterator<String> iter = list.iterator();
        while (iter.hasNext()) {
            String item = iter.next();
            if (item.equals(target)) {
                list.remove(item);
            }
        }
        print(list);
    }
    /*
     * 正确
     */
    public static void remove32(List<String> list, String target){
        Iterator<String> iter = list.iterator();
        while (iter.hasNext()) {
            String item = iter.next();
            if (item.equals(target)) {
                iter.remove();
            }
        }
        print(list);
    }
}
