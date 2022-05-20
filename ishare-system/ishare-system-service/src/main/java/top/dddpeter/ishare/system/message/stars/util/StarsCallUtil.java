package top.dddpeter.ishare.system.message.stars.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import top.dddpeter.ishare.common.exception.IShareException;
import top.dddpeter.ishare.common.utils.JsonTrans;
import top.dddpeter.ishare.common.utils.StringUtils;
import top.dddpeter.ishare.system.message.stars.domain.StarsCommonReqDTO;
import top.dddpeter.ishare.system.message.stars.domain.StarsCommonResDTO;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class StarsCallUtil {

    private StarsCallUtil(){}

    public static StarsCommonResDTO toHttpPost(String url, StarsCommonReqDTO starsCommonDTO) {
        try {
            String param = JsonTrans.objectToJson(starsCommonDTO);
            log.info("调用满天星http请求地址为：{}", url);
            log.info("调用满天星http请求报文：{}", param);
            CloseableHttpClient httpClient = createSSLClientDefault();
            HttpPost method = new HttpPost(url);
            if (StringUtils.isNotEmpty(param)) {
                StringEntity entity = new StringEntity(param, StandardCharsets.UTF_8);
                entity.setContentEncoding(StandardCharsets.UTF_8.name());
                entity.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                method.setEntity(entity);
            }
            HttpResponse resultRsp = httpClient.execute(method);
            if(resultRsp.getStatusLine().getStatusCode() != HttpStatus.OK.value()){
                throw new IShareException("满天星调用失败，返回码：" + resultRsp.getStatusLine().getStatusCode());
            }
            String result = EntityUtils.toString(resultRsp.getEntity());
            log.info("满天星接口返回报文：\n {}", result);
            if(StringUtils.isEmpty(result)){
                throw new IShareException("满天星调用失败，返回结果为空");
            }
            return JsonTrans.fromJson(result, StarsCommonResDTO.class);
        } catch (IShareException ex) {
            throw ex;
        }catch (Exception ex) {
            throw new IShareException("满天星调用异常：" + ex.getMessage());
        }
    }

    private static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build();
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            //
        }
        return HttpClients.createDefault();
    }

}
