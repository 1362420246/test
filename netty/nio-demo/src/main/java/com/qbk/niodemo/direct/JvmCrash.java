package com.qbk.niodemo.direct;

import sun.nio.ch.DirectBuffer;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * jvm crash 测试
 * MappedByteBuffer释放后再对它进行读操作的话就会引发这个crash，在并发情况下很容易正在释放时另一个线程正开始读取，于是crash就发生了
 */
public class JvmCrash {

    public static void main(String[] args) throws Exception {
        unmapAndRead();
    }

    private static void unmapAndRead() throws Exception {
        File file = new File("D://data.txt");
        FileChannel fileChannel = new RandomAccessFile(file, "rw").getChannel();
        MappedByteBuffer buf = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 4096);
        buf.putInt(5);
        buf.force();
        JvmCrash.unmap(buf);
        buf.get(2);//Crash occur
    }
    /**
     * 释放
     */
    private static void unmap2(MappedByteBuffer mappedByteBuffer) {
        ((DirectBuffer) mappedByteBuffer).cleaner().clean();
    }

    /**
     * 释放
     */
    private static void unmap(MappedByteBuffer mappedByteBuffer) {
        try {
            if (mappedByteBuffer == null) {
                return;
            }
            mappedByteBuffer.force();
            AccessController.doPrivileged(new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    try {
                        Method getCleanerMethod = mappedByteBuffer.getClass().getMethod("cleaner", new Class[0]);
                        getCleanerMethod.setAccessible(true);
                        sun.misc.Cleaner cleaner =
                                (sun.misc.Cleaner) getCleanerMethod.invoke(mappedByteBuffer, new Object[0]);
                        cleaner.clean();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}