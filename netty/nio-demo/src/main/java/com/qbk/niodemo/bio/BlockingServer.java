package com.qbk.niodemo.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * bio server
 */
public class BlockingServer {
    public static void main(String[] args) throws IOException {
        // 第一步，我们首先通过ServerSocket来监听端口，我们知道，每个进程都有一个唯一的端口
        ServerSocket serverSocket = new ServerSocket(8080);

        // 然后通过accept方法阻塞调用，直到有客户端的连接过来，就会返回Socket
        Socket socket = serverSocket.accept();

        // 然后获取Socket的输入流
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // 获取客户端的数据，这个地方是一个阻塞的IO,阻塞到直到数据读取完成
        String cliStr = bufferedReader.readLine();
        System.out.println("收到客户端数据：" + cliStr);

        // 获取Socekt的输出流
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        // 给客户端回写数据
        bufferedWriter.write("ok\n");

        // 最后刷新
        bufferedWriter.flush();
    }
}
