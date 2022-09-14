package com.qbk.niodemo.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * aio server
 */
public class AIOServer {
    public static void main(String[] args) throws Exception {
        // 创建一个SocketChannel
        final AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open();
        //绑定了8080端口
        serverChannel.bind(new InetSocketAddress(8080));
        /**
         * 接受连接。
         * 该方法启动一个异步操作来接受对此通道套接字的连接。该handler参数是一个完成处理程序，当连接被接受时（或操作失败）被调用。传递给完成处理程序的结果是AsynchronousSocketChannel新连接。
         *
         * 参数：
         * attachment - 要附加到I / O操作的对象; 可null
         * handler - 消耗结果的处理程序
         */
        serverChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            @Override
            public void completed(AsynchronousSocketChannel socketChannel, Object attachment) {
                try {
                    // 打印线程的名字
                    System.out.println("2--" + Thread.currentThread().getName());
                    System.out.println(socketChannel.getRemoteAddress());

                    ByteBuffer buffer = ByteBuffer.allocate(1024);

                    // socketChannel异步的读取数据到buffer中
                    socketChannel.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer buffer) {
                            // 打印线程的名字
                            System.out.println("3--" + Thread.currentThread().getName());
                            buffer.flip();
                            Charset charset = StandardCharsets.UTF_8;
                            CharBuffer charBuffer = charset.decode(buffer);
                            System.out.println("服务端接受："+ charBuffer);
                            socketChannel.write(ByteBuffer.wrap("HelloClient".getBytes()));
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer buffer) {
                            exc.printStackTrace();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                exc.printStackTrace();
            }
        });
        System.out.println("1--" + Thread.currentThread().getName());
        Thread.sleep(Integer.MAX_VALUE);
    }
}
