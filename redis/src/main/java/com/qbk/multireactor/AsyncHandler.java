package com.qbk.multireactor;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * Handlers ：执行非阻塞读/写
 **/
public class AsyncHandler implements Runnable{

    private SocketChannel channel;
    private SelectionKey sk;

    StringBuilder stringBuilder = new StringBuilder();

    ByteBuffer inputBuffer = ByteBuffer.allocate(1024);
    ByteBuffer outputBuffer = ByteBuffer.allocate(1024);

    public AsyncHandler(SocketChannel channel) {
        this.channel = channel;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public SelectionKey getSk() {
        return sk;
    }

    public void setSk(SelectionKey sk) {
        this.sk = sk;
    }

    @Override
    public void run() {
        try {
            if (sk.isReadable()) {
                read();
            } else if (sk.isWritable()) {
                write();
            }
        }catch (EOFException e){
            System.out.println("退出");
            try {
                channel.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }catch (Exception e){
             e.printStackTrace();
        }
    }
    private void read() throws IOException {
        inputBuffer.clear();
        int n = channel.read(inputBuffer);
        if(inputBufferComplete(n)){
            System.out.println(Thread.currentThread().getName()+": Server端收到客户端的请求消息："+stringBuilder.toString());
            outputBuffer.put(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
            this.sk.interestOps(SelectionKey.OP_WRITE);
        }
    }
    private boolean inputBufferComplete(int bytes) throws EOFException {
        if(bytes > 0){
            inputBuffer.flip();
            while(inputBuffer.hasRemaining()){
                //得到输入的字符
                byte ch = inputBuffer.get();
                //表示Ctrl+c
                if(ch == 3) {
                    throw new EOFException();
                }else if(ch=='\r'||ch=='\n'){
                    return true;
                }else {
                    stringBuilder.append((char)ch);
                }
            }
        }else if(bytes==1){
            throw new EOFException();
        }
        return false;
    }

    private void write() throws IOException {
        int write = -1;
        outputBuffer.flip();
        if(outputBuffer.hasRemaining()){
            //把收到的数据写回到客户端
            write = channel.write(outputBuffer);
        }
        outputBuffer.clear();
        stringBuilder.delete(0,stringBuilder.length());
        if(write<=0){
            this.sk.channel().close();
        }else{
            channel.write(ByteBuffer.wrap("\r\nreactor> ".getBytes()));
            //又转化为读事件
            this.sk.interestOps(SelectionKey.OP_READ);
        }
    }
}
