package top.dddpeter.ishare.common.utils.call;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import top.dddpeter.ishare.common.exception.IShareException;
import top.dddpeter.ishare.common.utils.StringUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;


/**
 * http方式接口调用工具
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/27 4:54 下午
 */
@Slf4j
public class HttpCallUtil {

    private HttpCallUtil() {}

    /**
     * json post请求
     * 
     * @author : huangshuanbao
     * @date : 2020/2/27 6:39 下午
     * @param httpUrl 请求地址
     * @param jsonParam 报文json串
     * @return : String – 调用结果
     */
    public static String httpPost(String httpUrl, String jsonParam) {
        try {
            CloseableHttpClient httpClient = createSSLClientDefault();
            HttpPost method = new HttpPost(httpUrl);
            if (StringUtils.isNotEmpty(jsonParam)) {
                // 默认使用Content-Type: application/json; charset=UTF-8
                StringEntity entity = new StringEntity(jsonParam, ContentType.APPLICATION_JSON);
                method.setEntity(entity);
            }
            HttpResponse resultRsp = httpClient.execute(method);
            if(resultRsp.getStatusLine().getStatusCode()==200){
                return EntityUtils.toString(resultRsp.getEntity());
            }else{
                throw new IShareException("发送请求失败，返回码：" + resultRsp.getStatusLine().getStatusCode());
            }
        } catch (IOException ex) {
            throw new IShareException(ex.getMessage(), ex);
        }
    }

    private static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build();
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            // 不做任何处理
        }
        return HttpClients.createDefault();
    }

}
