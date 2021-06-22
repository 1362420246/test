package com.qbk.file.tempfile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 临时文件
 */
public class TempFile {
    public static void main(String[] args) throws IOException, InterruptedException {
        //一、使用File创建临时文件
        File temp = File.createTempFile("myTempFile", ".txt");

        //使用java.io.BufferedWriter写入临时文件
        BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
        bw.write("This is the temporary data written to temp file");
        bw.close();

        System.out.println("Temp file created : " + temp.getAbsolutePath());
        //C:\Users\DXYT\AppData\Local\Temp\myTempFile5630569801985106936.txt


        //二、使用NIO创建临时文件
        Path path = Files.createTempFile("myTempFile", ".txt");

        byte[] buf = "some data".getBytes();
        Files.write(path, buf);

        System.out.println("Temp file : " + path);
        //C:\Users\DXYT\AppData\Local\Temp\myTempFile3101409993823021712.txt


        Thread.sleep(10000);
        //三、当在应用exit时（jvm终止）删除文件，你可以使用:
        temp.deleteOnExit();

        Thread.sleep(3000);
        //如果你想要立马删除文件，你可以直接使用delete()方法
        boolean delete = temp.delete();
        System.out.println(delete);

        Thread.sleep(3000);
        //四、使用NIO删除临时文件
        boolean b = Files.deleteIfExists(path);
        System.out.println(b);
        //立即删除文件 java.nio.file.NoSuchFileException
        Files.delete(path);
    }
}
