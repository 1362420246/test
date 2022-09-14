package com.qbk.niodemo.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * aio client
 */
public class AIOClient {

    private final AsynchronousSocketChannel client;

     public AIOClient() throws IOException {
        client = AsynchronousSocketChannel.open();
    }

    public static void main(String[] args) throws Exception {
        new AIOClient().connect("localhost",8080);
    }

    public void connect(String host, int port) throws Exception {
        // 客户端向服务端发起连接
        client.connect(new InetSocketAddress(host, port), null, new CompletionHandler<Void, Object>() {
            @Override
            public void completed(Void result, Object attachment) {
                try {
                    Future<Integer> write = client.write(ByteBuffer.wrap("这是一条测试数据".getBytes()));
                    write.get();
                    System.out.println("已发送到服务端");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                final ByteBuffer buffer = ByteBuffer.allocate(1024);

                // 客户端接收服务端的数据，获取的数据写入到bb中
                client.read(buffer, null, new CompletionHandler<Integer, Object>() {
                    @Override
                    public void completed(Integer result, Object attachment) {
                        // 服务端返回数据的长度result
                        System.out.println("I/O操作完成：" + result);
                        System.out.println("获取反馈结果：" + new String(buffer.array(),0,result));
                    }
                    @Override
                    public void failed(Throwable exc, Object attachment) {
                        exc.printStackTrace();
                    }
                });
            }
            @Override
            public void failed(Throwable exc, Object attachment) {
                exc.printStackTrace();
            }
        });

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        client.close();
    }
}
