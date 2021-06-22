package com.qbk.jvmtest.oom;

/**
 * java.lang.StackOverflowError
 * 栈溢出
 */
public class StackOverflowError {

    public static void main (String [] srgs) {
        stackOutOfMemoryError(1);
    }
    private static void stackOutOfMemoryError(int depth){
        depth++;
        stackOutOfMemoryError(depth);
    }
}
