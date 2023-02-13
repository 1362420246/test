package com.qbk.jvmtest.oom;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 內存泄漏（Memory Leak）
 * <p>
 * 这个代码将创建 1000 个线程，每个线程都使用 ThreadLocal 绑定了一个大对象。
 * 由于没有对 ThreadLocal 调用 remove 方法，导致内存泄漏。
 * 随着线程数量的增加和循环次数的增加，内存消耗量会不断增加，并最终导致内存溢出。
 *
 *  1. 设置 -Xmx200m -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError
 *  2. 执行 jstat -gc pid
 *  3. 执行 jmap -dump:format=b,file=a1.hprof pid
 *  4. 使用 JProfiler打开hprof文件,查看堆遍历器(Heap walker)-> 堆遍历器图
 *
 *  说明：
 *  +PrintGCDetails ：打印gc日志
 *  +HeapDumpOnOutOfMemoryError ：会在OOM时自动生成dump文件
 *  jmap -dump:format=b,file=a1.hprof pid  ：手动生成dump文件，文件名是a1.hprof
 *
 *  第一次 fullgc 日志：
 *  [Full GC (System.gc())
 *  [PSYoungGen: 1630K->0K(59904K)]
 *  [ParOldGen: 8K->1566K(136704K)] 1638K->1566K(196608K),
 *  [Metaspace: 4304K->4304K(1056768K)], 0.0116857 secs]
 *  [Times: user=0.03 sys=0.02, real=0.01 secs]
 *
 *  第n次 fullgc 日志：
 *  [Full GC (System.gc())
 *  [PSYoungGen: 46163K->43061K(59904K)]
 *  [ParOldGen: 136663K->136663K(136704K)] 182827K->179725K(196608K),
 *  [Metaspace: 4329K->4329K(1056768K)], 0.3294192 secs]
 *  [Times: user=1.02 sys=0.01, real=0.33 secs]
 *
 *  oom日志 :
 *  Exception in thread "pool-1-thread-396" java.lang.OutOfMemoryError: GC overhead limit exceeded
 *
 *  [Full GC (Ergonomics) [PSYoungGen: 51712K->51701K(59904K)]
 *  [ParOldGen: 136672K->136672K(136704K)] 188384K->188373K(196608K),
 *  [Metaspace: 4371K->4371K(1056768K)], 0.3313296 secs]
 *  [Times: user=1.30 sys=0.00, real=0.33 secs]
 *
 */
public class MemoryLeakTest {

    static final int THREAD_COUNT = 1000;

    static ThreadLocal<MyKKObject> threadLocal = new ThreadLocal<>();

    static ExecutorService executors = Executors.newFixedThreadPool(THREAD_COUNT);

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < THREAD_COUNT; i++) {
            int finalI = i;
            executors.execute(
                    () -> {
                        MyKKObject data = new MyKKObject();
                        data.setId(finalI);
                        threadLocal.set(data);
                        // 由于 data 引用被绑定到了 ThreadLocal，导致内存泄漏
                        // 需要调用 threadLocal.remove() 方法来避免内存泄漏

                        //threadLocal.remove();
                    }
            );
            TimeUnit.SECONDS.sleep(1);
            if( i % 20 == 0){
                System.out.println("触发Full GC");
                System.gc();
            }
        }
        TimeUnit.HOURS.sleep(1);
    }
}
@Data
class MyKKObject{
    private Integer id;
    private List<Integer> arr = new ArrayList<>();
    {
        for (int i = 0; i < 100000; i++) {
            arr.add(i);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.print(this + " ,被回收!  ");
        super.finalize();
    }

    @Override
    public String toString() {
        return "MyKKObject{" +
                "id=" + id +
                '}';
    }
}