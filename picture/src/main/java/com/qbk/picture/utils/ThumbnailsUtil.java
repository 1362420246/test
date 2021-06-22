package com.qbk.picture.utils;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

/**
 * Thumbnails 工具类：
 * 按比例缩放
 * 缩略图
 * 裁剪
 * 缩略图写进输出流里
 * 缩略图写进缓存里
 * 添加水印
 * 旋转
 */
public class ThumbnailsUtil {
    /**
     * 按比例缩放
     * @param source 原图文件的路径
     * @param scale 缩放比例，值在0到1之间，1f就是原图大小，0.5就是原图的一半大小，这里的大小是指图片的长宽
     * @param outputQuality 是图片的质量，值也是在0到1，越接近于1质量越好，越接近于0质量越差
     * @param toFile 压缩后文件的路径
     */
    public static void compress(String source ,double scale ,double outputQuality ,String toFile) throws IOException {
        Thumbnails.of(source)
                .scale(scale)
                .outputQuality(outputQuality)
                .toFile(toFile);
    }
    /**
     * 缩略图
     * @param source 原图文件的路径
     * @param width 宽
     * @param height 高
     * @param toFile 压缩后文件的路径
     */
    public static void zoomOut(String source ,int width, int height ,String toFile) throws IOException {
        Thumbnails.of(source)
                .size(width, height)
                .toFile(toFile);
    }
    /**
     * 裁剪
     * @param source 原图文件的路径
     * @param width 宽
     * @param height 高
     * @param toFile 压缩后文件的路径
     */
    public static void cropping(String source,int width,int height,String toFile)throws IOException{
        Thumbnails.of(source)
                .sourceRegion(Positions.CENTER, width, height)
                .size(width, height)
                .keepAspectRatio(false)
                .toFile(toFile);
    }
    /**
     * 缩略图写进输出流里
     * @param source 原图文件的路径
     * @param width 宽
     * @param height 高
     * @param os 输出流
     */
    public static void zoomOutputStream(String source , int width, int height , OutputStream os) throws IOException {
        Thumbnails.of(source)
                .size(width, height)
                .outputFormat("png")
                .toOutputStream(os);
    }
    /**
     * 缩略图写进缓存里
     * @param source 原图文件的路径
     * @param width 宽
     * @param height 高
     */
    public static BufferedImage getBufferedImage(String source , int width, int height ) throws IOException {
        return Thumbnails.of(source).size(width, height).asBufferedImage();
    }
    /***
     * 添加水印
     * @param source 原图文件的路径
     * @param width 宽
     * @param height 高
     * @param watermark 水印路径
     * @param toFile 压缩后文件的路径
     */
    public static void addWatermark(String source , int width, int height , String watermark,String toFile) throws IOException {
        //rotate 顺时针旋转90度  watermark水印位置 outputQuality图片质量
        Thumbnails.of(source)
                .size(width, height)
                .rotate(90)
                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(watermark)), 0.5f)
                .outputQuality(0.8)
                .toFile(new File(toFile));
    }
    /**
     * 测试
     */
    public static void main(String[] args) throws IOException {
        String source = "C:\\Users\\86186\\Desktop\\mm\\m1.jpg";

        //测试按比例缩放
        String destPath = "C:\\Users\\86186\\Desktop\\mm\\缩放结果.jpg";
        compress(source,0.3f,0.5f,destPath);

        //测试缩略图
        String destPath2 = "C:\\Users\\86186\\Desktop\\mm\\缩略图结果.jpg";
        zoomOut(source,130,130,destPath2);

        //测试裁剪
        String destPath3 = "C:\\Users\\86186\\Desktop\\mm\\裁剪结果.jpg";
        cropping(source,130,130,destPath3);

        //测试 缩略图写进输出流里
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        zoomOutputStream(source,130,130,byteArrayOut);
        byte[] byteArray = byteArrayOut.toByteArray();
        System.out.println(Arrays.toString(byteArray));

        //测试 缩略图写进缓存里
        String destPath4 = "C:\\Users\\86186\\Desktop\\mm\\写进缓存结果.jpg";
        BufferedImage bufferedImage = getBufferedImage(source, 130, 130);
        ImageIO.write(bufferedImage, "jpg", new File(destPath4));

        //测试添加水印
        String watermark = "C:\\Users\\86186\\Desktop\\mm\\水印.jpg";
        String destPath5 = "C:\\Users\\86186\\Desktop\\mm\\水印结果.jpg";
        //获取原图大小
        BufferedImage image = ImageIO.read(new FileInputStream(source));
        addWatermark(source,image.getWidth(),image.getHeight(),watermark,destPath5);
    }
}