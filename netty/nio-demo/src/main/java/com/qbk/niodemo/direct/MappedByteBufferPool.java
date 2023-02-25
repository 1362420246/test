package com.qbk.niodemo.direct;

import sun.nio.ch.DirectBuffer;

import java.nio.MappedByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * MappedByteBufferPool是一个对象池，用于管理MappedByteBuffer对象。
 *
 * 它的构造函数中指定了对象池的容量
 *
 * acquire方法用于获取一个可用的MappedByteBuffer对象，
 * 如果对象池中已经有可用的对象，则直接从对象池中获取。
 * 否则，就创建一个新的MappedByteBuffer对象。
 *
 * release方法用于将MappedByteBuffer对象返回给对象池，
 * 如果对象池未满，则将MappedByteBuffer对象放入对象池中。
 * 否则，直接释放MappedByteBuffer对象。
 */
public class MappedByteBufferPool {

    private final BlockingQueue<MappedByteBuffer> pool;

    private final int capacity;

    public MappedByteBufferPool(int capacity) {
        this.pool = new ArrayBlockingQueue<>(capacity);
        this.capacity = capacity;
    }

    public MappedByteBuffer acquire(int size) {
        MappedByteBuffer buffer = pool.poll();
        if (buffer == null || buffer.capacity() < size) {
            // 创建一个新的MappedByteBuffer
            buffer = createBuffer(size);
        }
        return buffer;
    }

    public void release(MappedByteBuffer buffer) {
        if (pool.size() < capacity) {
            pool.offer(buffer);
        } else {
            // 如果对象池已满，则直接释放MappedByteBuffer
            unmap(buffer);
        }
    }

    private MappedByteBuffer createBuffer(int size) {
        // 创建一个新的MappedByteBuffer
        // ...
        return null;
    }

    private void unmap(MappedByteBuffer buffer) {
        // 释放MappedByteBuffer
        ((DirectBuffer) buffer).cleaner().clean();
    }
}
