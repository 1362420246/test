package com.qbk.file.path;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Path接口是java NIO2的一部分。首次在java 7中引入。Path接口在java.nio.file包下，所以全称是java.nio.file.Path。
 * java中的Path表示文件系统的路径。可以指向文件或文件夹。也有相对路径和绝对路径之分。
 */
public class PathDemo {
    public static void main(String[] args) throws IOException {
        //创建path
        Path path = Paths.get("C:\\Users\\DXYT\\AppData\\Local\\Temp\\myTempFile7782543697215050434.txt");

        //创建相对路径 当前路径
        Path currentDir = Paths.get(".");
        System.out.println(currentDir.toAbsolutePath());

        //标准化路径。标准化的含义是路径中的.和..都被去掉，指向真正的路径目录地址
        System.out.println(currentDir.normalize());

        //检查文件系统中是否存在某路径
        //LinkOption.NOFOLLOW_LINKS枚举类型，表示Files.exists不会跟进到路径中有连接的下层文件目录。表示path路径中如果有连接，Files.exists方法不会跟进到连接中去
        boolean pathExists = Files.exists(path, LinkOption.NOFOLLOW_LINKS);
        System.out.println(pathExists);

        //拷贝 & 覆盖
        Path target = Paths.get("C:\\Users\\DXYT\\AppData\\Local\\Temp\\myTempFile111111.txt");
        Files.copy(path,target,StandardCopyOption.REPLACE_EXISTING);

        //删除
//        Files.delete(target);

        //写入
        byte[] buf = "some data111".getBytes();
        Files.write(target, buf);

        //创建文件夹
        Path newDir;
        try {
            newDir = Files.createDirectory( Paths.get("D:\\data\\tmp"));
        } catch(FileAlreadyExistsException e){
            // 目录已经存在
//            e.printStackTrace();
            newDir =  Paths.get("D:\\data\\tmp");
        }

        //移动
        Files.move(target, Paths.get("D:\\data\\tmp\\ttt.txt"),StandardCopyOption.REPLACE_EXISTING);

        /*
        遍历
        Files.walkFileTree()方法包含递归遍历目录树的功能。walkFileTree()方法将Path实例和FileVisitor作为参数。Path实例指向您想要遍历的目录。FileVisitor在遍历期间被调用。

        FileVisitor实现中的每个方法在遍历过程中的不同时间都被调用:
        在访问任何目录之前调用preVisitDirectory()方法。在访问一个目录之后调用postVisitDirectory()方法。
        调用visitFile()在文件遍历过程中访问的每一个文件。它不会访问目录-只会访问文件。在访问文件失败时调用visitFileFailed()方法。例如，如果您没有正确的权限，或者其他什么地方出错了。

        这四个方法中的每个都返回一个FileVisitResult枚举实例。FileVisitResult枚举包含以下四个选项:
        CONTINUE 继续
        TERMINATE 终止
        SKIP_SIBLING 跳过同级
        SKIP_SUBTREE 跳过子级
        通过返回其中一个值，调用方法可以决定如何继续执行文件。

        CONTINUE继续意味着文件的执行应该像正常一样继续。

        TERMINATE终止意味着文件遍历现在应该终止。

        SKIP_SIBLINGS跳过同级意味着文件遍历应该继续，但不需要访问该文件或目录的任何同级。

        SKIP_SUBTREE跳过子级意味着文件遍历应该继续，但是不需要访问这个目录中的子目录。这个值只有从preVisitDirectory()返回时才是一个函数。如果从任何其他方法返回，它将被解释为一个CONTINUE继续。
         */
        Files.walkFileTree(newDir, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println("pre visit dir:" + dir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println("visit file: " + file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                System.out.println("visit file failed: " + file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                System.out.println("post visit directory: " + dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
