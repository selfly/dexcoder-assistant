package com.dexcoder.commons.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.*;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.dexcoder.commons.exceptions.CommonsAssistantException;

/**
 * 图片工具
 * <p/>
 * User: liyd
 * Date: 13-10-31 下午5:07
 * version $Id: ImageTools.java, v 0.1 Exp $
 */
public final class ImageUtils {

    /**
     * 默认图片格式
     */
    private static final String DEFAULT_IMAGE_FORMAT = "jpg";

    /**
     * 给图片添加水印、可设置水印图片旋转角度
     *
     * @param srcImage    the src image
     * @param waterImage  the water image
     * @param maxImgWidth the max img width
     * @param alpha       the alpha
     * @param pointX      the point x
     * @param pointY      the point y
     * @param degree      水印图片旋转角度
     * @param targetPath  目标图片路径
     */
    public static void resizeAddLogoSave(InputStream srcImage, InputStream waterImage, int maxImgWidth, float alpha,
                                         int pointX, int pointY, Integer degree, String targetPath) {
        try {
            BufferedImage srcImg = ImageIO.read(srcImage);

            ImageIcon imageIcon = new ImageIcon(srcImg);

            Image image = imageIcon.getImage();

            int iWidth = image.getWidth(null);
            if (iWidth > maxImgWidth) {
                int iHeight = image.getHeight(null);

                if (iWidth > iHeight) {
                    image = image.getScaledInstance(maxImgWidth, (maxImgWidth * iHeight) / iWidth, Image.SCALE_SMOOTH);
                } else {
                    image = image.getScaledInstance((maxImgWidth * iWidth) / iHeight, maxImgWidth, Image.SCALE_SMOOTH);
                }
            }

            // This code ensures that all the pixels in the image are loaded.
            Image temp = new ImageIcon(image).getImage();

            // Create the buffered image.
            BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null), temp.getHeight(null),
                BufferedImage.TYPE_INT_RGB);

            // Copy image to buffered image.
            Graphics2D g1 = bufferedImage.createGraphics();

            // Clear background and paint the image.
            g1.setColor(Color.white);
            g1.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
            g1.drawImage(temp, 0, 0, null);
            g1.dispose();

            // Soften.
            float softenFactor = 0.05f;
            float[] softenArray = { 0, softenFactor, 0, softenFactor, 1 - (softenFactor * 4), softenFactor, 0,
                    softenFactor, 0 };
            Kernel kernel = new Kernel(3, 3, softenArray);
            ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
            bufferedImage = cOp.filter(bufferedImage, null);

            //            //重设大小  此方法重设大小画质下降较大
            //            image = resize(image, maxImgWidth);

            // 创建画笔对象
            Graphics2D g = bufferedImage.createGraphics();

            // 设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            g.drawImage(image.getScaledInstance(image.getWidth(null), image.getHeight(null), Image.SCALE_SMOOTH), 0, 0,
                null);
            //
            if (null != degree) {
                // 设置水印旋转
                g.rotate(Math.toRadians(degree), (double) bufferedImage.getWidth() / 2,
                    (double) bufferedImage.getHeight() / 2);
            }

            // 水印图象的路径 水印一般为gif或者png的，这样可设置透明度
            ImageIcon imgIcon = new ImageIcon(ImageIO.read(waterImage));

            // 得到Image对象。
            Image img = imgIcon.getImage();

            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

            // 表示水印图片的位置
            g.drawImage(img, pointX, pointY, null);

            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

            g.dispose();

            //转换出的文件
            File fo = new File(targetPath);
            if (!fo.getParentFile().exists()) {
                fo.getParentFile().mkdirs();
            }
            String suffix = fo.getName().substring(fo.getName().lastIndexOf(".") + 1);
            ImageIO.write(bufferedImage, suffix, fo);
        } catch (Exception e) {
            throw new CommonsAssistantException("给图片添加水印出现错误", e);
        } finally {
            IOUtils.closeQuietly(srcImage);
            IOUtils.closeQuietly(waterImage);
        }
    }

    /**
     * 给图片添加文字水印、可设置水印的旋转角度
     *
     * @param logoText      the logo text 水印文字
     * @param fontColor     the font color 水印文字颜色
     * @param fontName      the font name 水印字体
     * @param fontSize      the font size 水印字体大小
     * @param alpha         the alpha 水印字体透明度
     * @param pointX        the point x 水印字体X坐标
     * @param pointY        the point y 水印字体Y坐标
     * @param degree        the degree 水印字段旋转角度
     * @param bufferedImage the buffered image 水印图片对象
     * @return the buffered image
     */
    public static BufferedImage watermarkText(String logoText, Color fontColor, String fontName, int fontSize,
                                              float alpha, int pointX, int pointY, Integer degree,
                                              BufferedImage bufferedImage) {
        try {

            // 得到画笔对象
            Graphics2D g = bufferedImage.createGraphics();

            // 设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            g.drawImage(bufferedImage.getScaledInstance(bufferedImage.getWidth(null), bufferedImage.getHeight(null),
                Image.SCALE_SMOOTH), 0, 0, null);

            if (null != degree) {
                // 设置水印旋转
                g.rotate(Math.toRadians(degree), (double) bufferedImage.getWidth() / 2,
                    (double) bufferedImage.getHeight() / 2);
            }

            // 设置颜色
            g.setColor(fontColor);

            // 设置 Font
            g.setFont(new Font(fontName, Font.PLAIN, fontSize));

            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

            // 第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y) .
            g.drawString(logoText, pointX, pointY);

            g.dispose();

            return bufferedImage;

        } catch (Exception e) {
            throw new RuntimeException("给图片添加水印失败", e);
        }
    }

    /**
     * 重设图片文件大小并设置水印后保存
     *
     * @param inputStream the input stream 图片文件流
     * @param logoText    the logo text 水印文字
     * @param maxImgWidth the max img width 图片重设大小最大宽度
     * @param fontColor   the font color 水印文件颜色
     * @param fontName    the font name 水印文字字体
     * @param fontSize    the font size 水印文字大小
     * @param alpha       the alpha 水印文字透明度
     * @param pointX      the point x 水印文字x坐标
     * @param pointY      the point y 水印文字y坐标
     * @param degree      the degree 水印文字旋转角度
     * @param targetPath  the target path 加工后图片保存路径
     */
    public static void resizeAddLogoWrite(InputStream inputStream, String logoText, int maxImgWidth, Color fontColor,
                                          String fontName, int fontSize, float alpha, int pointX, int pointY,
                                          Integer degree, String targetPath) {
        //        try {
        //            BufferedImage bufferedImage = ImageIO.read(inputStream);
        //            //重设大小
        //            BufferedImage resize = resize(bufferedImage, maxImgWidth);
        //            //添加水印
        //            resize = watermarkText(logoText, fontColor, fontName, fontSize, alpha, pointX, pointY,
        //                degree, resize);
        //            //转换出的文件
        //            File fo = new File(targetPath);
        //            if (!fo.getParentFile().exists()) {
        //                fo.getParentFile().mkdirs();
        //            }
        //            String suffix = fo.getName().substring(fo.getName().lastIndexOf(".") + 1);
        //            ImageIO.write(resize, suffix, fo);
        //        } catch (Exception e) {
        //            LOG.warn("重设图片文件大小出现错误", e);
        //            throw new RuntimeException(e);
        //        } finally {
        //            IOUtils.closeQuietly(inputStream);
        //        }
    }

    /**
     * 获取图片的宽和高
     *
     * @param imageFile 图片文件
     * @return int[] { 宽, 高 }
     */
    public static int[] getImageWH(File imageFile) {
        byte[] bytes = readFileToByte(imageFile);
        return getImageWH(bytes);
    }

    /**
     * 获取图片的宽和高
     *
     * @param bytes 图片byte数组
     * @return int[] { 宽, 高 }
     */
    public static int[] getImageWH(byte[] bytes) {

        InputStream is = null;
        try {
            is = new ByteArrayInputStream(bytes);
            BufferedImage image = ImageIO.read(is);

            int width = image.getWidth();
            int height = image.getHeight();
            return new int[] { width, height };

        } catch (Exception e) {
            throw new CommonsAssistantException("读取图片长宽出现异常", e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * 按指定的最大值缩放，以Math.max(width,height)为准
     *
     * @param bytes
     * @param maxSize
     * @param targetFile
     */
    public static void resizeWriteByMaxSize(byte[] bytes, int maxSize, File targetFile) {

        BufferedImage bufferedImage = resizeImage(bytes, maxSize, 0, true);
        String suffix = targetFile.getName().substring(targetFile.getName().lastIndexOf(".") + 1);
        writeImage(bufferedImage, suffix, targetFile);
    }

    /**
     * 按指定的最大值缩放，以Math.max(width,height)为准
     *
     * @param srcFile
     * @param maxSize
     * @param targetFile
     */
    public static void resizeWriteByMaxSize(File srcFile, int maxSize, File targetFile) {
        byte[] bytes = readFileToByte(srcFile);
        BufferedImage bufferedImage = resizeImage(bytes, maxSize, 0, true);
        String suffix = targetFile.getName().substring(targetFile.getName().lastIndexOf(".") + 1);
        writeImage(bufferedImage, suffix, targetFile);
    }

    /**
     * 根据宽度按比例缩放
     *
     * @param bytes
     * @param newWith
     * @param targetFile
     */
    public static void resizeWriteImageByWidth(byte[] bytes, int newWith, File targetFile) {

        BufferedImage bufferedImage = resizeImage(bytes, newWith, 0, false);
        String suffix = targetFile.getName().substring(targetFile.getName().lastIndexOf(".") + 1);
        writeImage(bufferedImage, suffix, targetFile);
    }

    /**
     * 根据宽度按比例缩放
     *
     * @param srcFile
     * @param newWith
     * @param targetFile
     */
    public static void resizeWriteImageByWidth(File srcFile, int newWith, File targetFile) {
        byte[] bytes = readFileToByte(srcFile);
        BufferedImage bufferedImage = resizeImage(bytes, newWith, 0, false);
        String suffix = targetFile.getName().substring(targetFile.getName().lastIndexOf(".") + 1);
        writeImage(bufferedImage, suffix, targetFile);
    }

    /**
     * 按指定的宽度、高度重设图片大小后保存
     *
     * @param bytes
     * @param newWidth
     * @param newHeight
     * @param targetFile
     */
    public static void resizeWriteImage(byte[] bytes, int newWidth, int newHeight, File targetFile) {

        BufferedImage bufferedImage = resizeImage(bytes, newWidth, newHeight, false);
        String suffix = targetFile.getName().substring(targetFile.getName().lastIndexOf(".") + 1);
        writeImage(bufferedImage, suffix, targetFile);
    }

    /**
     * 按指定的宽度、高度重设图片大小后保存
     *
     * @param srcFile
     * @param newWidth
     * @param newHeight
     * @param targetFile
     */
    public static void resizeWriteImage(File srcFile, int newWidth, int newHeight, File targetFile) {
        byte[] bytes = readFileToByte(srcFile);
        BufferedImage bufferedImage = resizeImage(bytes, newWidth, newHeight, false);
        String suffix = targetFile.getName().substring(targetFile.getName().lastIndexOf(".") + 1);
        writeImage(bufferedImage, suffix, targetFile);
    }

    /**
     * 按指定的最大值缩放，以Math.max(width,height)为准
     *
     * @param bytes   the bytes
     * @param maxSize the max size
     * @return the byte [ ]
     */
    public static byte[] resizeByMaxSize(byte[] bytes, int maxSize) {

        BufferedImage bufferedImage = resizeImage(bytes, maxSize, 0, true);

        return bufferedImageToByte(bufferedImage, DEFAULT_IMAGE_FORMAT);
    }

    /**
     * 按指定的最大值缩放，以Math.max(width,height)为准
     *
     * @param srcFile the src file
     * @param maxSize the max size
     * @return the byte [ ]
     */
    public static byte[] resizeByMaxSize(File srcFile, int maxSize) {
        byte[] bytes = readFileToByte(srcFile);
        BufferedImage bufferedImage = resizeImage(bytes, maxSize, 0, true);
        String suffix = srcFile.getName().substring(srcFile.getName().lastIndexOf(".") + 1);
        return bufferedImageToByte(bufferedImage, suffix);
    }

    /**
     * 根据宽度按比例缩放
     *
     * @param bytes   the bytes
     * @param newWith the new with
     * @return the byte [ ]
     */
    public static byte[] resizeImageByWidth(byte[] bytes, int newWith) {

        BufferedImage bufferedImage = resizeImage(bytes, newWith, 0, false);
        return bufferedImageToByte(bufferedImage, DEFAULT_IMAGE_FORMAT);
    }

    /**
     * 根据宽度按比例缩放
     *
     * @param srcFile the src file
     * @param newWith the new with
     * @return the byte [ ]
     */
    public static byte[] resizeImageByWidth(File srcFile, int newWith) {
        byte[] bytes = readFileToByte(srcFile);
        BufferedImage bufferedImage = resizeImage(bytes, newWith, 0, false);
        String suffix = srcFile.getName().substring(srcFile.getName().lastIndexOf(".") + 1);
        return bufferedImageToByte(bufferedImage, suffix);
    }

    /**
     * 按指定的宽度、高度重设图片大小后保存
     *
     * @param bytes     the bytes
     * @param newWidth  the new width
     * @param newHeight the new height
     * @return the byte [ ]
     */
    public static byte[] resizeImage(byte[] bytes, int newWidth, int newHeight) {

        BufferedImage bufferedImage = resizeImage(bytes, newWidth, newHeight, false);
        return bufferedImageToByte(bufferedImage, DEFAULT_IMAGE_FORMAT);
    }

    /**
     * 按指定的宽度、高度重设图片大小后保存
     *
     * @param srcFile   the src file
     * @param newWidth  the new width
     * @param newHeight the new height
     * @return the byte [ ]
     */
    public static byte[] resizeImage(File srcFile, int newWidth, int newHeight) {
        byte[] bytes = readFileToByte(srcFile);
        BufferedImage bufferedImage = resizeImage(bytes, newWidth, newHeight, false);
        String suffix = srcFile.getName().substring(srcFile.getName().lastIndexOf(".") + 1);
        return bufferedImageToByte(bufferedImage, suffix);
    }

    /**
     * 缩放后按指定宽、高裁切图片
     *
     * @param cutWidth
     * @param cutHeight
     * @return
     */
    public static byte[] resizeCutImage(byte[] bytes, int cutWidth, int cutHeight) {
        BufferedImage bufferedImage = resizeCutImage(bytes, cutWidth, cutHeight, true);
        return bufferedImageToByte(bufferedImage, DEFAULT_IMAGE_FORMAT);
    }

    /**
     * 缩放后按指定宽、高裁切图片
     *
     * @param srcFile
     * @param cutWidth
     * @param cutHeight
     * @return
     */
    public static byte[] resizeCutImage(File srcFile, int cutWidth, int cutHeight) {
        byte[] bytes = readFileToByte(srcFile);
        BufferedImage bufferedImage = resizeCutImage(bytes, cutWidth, cutHeight, true);
        String suffix = srcFile.getName().substring(srcFile.getName().lastIndexOf(".") + 1);
        return bufferedImageToByte(bufferedImage, suffix);
    }

    /**
     * 按指定的宽度、高度重设图片大小后保存
     *
     * @param srcFile
     * @param newWidth
     * @param newHeight
     * @param targetFile
     */
    public static void resizeCutWriteImage(File srcFile, int newWidth, int newHeight, File targetFile) {
        byte[] bytes = readFileToByte(srcFile);
        BufferedImage bufferedImage = resizeCutImage(bytes, newWidth, newHeight, true);
        String suffix = targetFile.getName().substring(targetFile.getName().lastIndexOf(".") + 1);
        writeImage(bufferedImage, suffix, targetFile);
    }

    /**
     * 按指定的宽度、高度重设图片大小后保存
     *
     * @param bytes
     * @param newWidth
     * @param newHeight
     * @param targetFile
     */
    public static void resizeCutWriteImage(byte[] bytes, int newWidth, int newHeight, File targetFile) {
        BufferedImage bufferedImage = resizeCutImage(bytes, newWidth, newHeight, true);
        String suffix = targetFile.getName().substring(targetFile.getName().lastIndexOf(".") + 1);
        writeImage(bufferedImage, suffix, targetFile);
    }

    /**
     * 裁切图片
     *
     * @param imageFile 图片文件
     * @param x         起始x坐标
     * @param y         起始y坐标
     * @param cutWidth  裁切的宽度
     * @param cutHeight 裁切的高度
     * @return
     */
    public static byte[] cutImage(File imageFile, int x, int y, int cutWidth, int cutHeight) {
        try {
            BufferedImage image = ImageIO.read(imageFile);
            image = image.getSubimage(x, y, cutWidth, cutHeight);
            String suffix = imageFile.getName().substring(imageFile.getName().lastIndexOf(".") + 1);
            return bufferedImageToByte(image, suffix);
        } catch (IOException e) {
            throw new CommonsAssistantException("读取图片失败", e);
        }
    }

    /**
     * 裁切图片
     *
     * @param imageBytes 图片文件
     * @param x          起始x坐标
     * @param y          起始y坐标
     * @param cutWidth   裁切的宽度
     * @param cutHeight  裁切的高度
     * @return
     */
    public static byte[] cutImage(byte[] imageBytes, int x, int y, int cutWidth, int cutHeight) {
        BufferedImage image = readByteToBufferedImage(imageBytes);
        image = image.getSubimage(x, y, cutWidth, cutHeight);
        return bufferedImageToByte(image, DEFAULT_IMAGE_FORMAT);
    }

    /**
     * 将BufferedImage对象转换成byte数组
     *
     * @param bufferedImage
     * @param imageFormat
     * @return
     */
    private static byte[] bufferedImageToByte(BufferedImage bufferedImage, String imageFormat) {

        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();

            ImageIO.write(bufferedImage, (StrUtils.isBlank(imageFormat) ? DEFAULT_IMAGE_FORMAT : imageFormat), bos);

            byte[] data = bos.toByteArray();

            return data;
        } catch (IOException e) {
            throw new CommonsAssistantException("将BufferedImage转换成byte数组失败", e);
        } finally {
            IOUtils.closeQuietly(bos);
        }
    }

    /**
     * 将文件转换成byte数组
     *
     * @param srcFile
     * @return
     */
    private static byte[] readFileToByte(File srcFile) {
        try {
            byte[] bytes = FileUtils.readFileToByteArray(srcFile);
            return bytes;
        } catch (IOException e) {
            throw new CommonsAssistantException("将图片文件转换成byte数组失败", e);
        }
    }

    /**
     * 将byte数组转换成BufferedImage
     *
     * @param bytes
     * @return
     */
    private static BufferedImage readByteToBufferedImage(byte[] bytes) {

        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
            return image;
        } catch (IOException e) {
            throw new CommonsAssistantException("将byte数组转换成BufferedImage对象失败", e);
        }
    }

    /**
     * 按指定格式将图片对象写入到指定文件
     *
     * @param bufferedImage
     * @param formatName
     * @param targetFile
     */
    private static void writeImage(BufferedImage bufferedImage, String formatName, File targetFile) {
        try {
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            ImageIO.write(bufferedImage, formatName, targetFile);
        } catch (IOException e) {
            throw new CommonsAssistantException("图片写入失败", e);
        }
    }

    /**
     * 图片按指定宽、高缩放
     *
     * @param bytes      图片byte数组
     * @param newWidth   指定的缩放宽度
     * @param newHeight  指定的缩放高度，0或小于0时将按比例缩放
     * @param isDoHeight 最大宽度是否作用于高度(true时即高度不得超过指定的宽度，竖形长图片高度超过宽度时以高度为准)
     * @return 缩放后的图片 buffered image
     */
    private static BufferedImage resizeImage(byte[] bytes, int newWidth, int newHeight, boolean isDoHeight) {

        BufferedImage srcImage = null;
        try {
            srcImage = ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (Exception e) {
            throw new RuntimeException("读取图片成BufferedImage失败", e);
        }

        //        ImageIcon ii = new ImageIcon(bytes);
        //        Image i = ii.getImage();
        Image resizedImage = null;

        //指定高度，强制缩放成指定尺寸
        if (newHeight > 0) {
            resizedImage = srcImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        } else {
            //按比例缩放
            int iWidth = srcImage.getWidth(null);
            int iHeight = srcImage.getHeight(null);
            if (iWidth < iHeight && isDoHeight) {
                resizedImage = srcImage.getScaledInstance((newWidth * iWidth) / iHeight, newWidth, Image.SCALE_SMOOTH);
            } else {
                resizedImage = srcImage.getScaledInstance(newWidth, (newWidth * iHeight) / iWidth, Image.SCALE_SMOOTH);
            }
        }

        // This code ensures that all the pixels in the image are loaded.
        Image temp = new ImageIcon(resizedImage).getImage();

        // Create the buffered image.
        BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null), temp.getHeight(null),
            BufferedImage.TYPE_INT_RGB);

        // Copy image to buffered image.
        Graphics g = bufferedImage.createGraphics();

        // Clear background and paint the image.
        g.setColor(Color.white);
        g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
        g.drawImage(temp, 0, 0, null);
        g.dispose();

        // Soften.
        float softenFactor = 0.05f;
        float[] softenArray = { 0, softenFactor, 0, softenFactor, 1 - (softenFactor * 4), softenFactor, 0,
                softenFactor, 0 };
        Kernel kernel = new Kernel(3, 3, softenArray);
        ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        bufferedImage = cOp.filter(bufferedImage, null);

        return bufferedImage;
    }

    /**
     * 图片裁切截取
     *
     * @param bytes     图片对象
     * @param cutWidth  裁切截取的宽度
     * @param cutHeight 裁切截取的高度
     * @param isZoom    是否缩放后截取
     * @return 裁切后的图片对象
     */
    private static BufferedImage resizeCutImage(byte[] bytes, int cutWidth, int cutHeight, boolean isZoom) {

        BufferedImage image = readByteToBufferedImage(bytes);

        //是否缩放后截取
        if (isZoom) {
            //获取图片原宽、高
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();

            //算出缩放的比例，宽、高以小的为准缩放截取，防止留白
            double scale;
            if (imageWidth < imageHeight) {
                scale = imageWidth / (double) cutWidth;
            } else {
                scale = imageHeight / (double) cutHeight;
            }

            //算出新的宽、高后缩放
            int newImageWidth = (int) (imageWidth / scale);
            int newImageHeight = (int) (imageHeight / scale);
            image = resizeImage(bytes, Math.max(newImageWidth, cutWidth), Math.max(newImageHeight, cutHeight), false);
        }

        // This code ensures that all the pixels in the image are loaded.
        Image temp = new ImageIcon(image).getImage();

        // Create the buffered image.
        BufferedImage bufferedImage = new BufferedImage(cutWidth, cutHeight, BufferedImage.TYPE_INT_RGB);

        // Copy image to buffered image.
        Graphics g = bufferedImage.createGraphics();

        // Clear background and paint the image.
        g.setColor(Color.white);
        g.fillRect(0, 0, cutWidth, cutHeight);
        g.drawImage(temp, 0, 0, null);
        g.dispose();

        // Soften.
        float softenFactor = 0.05f;
        float[] softenArray = { 0, softenFactor, 0, softenFactor, 1 - (softenFactor * 4), softenFactor, 0,
                softenFactor, 0 };
        Kernel kernel = new Kernel(3, 3, softenArray);
        ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        bufferedImage = cOp.filter(bufferedImage, null);

        return bufferedImage;
    }

    /**
     * 获取图片格式
     *
     * @param image
     * @return
     */
    public static String getImageFormat(File image) {
        byte[] bytes = readFileToByte(image);
        return getImageFormat(bytes);
    }

    /**
     * 获取图片格式
     *
     * @param image
     * @return
     */
    public static String getImageFormat(byte[] image) {

        ImageInputStream iis = null;
        try {
            //  Create an image input stream on the image
            iis = ImageIO.createImageInputStream(new ByteArrayInputStream(image));
            //  Find all image readers that recognize the image format
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            //  Use the first reader
            ImageReader reader = iter.next();
            //  Return the format name
            return reader.getFormatName();
        } catch (IOException e) {
            throw new CommonsAssistantException("获取图片格式失败", e);
        } finally {
            try {
                iis.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }
}
