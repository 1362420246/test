package com.qbk.data.serializer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Socket
 */
public class SocketDemo {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",8080);
        final OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream =
                new ObjectOutputStream(outputStream);
        final User user = new User();
        user.setId(22);
        user.setName("kk");
        user.setPassword("123");
        objectOutputStream.writeObject(user);
        objectOutputStream.close();
        socket.close();
    }
}
