package com.qbk.picture.utils;

import org.apache.commons.io.FilenameUtils;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * 头像合成 工具类
 * 图片合成 1:钉钉效果  2:微信效果
 * 裁圆形
 * 图片缩放
 */
public final class ImageSynthesisUtil {

    public static void main(String[] args) throws IOException {
        String a = "C:\\Users\\86186\\Desktop\\kk\\a.png";
        String b = "C:\\Users\\86186\\Desktop\\kk\\b.jfif";
        String c = "C:\\Users\\86186\\Desktop\\kk\\c.jpg";
        String d = "C:\\Users\\86186\\Desktop\\kk\\d.jpg";
        String e = "C:\\Users\\86186\\Desktop\\kk\\e.jpg";
        String f = "C:\\Users\\86186\\Desktop\\kk\\f.jpg";
        String g = "C:\\Users\\86186\\Desktop\\kk\\g.jpg";
        String h = "C:\\Users\\86186\\Desktop\\kk\\h.jpg";
        String i = "C:\\Users\\86186\\Desktop\\kk\\i.jpg";
        String j = "C:\\Users\\86186\\Desktop\\kk\\j.jpg";

        List<String> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        list.add(c);
        list.add(d);
        list.add(e);
        list.add(f);
        list.add(g);
        list.add(h);
        list.add(i);
        list.add(j);

        String outPath = "C:\\Users\\86186\\Desktop\\kk\\zwx9.PNG";
        getCombinationOfhead(list,outPath,2);
    }

    /**
     * 画板边长
     */
    public static int square = 212;

    /**
     * 间隔
     */
    public static int interval = 4;

    /**
     * 背景颜色
     */
    public static Color background = new Color(231,231,231);

    /**
     * 生成组合头像
     * @param paths 用户图像
     * @param outPath 输出地址 只能是png格式，不然有黑框
     * @param wxOrDd 你说你想要微信效果还是钉钉效果 1:钉钉效果  2:微信效果
     */
    public static void getCombinationOfhead(List<String> paths ,String outPath,int wxOrDd) throws IOException {
        int grid ;
        if(wxOrDd == 1){
            //钉钉最多支持4宫格
            grid = 4;
        }else {
            //微信最多9宫格
            grid = 9;
        }
        //最多支持几宫格
        if(paths.size() > grid){
            paths= paths.subList(0,grid);
        }
        //缩略图的边长
        int litimg = getLitimg(square, interval, paths.size(), wxOrDd);
        List<BufferedImage> bufferedImages = new ArrayList<>();
        // 压缩图片所有的图片生成尺寸同一
        for (int i = 0; i <paths.size() ; i++) {
            String path = paths.get(i);
            if(paths.size()== 3 &&  wxOrDd == 1 && i == 0){
                //钉钉三张图的效果 图片大小不一致
                bufferedImages.add(resize(path, litimg *2, litimg *2, true));
            }else {
                bufferedImages.add(resize(path, litimg, litimg, true));
            }

        }
        // BufferedImage.TYPE_INT_ARGB + png 输出可以保证边框不黑
        BufferedImage outImage = new BufferedImage(square, square,BufferedImage.TYPE_INT_ARGB);
        // 生成画布
        Graphics g = outImage.getGraphics();
        Graphics2D g2d = (Graphics2D) g;
        // 设置背景色
        g2d.setBackground(background);
        // 通过使用当前绘图表面的背景色进行填充来清除指定的矩形。
        g2d.clearRect(0, 0, square, square);

        // 开始拼凑 根据图片的数量判断该生成那种样式的组合
        // j 代表第二层 x坐标计算参数
        int j = 0;
        // z 代表第三层 x坐标计算参数
        int z = 0;
        for (int i = 0 ; i < bufferedImages.size(); i++) {
            if (bufferedImages.size() > 4){
                if (i < 3) {
                    int y = interval ;
                    int x = i * litimg + interval * (i+1) ;
                    g2d.drawImage(bufferedImages.get(i), x, y, null);
                }else if( i < 6){
                    int y = litimg + interval * 2;
                    int x = j * litimg + interval * (j+1) ;
                    g2d.drawImage(bufferedImages.get(i), x, y  , null);
                    j++;
                }else {
                    int y = litimg * 2 + interval * 3;
                    int x = z * litimg + interval * (z+1) ;
                    g2d.drawImage(bufferedImages.get(i), x, y  , null);
                    z++;
                }
            }else if(bufferedImages.size() == 4){
                if (i < 2) {
                    int y = interval;
                    int x = i * litimg + interval * (i+1) ;
                    if(wxOrDd == 1){
                        y = 0;
                        x = (litimg + interval) * i;
                    }
                    g2d.drawImage(bufferedImages.get(i), x, y, null);
                } else {
                    int y = litimg + interval * 2;
                    int x = j * litimg + interval * (j+1) ;
                    if(wxOrDd == 1){
                        y = litimg + interval;
                        x = (litimg + interval) * j;
                    }
                    g2d.drawImage(bufferedImages.get(i), x, y  , null);
                    j++;
                }
            } else if (bufferedImages.size() == 3){
                //三张图
                if (i < 1) {
                    int y = interval;
                    int x = (square - litimg)/2;
                    if(wxOrDd == 1){
                        y = 0 ;
                        x = 0 - litimg;
                    }
                    g2d.drawImage(bufferedImages.get(i), x, y, null);
                } else {
                    int y = litimg + interval * 2;
                    int x = j * litimg + interval * (j+1) ;
                    if(wxOrDd == 1){
                        y = ( litimg + interval) * j;
                        x = litimg + interval ;
                    }
                    g2d.drawImage(bufferedImages.get(i), x , y, null);
                    j++;
                }
            } else if (bufferedImages.size() == 2){
                //两张图
                int y = (square - litimg)/2;
                int x = i * litimg + interval * (i+1) ;
                if(wxOrDd == 1){
                     y = 0 ;
                     x = 0 - litimg/2 + (litimg + interval) * i;
                }
                g2d.drawImage(bufferedImages.get(i), x ,y, null);
            } else if (bufferedImages.size() == 1){
                //一张图在中心位
                int xy = (square - litimg)/2;
                if(wxOrDd == 1){
                    xy = 0;
                }
                g2d.drawImage(bufferedImages.get(i), xy, xy, null);
            }
            // 需要改变颜色的话在这里绘上颜色。可能会用到AlphaComposite类
        }
        //回收 Graphics
        g2d.dispose();
        //裁圆
        outImage = convertCircular(outImage);
        //生成图片
        String format = FilenameUtils.getExtension(outPath);
        ImageIO.write(outImage, format, new File(outPath));
    }

    /**
     * 计算缩略图的边长
     * @param square 背景边长
     * @param interval 间隙
     * @param size 几张图
     * @param wxOrDd 1:钉钉效果  2:微信效果
     */
    private static int getLitimg(int square, int interval, int size ,int wxOrDd) {
        //不同张数的图片 合成时候的缩略图边长不同
        if(wxOrDd == 1){
            //钉钉尺寸
            if(size == 1 || size == 2){
                return square;
            }else{
                return square / 2;
            }
        }else {
            //微信只有两种尺寸  2*2  3*3
            if(size <= 4){
                return (square - interval * 3 ) / 2;
            }else {
                return (square - interval * 4 ) / 3;
            }
        }
    }

    /**
     * 裁圆
     * 传入的图像必须是正方形的 才会 圆形 如果是长方形的比例则会变成椭圆的
     */
    private static BufferedImage convertCircular(BufferedImage bi1) throws IOException {
        //设置底
        BufferedImage bi2 = new BufferedImage(bi1.getWidth(), bi1.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bi2.createGraphics();
        Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, bi1.getWidth(), bi1.getHeight());
        g2.setClip(shape);
        // 使用 setRenderingHint 设置抗锯齿
        g2.drawImage(bi1, 0, 0, bi1.getWidth(), bi1.getHeight(), null);
        // 设置颜色
        g2.setBackground(background);
        g2.dispose();
        return bi2;
    }

    /**
     * 图片缩放
     * @param filePath  图片路径
     * @param height 高度
     * @param width 宽度
     * @param bb 比例不对时是否需要补白
     */
    private static BufferedImage resize(String filePath, int height, int width, boolean bb) {
        try {
            // 缩放比例
            double ratio = 0;
            File f = new File(filePath);
            BufferedImage bi = ImageIO.read(f);
            Image itemp = bi.getScaledInstance(width, height,
                    Image.SCALE_SMOOTH);
            // 计算比例
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                if (bi.getHeight() > bi.getWidth()) {
                    ratio = (new Integer(height)).doubleValue()
                            / bi.getHeight();
                } else {
                    ratio = (new Integer(width)).doubleValue() / bi.getWidth();
                }
                AffineTransformOp op = new AffineTransformOp(
                        AffineTransform.getScaleInstance(ratio, ratio), null);
                itemp = op.filter(bi, null);
            }
            if (bb) {
                BufferedImage image = new BufferedImage(width, height,
                        BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, width, height);
                if (width == itemp.getWidth(null)){
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                }else {
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                }
                g.dispose();
                itemp = image;
            }
            return (BufferedImage) itemp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}