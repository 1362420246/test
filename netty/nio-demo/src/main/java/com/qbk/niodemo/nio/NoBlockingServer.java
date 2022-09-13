package com.qbk.niodemo.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * nio server
 */
public class NoBlockingServer {

    private static List<SocketChannel> clients = new ArrayList<>();

    public static void main(String[] args) {
        try {
            // 得到一个serverSocketChannel管道，这个就等同于ServerSocket，只不过这个是支持异步并且可同时读写
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

            //我们想要socket为非阻塞，通过设置该值为false就是为非阻塞
            serverSocketChannel.configureBlocking(false);

            // 绑定端口
            serverSocketChannel.socket().bind(new InetSocketAddress(8080));

            while (true) {
                // 然后接收客户端的请求，调用accept,由于设置成非阻塞了，所以accept将不会阻塞在这里等待客户端的连接过来
                SocketChannel socketChannel = serverSocketChannel.accept();
                // 那么不阻塞之后，得到的这个SocketChannel就有可能是null的连接，所以判断是否为空
                if (socketChannel != null) {
                    //同时也设置socketChannel为非阻塞，因为原来我们读取数据read方法也是阻塞的
                    socketChannel.configureBlocking(false);
                    clients.add(socketChannel);
                } else {
                    Thread.sleep(3000);
                    System.out.println("没有连接，请等待~~~");
                }

                for (SocketChannel client : clients) {
                    // channel中的数据都是先读取到buffer中，也都先写入到buffer中，所以定义一个 ByteBuffer
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    // 数据读取到缓冲区,由于上面设置了非阻塞，此时的read将不会阻塞
                    int num = client.read(byteBuffer);
                    if (num > 0) {
                        System.out.println(
                                "客户端端口:" + client.socket().getPort() +
                                        ",收到客户端数据：" + new String(byteBuffer.array()));
                    } else {
                        System.out.println("等待客户端写数据！！！");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
