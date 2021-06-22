package com.qbk.data.serializer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ServerSocket
 */
public class ServerSocketDemo {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(8080);
        final Socket socket = serverSocket.accept();
        ObjectInputStream inputStream =
                new ObjectInputStream(socket.getInputStream());
        User user = (User) inputStream.readObject();
        System.out.println(user);
        inputStream.close();
        socket.close();
    }
}
