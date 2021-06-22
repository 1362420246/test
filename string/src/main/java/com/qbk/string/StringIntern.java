package com.qbk.string;

/**
 * str intern方法
 */
public class StringIntern {
    public static void main(String[] args) {
        /*
        String.intern()使用原理
        String.intern()是一个Native方法，底层调用C++的 StringTable::intern方法实现。
        当通过语句str.intern()调用intern()方法后，JVM 就会在当前类的常量池中查找是否存在与str等值的String，
        若存在则直接返回常量池中相应Strnig的引用；若不存在，则会在常量池中创建一个等值的String，
        然后返回这个String在常量池中的引用。因此，只要是等值的String对象，
        使用intern()方法返回的都是常量池中同一个String引用，所以，这些等值的String对象通过intern()后使用==是可以匹配的

         Jdk6中常量池位于PermGen区  Jdk7、8中，常量池由PermGen区移到了堆区
         */
        String str = new String("abc");
        String str2 = "abc";
        System.out.println( str == str2);
        System.out.println( str.intern() == str2);
    }
}
