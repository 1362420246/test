package com.qbk.collection;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author ：quboka
 * @description：list 交集对比
 * @date ：2019/12/4 10:50
 */
public class ListRetain {

    public static void main(String[] args) {
        List<User> users1 = new ArrayList<>();
        List<User> users2 = new ArrayList<>();
        for (int i = 1; i <= 10000; i++) {
            users1.add(User.builder().id(i).build());
            users2.add(User.builder().id(i * 2).build());
        }
        //测试 1.8 获取 id集合的效率
        long startTime = System.nanoTime();
        List<Integer> ids1 = users1.stream().map(User::getId).collect(Collectors.toList());
        long endTime = System.nanoTime();
        //耗时53毫秒
        System.out.println("1.8 从对象中取id的效率："+ TimeUnit.MILLISECONDS.convert((endTime - startTime),TimeUnit.NANOSECONDS));
        //测试 foreach id集合效率
        long startTime2 = System.nanoTime();
        List<Integer> ids2 = new ArrayList<>(users2.size() + (users2.size() >> 1) );
        for (User user : users2) {
            ids2.add(user.getId());
        }
        long endTime2 = System.nanoTime();
        //耗时2毫秒
        System.out.println("foreach 从对象中取id的效率："+ TimeUnit.MILLISECONDS.convert((endTime2 - startTime2),TimeUnit.NANOSECONDS));

        List<User> u1 = new ArrayList<User>(){
            {
                add(User.builder().id(1).build());
                add(User.builder().id(2).build());
                add(User.builder().id(3).build());
                add(User.builder().id(4).build());
            }
        };
        List<User> u2 = new ArrayList<User>(){
            {
                add(User.builder().id(6).build());
                add(User.builder().id(4).build());
                add(User.builder().id(2).build());
                add(User.builder().id(5).build());
            }
        };

        System.out.println("--------------------------华丽分割线----------------");

        //使用map查找交集
        //Multimap<String, User> resultMap = deviceContrast(u1, u2);
        Multimap<String, User> resultMap = deviceContrast(users1, users2);
        List<User> user1Add = (List<User>) resultMap.get("user1Add");
        Collection<User> user2Add = resultMap.get("user2Add");
        Collection<User> repetitionList = resultMap.get("repetitionList");
        System.out.println(user1Add);
        System.out.println(user2Add);
        System.out.println(repetitionList);

        System.out.println("--------------------------华丽分割线----------------");

        //使用contains 查找交集
        Multimap<String, User> getDiffrent = getDiffrent(users1, users2);
        List<User> user1Add2 = (List<User>) getDiffrent.get("user1Add");
        Collection<User> user2Add2 = getDiffrent.get("user2Add");
        Collection<User> repetitionList2 = getDiffrent.get("repetitionList");
        System.out.println(user1Add2);
        System.out.println(user2Add2);
        System.out.println(repetitionList2);

        System.out.println("--------------------------华丽分割线----------------");

        //使用jdk自带方法
        getDiffrentByJdk(users1, users2);
    }
    /**
     * 使用map查找交集
     */
    private static Multimap<String,User> deviceContrast(List<User> users1, List<User> users2) {
        long st = System.nanoTime();
        Multimap<String,User> resultMap = ArrayListMultimap.create();
        int n = users1.size() + users2.size() ;
        Map<Integer,Integer> map = new HashMap<>( n + (n >> 1));
        Map<Integer,User> copyMap = new HashMap<>(n + (n >> 1));

        //判定大小
        List<User> maxList = users1;
        List<User> minList = users2;

        List<User> maxAdd = new ArrayList<>() ;
        List<User> minAdd = new ArrayList<>() ;
        List<User> repetitionList = new ArrayList<>() ;

        //是否交换
        boolean isSwop = false;

        //交换
        if(users2.size() > users1.size()) {
            isSwop = true;
            maxList = users2;
            minList = users1;
        }

        // 遍历maxList集合
        for (User u : maxList) {
            map.put(u.getId() , 1);
            copyMap.put(u.getId() , u);
        }
        // 遍历minList集合
        for (User u : minList) {
            copyMap.put(u.getId() , u);
            Integer id = map.get(u.getId());
            if(id == null){
                //min 多的为
                map.put(u.getId(), 2);
                continue;
            }
            // 重复的为3
            map.put(u.getId(), 3);
        }
        for(Map.Entry<Integer, Integer> entry:map.entrySet()){
            if(entry.getValue() == 1){
                maxAdd.add(copyMap.get(entry.getKey()));
            }else if(entry.getValue() == 2){
                minAdd.add(copyMap.get(entry.getKey()));
            }else {
                repetitionList.add(copyMap.get(entry.getKey()));
            }
        }
        if(isSwop){
            resultMap.putAll("user2Add",maxAdd);
            resultMap.putAll("user1Add",minAdd);
        }else {
            resultMap.putAll("user1Add",maxAdd);
            resultMap.putAll("user2Add",minAdd);
        }
        resultMap.putAll("repetitionList",repetitionList);

        long et = System.nanoTime();
        System.out.println("map查找效率："+ TimeUnit.MILLISECONDS.convert((et - st),TimeUnit.NANOSECONDS));
        return resultMap;
    }


    /**
     *  使用contains 查找交集
     */
    private static Multimap<String,User> getDiffrent(List<User> users1, List<User> users2) {
        Multimap<String,User> resultMap = ArrayListMultimap.create();
        long st = System.nanoTime();
        List<Integer> ids = new ArrayList<>(users2.size() + (users2.size() >> 1) );
        for (User user : users2) {
            ids.add(user.getId());
        }
        List<User> user1Add = new ArrayList<>() ;
        List<User> repetitionList = new ArrayList<>() ;
        for(User user : users1) {
            if(ids.contains(user.getId())) {
                repetitionList.add(user);
            }else {
                user1Add.add(user);
            }
        }
        resultMap.putAll("user1Add",user1Add);

        Collection<User> tempList = new ArrayList<>(users2);
        tempList.removeAll(repetitionList);
        resultMap.putAll("user2Add",tempList);

        resultMap.putAll("repetitionList",repetitionList);
        long et = System.nanoTime();
        System.out.println("contains查找效率："+ TimeUnit.MILLISECONDS.convert((et - st),TimeUnit.NANOSECONDS));
        return resultMap;
    }

    /**
     * 使用jdk自带方法
     */
    private static void getDiffrentByJdk(List<User> users1, List<User> users2) {
        long st = System.nanoTime();
        List<Integer> ids1 = users1.stream().map(User::getId).collect(Collectors.toList());
        List<Integer> ids2 = users2.stream().map(User::getId).collect(Collectors.toList());
        Collection<Integer> realA = new ArrayList<>(ids1);
        Collection<Integer> realB = new ArrayList<>(ids2);
        // 求交集
        realA.retainAll(realB);
        System.out.println("交集结果：" + realA);
        Collection<Integer> aa = new ArrayList<>(realA);
        ids1.removeAll(aa);
        ids2.removeAll(aa);
        System.out.println("a差集：" + ids1);
        System.out.println("b差集：" + ids2);
        long et = System.nanoTime();
        System.out.println("jdk自带方法查找效率："+ TimeUnit.MILLISECONDS.convert((et - st),TimeUnit.NANOSECONDS));
    }
 
}
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id"})
class User{
    private Integer id;
}