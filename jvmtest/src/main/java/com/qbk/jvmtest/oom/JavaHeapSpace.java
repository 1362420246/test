package com.qbk.jvmtest.oom;

import java.util.ArrayList;
import java.util.List;

/**
 * java.lang.OutOfMemoryError: Java heap space
 * 堆内存溢出
 * jvm参数：-Xms10m -Xmx10m -XX:+HeapDumpOnOutOfMemoryError
 */
public class JavaHeapSpace {

    public static void main (String[] args){
        List<Integer> list = new ArrayList<>();
        int i=0;
        while(true){
            list.add(i++);
        }
    }
}
