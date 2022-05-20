package top.dddpeter.ishare.common.oss.utils;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.OSSObject;
import lombok.extern.slf4j.Slf4j;
import top.dddpeter.ishare.common.oss.config.OssClientProperties;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lijinde
 * @date 2020/12/29
 */
@Slf4j
public class OSSUtils {

    /**
     * 获取 文件 流
     *
     * @params url
     */
    public static byte[] getFile(String url) throws IOException {
        URL urlConnection = new URL(url);
        HttpURLConnection con = (HttpURLConnection) urlConnection.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(4 * 1000);
        //通过输入流获取图片数据
        InputStream inStream = con.getInputStream();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 上传base64文件到OSS
     *
     * @param fileContent base64文件
     */
    public static void uploadFilrToOSS(byte[] fileContent, String pdfPath, OssClientProperties ossClientProperties) {
//        byte[] imageByte =  Base64.getDecoder().decode(fileContent);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileContent);
        OSS ossClient = new OSSClientBuilder().build(ossClientProperties.getEndpoint(), ossClientProperties.getAccessKeyId(),
                ossClientProperties.getAccessKeySecret());
        log.info("文件上传到OSS，endpoint = {}，accessKeyId = {}， accessKeySecret = {}, bucketName = {}",
                ossClientProperties.getEndpoint(), ossClientProperties.getAccessKeyId(),
                ossClientProperties.getAccessKeySecret(), ossClientProperties.getBucketName());
        ossClient.putObject(ossClientProperties.getBucketName(), pdfPath, byteArrayInputStream);
        ossClient.shutdown();
    }

    /**
     * 从OSS服务器下载文件
     */
    public static OSSObject downLoadFileFromOSS(String pdfPath, OssClientProperties ossClientProperties) {
        OSS ossClient = new OSSClientBuilder().build(ossClientProperties.getEndpoint(), ossClientProperties.getAccessKeyId(),
                ossClientProperties.getAccessKeySecret());
        // 设置过期时间
        Date expiration = new Date(System.currentTimeMillis() + 3600 * 24 * 31 * 120);
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(ossClientProperties.getBucketName(), pdfPath, HttpMethod.GET);
        // 设置过期时间。
        request.setExpiration(expiration);
        // 生成签名URL（HTTP GET请求）。
        URL signedUrl = ossClient.generatePresignedUrl(request);
        // 使用签名URL发送请求。
        Map<String, String> customHeaders = new HashMap<>();
        // 添加GetObject请求头。
        customHeaders.put("Range", "bytes=100-1000");
        OSSObject oss = ossClient.getObject(signedUrl, customHeaders);
        ossClient.shutdown();
        return oss;
    }
}
