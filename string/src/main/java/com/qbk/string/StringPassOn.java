package com.qbk.string;

import java.util.Arrays;

/**
 * string传递
 */
public class StringPassOn {
    private String str = new String("abc");
    private StringBuilder sbr = new StringBuilder("abc");
    private StringBuffer sb = new StringBuffer("abc");
    private String[] arr = {"a","b","c"};
    private Integer i = new Integer(666);
    public static void main(String[] args) {
        StringPassOn spo = new StringPassOn();
        spo.fun(spo.str,spo.sbr,spo.sb,spo.arr,spo.i);
        System.out.println(spo.str);
        System.out.println(spo.sbr);
        System.out.println(spo.sb);
        System.out.println(Arrays.toString(spo.arr));
        System.out.println(spo.i);
    }
    private void fun(String str,StringBuilder sbr ,StringBuffer sb ,String[] arr,Integer i){
        //str变量指向另一个对象
        str = "qbk";
        //sbr变量指向另一个对象
        sbr = new StringBuilder("abcd");
        //sb变量保持原有指向对象，并做操作
        sb.append("d");
        //arr变量保持原有指向对象，并做操作
        arr[1] = "y";
        //arr  = new String[]{"a", "y", "c"};
        //i变量指向另一个对象
        i = 233;
    }
}
