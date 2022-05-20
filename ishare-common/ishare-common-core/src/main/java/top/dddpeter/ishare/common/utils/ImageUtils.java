package top.dddpeter.ishare.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @ClassName ImageUtils
 * @Description
 * @Author 夜十一狼
 * @Version 1.0
 * @Create 2020-06-01 16:42:27
 * @Update
 */


public class ImageUtils {

    private ImageUtils() {
        throw new IllegalStateException("请不要对我实例化！");
    }

    /**
     * 根据图片链接转为base64数据
     *
     * @param imageUrl
     * @return
     * @throws Exception
     */
    public static String getBase64ByUrl(String imageUrl) throws IOException {
        URL url = null;
        HttpURLConnection conn = null;
        InputStream inStream = null;
        try{
            // new一个URL对象
            url = new URL(imageUrl);
            // 打开链接
            conn = (HttpURLConnection) url.openConnection();
            // 设置请求方式为"GET"
            conn.setRequestMethod("GET");
            // 超时响应时间为5秒
            conn.setConnectTimeout(5 * 1000);
            // 通过输入流获取图片数据
            inStream = conn.getInputStream();
            // 得到图片的二进制数据，以二进制封装得到数据，具有通用性
            byte[] data = readInputStream(inStream);
            return EncodeUtils.decodeBase64(data);
        } catch (Exception e){
            return "";
        } finally {
            if(inStream != null) {
                inStream.close();
            }
            if(conn != null) {
                conn.disconnect();
            }
        }
    }

    private static byte[] readInputStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        // 创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        // 每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        // 使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        // 关闭输入流
        inStream.close();
        // 把outStream里的数据写入内存
        return outStream.toByteArray();
    }


}
