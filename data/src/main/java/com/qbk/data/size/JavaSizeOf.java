package com.qbk.data.size;

import com.carrotsearch.sizeof.RamUsageEstimator;

/**
 * 计算java对象内存大小
 *
 * -XX:-UseCompressedClassPointers -XX:-UseCompressedOops
 */
public class JavaSizeOf {

    /**
     * -XX:+UseCompressedOops: mark/8 + klass/4 + padding/4 = 16
     * -XX:-UseCompressedOops: mark/8 + klass/8 = 16
     */
    static class A {

    }

    /**
     * -XX:+UseCompressedOops: mark/8 + klass/4 + 4 = 16
     * -XX:-UseCompressedOops: mark/8 + klass/8 + 4 + padding/4 = 24
     */
    static class B {
        int b;
    }

    /**
     * -XX:+UseCompressedOops: mark/8 + klass/4 + 4 + 4 + padding/4 = 24
     * -XX:-UseCompressedOops: mark/8 + klass/8 + 4 + 4 = 24
     */
    static class C {
        int c1;
        int c2;
    }

    /**
     * -XX:+UseCompressedOops: mark/8 + klass/4 + 4 + 4 + padding/4 = 24
     * -XX:-UseCompressedOops: mark/8 + klass/8 + 8 + 4 + padding/4 = 32
     */
    static class D {
        int d1;
        Integer d2;
    }

    /**
     * -XX:+UseCompressedOops: mark/8 + klass/4 + 4  = 16
     * -XX:-UseCompressedOops: mark/8 + klass/8 + 8  = 24
     */
    static class E {
        Integer e;
    }

    /**
     * -XX:+UseCompressedOops: mark/8 + klass/4 + 4  = 16
     * -XX:-UseCompressedOops: mark/8 + klass/8 + 8  = 24
     */
    static class F {
        Long f;
    }

    /**
     * -XX:+UseCompressedOops: mark/8 + klass/4 + 4  = 16
     * -XX:-UseCompressedOops: mark/8 + klass/8 + 8  = 24
     */
    static class G {
        String g;
    }

    public static void main(String[] args) {
        System.out.println(RamUsageEstimator.sizeOf(1)); // 16 / 24
        System.out.println(RamUsageEstimator.sizeOf(new Integer(2))); // 16 / 24
        System.out.println(RamUsageEstimator.sizeOf("s")); // 48 / 64
        System.out.println(RamUsageEstimator.sizeOf(new String("z"))); // 48 / 64
        int[] arr = {};
        int[] arr2 = {1};
        int[] arr3 = {1,2};
        D[] arr4 = {new D()};
        D[] arr5 = {new D(),new D()};
        System.out.println(RamUsageEstimator.sizeOf(arr)); // 16 / 24
        System.out.println(RamUsageEstimator.sizeOf(arr2)); // 24 / 32
        System.out.println(RamUsageEstimator.sizeOf(arr3)); // 24 / 32
        System.out.println(RamUsageEstimator.sizeOf(arr4)); // 48 / 64
        System.out.println(RamUsageEstimator.sizeOf(arr5)); // 72 / 104

        System.out.println("A:" + RamUsageEstimator.sizeOf(new A()));
        System.out.println("B:" + RamUsageEstimator.sizeOf(new B()));
        System.out.println("C:" + RamUsageEstimator.sizeOf(new C()));
        System.out.println("D:" + RamUsageEstimator.sizeOf(new D()));
        System.out.println("E:" + RamUsageEstimator.sizeOf(new E()));
        System.out.println("F:" + RamUsageEstimator.sizeOf(new F()));
        System.out.println("G:" + RamUsageEstimator.sizeOf(new G()));
    }
}
