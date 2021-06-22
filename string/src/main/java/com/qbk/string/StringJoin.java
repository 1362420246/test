package com.qbk.string;

import java.util.StringJoiner;

/**
 * str 六种拼接
 */
public class StringJoin {
    public static void main(String[] args) {
        String s1 = "布宜诺斯艾利斯";
        String s2 = "博卡";

        //第一种 +
        String strJoin = s1 +  s2 ;
        //编译器会把 + 号转换成 (new StringBuilder(String.valueOf(s1))).append(s2).toString()
        System.out.println(strJoin);

        //第二种 concat
        String strJoin2 = s1.concat(s2) ;
        //底层使用 System.arraycopy char数组复制
        System.out.println(strJoin2);

        //第三种 join 参数1：分隔每个元素的分隔符
        String strJoin3 = String.join("-",s1,s2);
        //底层使用 StringJoiner
        System.out.println(strJoin3);

        //第四种 StringJoiner  jdk1.8  构造方法参数：分隔符
        //用于构造由分隔符分隔的字符序列，并可选择性地从提供的前缀开始和以提供的后缀结尾
        StringJoiner stringJoiner = new StringJoiner("+");
        stringJoiner.add(s1);
        stringJoiner.add(s2);
        System.out.println(stringJoiner.toString());

        //第五种 StringBuilder  jdk1.5
        StringBuilder sbr = new StringBuilder(s1);
        //线程非安全
        sbr.append(s2);
        String strJoin4 = sbr.toString();
        System.out.println(strJoin4);

        //第六种 StringBuffer
        StringBuffer sb = new StringBuffer(s1);
        //使用synchronized  线程安全
        sb.append(s2);
        String strJoin5 = sb.toString();
        System.out.println(strJoin5);

        //测试时间
        long start1 = System.nanoTime();
        String result = "";
        // for 循环中创建了大量的 StringBuilder 对象 所以慢
        for (int i = 0; i < 100000; i++) {
            result += "六六六";
        }
        long end1 = System.nanoTime();

        long start2 = System.nanoTime();
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < 100000; i++) {
            sb2.append("六六六");
        }
        long end2 = System.nanoTime();

        long start3 = System.nanoTime();
        StringBuffer sb3 = new StringBuffer();
        for (int i = 0; i < 100000; i++) {
            sb3.append("六六六");
        }
        long end3 = System.nanoTime();

        System.out.println(end1 - start1);//16716566482
        System.out.println(end2 - start2);//1235384
        System.out.println(end3 - start3);//1266731
    }
}
