package top.dddpeter.ishare.common.encryption.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import com.alibaba.fastjson.JSON;
import top.dddpeter.ishare.common.encryption.domain.RSAEncryptedDomain;
import top.dddpeter.ishare.common.encryption.exception.EncryptedException;
import top.dddpeter.ishare.common.encryption.utils.DefaultAESUtil;
import top.dddpeter.ishare.common.encryption.utils.DefaultSignUtil;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


/**
 * 接口请求Demo（包含数据加密请求和解密)
 *
 * @author : huangshuanbao
 * @date : 2020/2/20 2:20 下午
 */
public class ApiTest {

    public static void main(String[] args) throws UnsupportedEncodingException, EncryptedException {
        System.out.println("接口请求Demo");
        /**
         * 测试环境接口地址
         */
        String url = "http://localhost:8004/api/getPayUrl";
        /**
         * 测试环境公钥。
         */
        String ptPublickey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCW8MgMu3IKfAPtMcAnx39YkjMYmHeYPyoE8UWJ5VTpdpuwaczGVPtVYEmvz402i4i4Ey1BLAU+QQqHCh8vNGfT+3Aj4wlSO3XnQXI+QWB03A+TJ6hX2ReLpvps4aIMkCW+aIrcVROb+WqXBMjgTa3uazeYUtcjbBYtZpUPEIkI5QIDAQAB";
        /**
         * 公钥 -- 渠道方
         */
        String strPubkey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnHfMEmKe4uple87C0Ewcqe/65x6spKJOXlsxLOQtRfmcszflhwdhbBXqqbqk+XjH64QpFWRtcEdHqodgwzUrN4G+kYIqYfzTI5G2d6+3kTfefGUCKQoHslmPWb+NgmQedYZmaLq+H+7b2x8oxe30R7suiRXk1tfv7TerB++zG8QIDAQAB";
        /**
         * 私钥-- 渠道方
         */
        String strPrikey = "MIIBNgIBADANBgkqhkiG9w0BAQEFAASCASAwggEcAgEAAoGBAKcd8wSYp7i6mV7zsLQTByp7/rnHqykok5eWzEs5C1F+ZyzN+WHB2FsFeqpuqT5eMfrhCkVZG1wR0eqh2DDNSs3gb6Rgiph/NMjkbZ3r7eRN958ZQIpCgeyWY9Zv42CZB51hmZour4f7tvbHyjF7fRHuy6JFeTW1+/tN6sH77MbxAgEAAoGAWe1R0k1bI5q14VR858m9d20V217IifNdyJrLAEeIAahlWMTVx3cKN6CxK3k09Wy2tGvYnQygHmEwdnP04/30nAnH5ycLmoN3BYNNu6QgujGG+04LuZ77RHKyTWRMYeHKbg8td0UL9cz9Qw54y/7S96ddP+q/1/SoAATUl1kE1AECAQACAQACAQACAQACAQA=";
        String dataJson = "{\n" +
                "\t\"channelCode\": \"1\",\n" +
                "\t\"transNo\":\"121211\",\n" +
                "\t\"orderId\":\"123213\",\n" +
                "\t\"amount\":\"12121212\",\n" +
                "\t\"pageBackUrl\":\"http://localhost:8004/api/queryOrderPage\",\n" +
                "\t\"dataBackUrl\":\"http://localhost:8004/api/queryOrderPage\"\n" +
                "}";
        // AES加密
        RSAEncryptedDomain initEncryData = DefaultAESUtil.buildRsaEncryptBody(dataJson, "test", strPubkey);
        // 加签
        DefaultSignUtil.buildRsaSign(initEncryData, strPrikey);
        System.out.println("一、加密后数据：" + JSON.toJSONString(initEncryData));
        String reqJm = JSON.toJSONString(initEncryData);
        System.out.println("二、组合后请求报文：" + reqJm);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        // 设置超时参数
        post.setConfig(post.getConfig().custom().setConnectionRequestTimeout(10000).setConnectTimeout(10000)
                .setSocketTimeout(10000).build());
        // 设置请求数据格式 json
        post.addHeader("Content-Type", "application/json;charset=utf-8");
        post.setEntity(new StringEntity(reqJm));
        String rescontext = "";
        try {
            // 调用接口并且获取返回值
            rescontext = copyToString(httpclient.execute(post).getEntity().getContent(), Charset.forName("UTF-8"));

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        System.out.println("三、响应原始报文：" + rescontext);
        RSAEncryptedDomain parameters = JSON.parseObject(rescontext, RSAEncryptedDomain.class);
        DefaultSignUtil.verifyRsaSign(parameters, strPubkey);
        String mwData = DefaultAESUtil.decryptRsaEncryptBody(parameters, strPrikey);
        System.out.println("四、响应明文数据:" + mwData);
    }

    public static String copyToString(InputStream in, Charset charset) throws IOException {
        if (in == null) {
            return "";
        }

        StringBuilder out = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(in, charset);
        char[] buffer = new char[4096];
        int bytesRead = -1;
        while ((bytesRead = reader.read(buffer)) != -1) {
            out.append(buffer, 0, bytesRead);
        }
        return out.toString();
    }

}
