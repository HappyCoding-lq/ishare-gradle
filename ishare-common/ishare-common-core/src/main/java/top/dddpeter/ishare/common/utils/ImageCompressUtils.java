package top.dddpeter.ishare.common.utils;

import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * 图片压缩处理工具
 *
 * @author lzj10
 * @create 2020-09-30-宪10:38
 */
public class ImageCompressUtils {


    private ImageCompressUtils() {
        throw new IllegalStateException("Utility class");
    }

    //图片需要的大小 单位 kb
    public static final int INT = 2048;

    /**
     * 将图片转换为base64格式
     *
     * @param imageUrl：图片路径
     * @param sizeLimit：原图大小上限，当图片原图大小超过该值时先将图片大小 设置为该值以下再转换成base64格式,单位kb
     * @return
     */
    public static String convertImageUrlToBase64(String imageUrl, Integer sizeLimit) throws IOException {
        //默认上限为2048k
        if (sizeLimit == null) {
            sizeLimit = INT;
        }
        sizeLimit = sizeLimit * 1024;
        String base64Image;
        DataInputStream dataInputStream = null;
        ByteArrayOutputStream outputStream = null;
        ByteArrayInputStream inputStream = null;
        try {
            //从远程读取图片
            URL url = new URL(imageUrl);
            dataInputStream = new DataInputStream(url.openStream());
            outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            int length;
            while ((length = dataInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            byte[] context = outputStream.toByteArray();

            //将图片数据还原为图片
            inputStream = new ByteArrayInputStream(context);
            BufferedImage image = ImageIO.read(inputStream);
            int imageSize = context.length;
            int type = image.getType();
            int height = image.getHeight();
            int width = image.getWidth();

            byte[] data;
            if (imageSize > sizeLimit) {
                BufferedImage tempImage;
                //判断文件大小是否大于size,循环压缩，直到大小小于给定的值
                while (imageSize > sizeLimit) {
                    //将图片长宽压缩到原来的90%
                    height = Double.valueOf(height * 0.9).intValue();
                    width = Double.valueOf(width * 0.9).intValue();
                    tempImage = new BufferedImage(width, height, type);
                    // 绘制缩小后的图
                    tempImage.getGraphics().drawImage(image, 0, 0, width, height, null);
                    //重新计算图片大小
                    outputStream.reset();
                    ImageIO.write(tempImage, "JPEG", outputStream);
                    imageSize = outputStream.toByteArray().length;
                }

                //将图片转化为base64并返回
                data = outputStream.toByteArray();
            } else {
                data = context;
            }
            //此处一定要使用org.apache.tomcat.util.codec.binary.Base64，防止再linux上出现换行等特殊符号
            base64Image = Base64.encodeBase64String(data);
        } catch (Exception e) {
            //抛出异常
            throw e;
        } finally {
            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return base64Image;
    }
    public static String compressImageBase64(String base64, Integer sizeLimit) throws IOException {
        //默认上限为500k
        if (sizeLimit == null) {
            sizeLimit = INT;
        }
        sizeLimit = sizeLimit * 1024;
        String base64Image;
        DataInputStream dataInputStream = null;
        ByteArrayOutputStream outputStream = null;
        ByteArrayInputStream inputStream = null;
        try {
            //从base64读取图片
            byte[] context = Base64Utils.decode(base64);
            //将图片数据还原为图片
            inputStream = new ByteArrayInputStream(context);
            BufferedImage image = ImageIO.read(inputStream);
            int imageSize = context.length;
            int type = image.getType();
            int height = image.getHeight();
            int width = image.getWidth();

            byte[] data;
            if (imageSize > sizeLimit) {
                BufferedImage tempImage;
                //判断文件大小是否大于size,循环压缩，直到大小小于给定的值
                while (imageSize > sizeLimit) {
                    //将图片长宽压缩到原来的90%
                    height = Double.valueOf(height * 0.9).intValue();
                    width = Double.valueOf(width * 0.9).intValue();
                    tempImage = new BufferedImage(width, height, type);
                    // 绘制缩小后的图
                    tempImage.getGraphics().drawImage(image, 0, 0, width, height, null);
                    //重新计算图片大小
                    outputStream.reset();
                    ImageIO.write(tempImage, "JPEG", outputStream);
                    imageSize = outputStream.toByteArray().length;
                }

                //将图片转化为base64并返回
                data = outputStream.toByteArray();
            } else {
                data = context;
            }
            //此处一定要使用org.apache.tomcat.util.codec.binary.Base64，防止再linux上出现换行等特殊符号
            base64Image = Base64.encodeBase64String(data);
        } catch (Exception e) {
            //抛出异常
            throw e;
        } finally {
            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return base64Image;
    }


}
