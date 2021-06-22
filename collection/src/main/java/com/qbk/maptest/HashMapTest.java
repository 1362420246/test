package com.qbk.maptest;

import java.util.HashMap;

/**
 * HashMap
 */
public class HashMapTest {

    public static void main(String[] args) {
        HashMap<String,String> map = new HashMap<>();
        map.put("a","a");
        map.put("q","q");
        map.put("a1","a");
        map.put("a2","a");
        map.put("a3","a");
        map.put("a4","a");
        map.put("a5","a");
        map.put("a6","a");
        map.put("a7","a");
        map.put("a8","a");
        map.put("a9","a");
        map.put("a10","a");
        map.put("a11","a");

        //扩容后的位置
        // newTab[e.hash & (newCap - 1)] = e;
        int initCap = 1 << 4;
        int cap1 = initCap << 1;
        int cap2 = cap1 << 1;
        System.out.println( hash("a") +  ":" + Integer.toBinaryString(hash("a")));
        System.out.println( hash("a") & (initCap -1)); //1100001 & 1111 = 1
        System.out.println( hash("a") & (cap1 -1)); //1100001 & 11111 = 1
        System.out.println( hash("a") & (cap2 -1)); //1100001 & 111111 = 100001

        System.out.println( hash("q")+  ":" + Integer.toBinaryString(hash("q")));
        System.out.println( hash("q") & (initCap -1)); //1110001 & 1111 = 1
        System.out.println( hash("q") & (cap1 -1)); //1110001 & 11111 = 10001
        System.out.println( hash("q") & (cap2 -1));//1110001 & 111111 = 110001

        // newTab[j] = loHead;
        // newTab[j + oldCap] = hiHead;
        System.out.println(97 & 16);// 1100001 & 10000 = 0
        System.out.println(113 & 16);// 1110001 & 10000 = 16
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
