package com.qbk.collection;

import cn.hutool.core.util.ArrayUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 数组扩容
 */
public class ArrayGrow {
    public static void main(String[] args) {
        int len = 3 ;
        String[] resource = new String[len];
        resource[0] = "a";
        resource[1] = "b";
        resource[2] = "c";
        System.out.println("原数组长度：" + resource.length);
        //用流打印
        Arrays.stream(resource).forEach(System.out::print);
        System.out.println();

        //1.直接数组扩容
        String[] copy1 = new String [len + (len >> 1)];
        //遍历复制
        IntStream.range(0,len).forEach( i -> copy1[i] = resource[i]);
        copy1[3] = "copy1";
        System.out.println("copy1长度：" + copy1.length);
        //用Arrays.toString打印
        System.out.println(Arrays.toString(copy1));

        //2 用Arrays.copyOf扩容/复制
        String[] copy2 = Arrays.copyOf(resource , len + (len >> 1));
        copy2[3] = "copy2";
        System.out.println("copy2长度：" + copy2.length);
        //用Arrays变成list打印
        System.out.println(Arrays.asList(copy2));

        //3 用System.arraycopy 复制
        String[] copy3 = new String [len + (len >> 1)];
        //参数：原数组 原数组复制起点  目标数组 目标数组复制起点 复制长度
        System.arraycopy(resource,0,copy3,0,resource.length);
        copy3[3] = "copy3";
        System.out.println("copy3长度：" + copy3.length);
        System.out.println(Arrays.toString(copy3));

        //4 用guava.Lists扩容 直接在数组头添加1-2个元素
         List<String> list = Lists.asList("copy4","q", resource);
         //list变数组
        String[] copy4 = list.toArray(new String[]{});
        System.out.println("copy4长度：" + copy4.length);
        System.out.println(Arrays.toString(copy4));

       //5 用lang3.ArrayUtils 扩容/复制
        final String[] copy5 = ArrayUtils.add(resource, "copy5");
        System.out.println("copy5长度：" + copy5.length);
        System.out.println(Arrays.toString(copy5));

        //6 hutool 合并
        final String[] copy6 = ArrayUtil.addAll(resource, resource);
        System.out.println("copy6长度：" + copy6.length);
        System.out.println(Arrays.toString(copy6));

        //7 hutool扩容
        final String[] copy7 = ArrayUtil.resize(resource, (len >> 1) + len);
        copy7[3] = "copy7";
        System.out.println("copy7长度：" + copy7.length);
        System.out.println(Arrays.toString(copy7));
    }
}
