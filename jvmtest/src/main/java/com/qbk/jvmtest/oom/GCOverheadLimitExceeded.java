package com.qbk.jvmtest.oom;

import java.util.Map;
import java.util.Random;

/**
 * java.lang.OutOfMemoryError: GC overhead limit exceeded
 * gc频繁
 * jvm参数：-Xmx100m -XX:+UseParallelGC
 *
 -Xmx10m
 -XX:+UseParallelGC
 -XX:+PrintGCDetails
 -XX:+PrintGCDateStamps
 -XX:+PrintHeapAtGC
 -Xloggc:C:/Users/qbk/Desktop/gc.log
 -XX:+HeapDumpOnOutOfMemoryError
 -XX:HeapDumpPath=C:/Users/qbk/Desktop/heap.hprof
 */
public class GCOverheadLimitExceeded {

    public static void main(String args[]) throws Exception {
        Map<Object,Object> map = System.getProperties();
        Random r = new Random();
        while (true) {
            map.put(r.nextInt(), "value");
        }
    }

}
