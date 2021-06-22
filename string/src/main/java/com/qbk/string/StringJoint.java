package com.qbk.string;

/**
 * str拼接
 */
public class StringJoint {
    public static void main(String[] args) {
        String s1 = "a";
        String s2 = s1 + "b";
        String s3 = "a" + "b";
        String s4 = s1.concat("b");
        System.out.println(s2 == "ab");//false
        System.out.println(s3 == "ab");//true
        System.out.println(s2 == s3);//false
        System.out.println(s4 == "ab");//false
        System.out.println(s4 == s2);//false
        System.out.println(s4 == s3);//false
        //返回给定对象的哈希码，该代码与默认的方法 hashCode() 返回的代码一样，无论给定对象的类是否重写 hashCode()。null 引用的哈希码为 0。
        System.out.println(System.identityHashCode("ab"));//1562557367
        System.out.println(System.identityHashCode(s2));//1101288798
        System.out.println(System.identityHashCode(s3));//1562557367
        System.out.println(System.identityHashCode(s4));//942731712
        System.out.println(s2.hashCode());//3105
        System.out.println(s3.hashCode());//3105
        System.out.println(s4.hashCode());//3105

        Object object = new Object();
        System.out.println(object);
        System.out.println(object.toString());
        System.out.println(object.hashCode());
        System.out.println(System.identityHashCode(object));
    }
}
