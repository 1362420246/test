package com.qbk.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Handlers ：执行非阻塞读/写
 **/
public class Handler implements Runnable{
    SocketChannel channe;

    Handler(SocketChannel channe) {
        this.channe = channe;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+"------Handler");
        ByteBuffer buffer = ByteBuffer.allocate(1024);
//        try {
//            Thread.sleep(1000000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        int len,total =0;
        StringBuilder msg = new StringBuilder();
        try {
            do {
                len = channe.read(buffer);
                if(len>0){
                    total+=len;
                    msg.append(new String(buffer.array()));
                }
            } while (len > buffer.capacity());
            System.out.println("total:"+total);
            System.out.println(channe.getRemoteAddress()+": Server receive Msg:"+msg);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(channe!=null){
                try {
                    channe.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
