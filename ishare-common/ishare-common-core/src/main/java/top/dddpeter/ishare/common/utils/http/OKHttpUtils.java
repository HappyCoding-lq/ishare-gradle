package top.dddpeter.ishare.common.utils.http;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: OkHttpUtils
 * @Description: OkHttp3工具类
 * @Author nightwolf
 * @Date: 2019-05-19 21:24
 * @Version: 1.0
 */
@Slf4j
public class OKHttpUtils {

    private OKHttpUtils() {
        throw new IllegalStateException("请不要对我实例化！");
    }

    public static String get(final String url) throws IOException {

        return get(url, null, null);

    }

    public static String get(String url, Map<String, String> headerMap, Map<String, String> paramMap) throws IOException {
        if (paramMap != null && !paramMap.isEmpty()) {
            url = url + "?" + map2String(paramMap);
        }
        Request request = new Request.Builder()
                .url(url)
                .build();
        if (headerMap != null && !headerMap.isEmpty()) {
            request = getNewRequest(request, headerMap);
        }
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
//                .addInterceptor(new LogInterceptor())
//                .sslSocketFactory(createSSLSocketFactory(), new OkHttpUtils.TrustAllManager())
//                .hostnameVerifier(new OkHttpUtils.TrustAllHostnameVerifier())
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static String postJSON(final String url, final String requestMessage) throws IOException {
        return postJSON(url, requestMessage, null);
    }

    public static String postJSON(final String url, String requestMessage, Map<String, String> headerMap) throws IOException {

        return post(30, TimeUnit.SECONDS, MediaType.parse("application/json; charset=utf-8"), url, requestMessage, headerMap);

    }

    public static String post(final int timeout, final TimeUnit timeUnit, final MediaType mediaType, final String url, final String requestMessage, Map<String, String> headerMap) throws IOException {

        Request request = new Request.Builder()
                .url(url)
//                .post(RequestBody.create(requestMessage, mediaType))
                .post(RequestBody.create(mediaType, requestMessage))
                .build();
        if (headerMap != null && !headerMap.isEmpty()) {
            request = getNewRequest(request, headerMap);
        }
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
//                .sslSocketFactory(createSSLSocketFactory(), new OkHttpUtils.TrustAllManager())
//                .hostnameVerifier(new OkHttpUtils.TrustAllHostnameVerifier())
//                .callTimeout(timeout, timeUnit)
                .connectTimeout(timeout, timeUnit)
                .readTimeout(timeout, timeUnit)
//                .addInterceptor(new LogInterceptor())
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }

    }

    private static Request getNewRequest(Request request, Map<String, String> headerMap) {
        Request.Builder requestBuilder = request.newBuilder();
        //遍历集合，将参数添加到请求头header中
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            requestBuilder.header(entry.getKey(), entry.getValue());
        }
        return requestBuilder.build();
    }

    /**
     * 默认信任所有的证书
     * 最好加上证书认证，主流App都有自己的证书
     *
     * @return
     */
    private static SSLSocketFactory createSSLSocketFactory() {

        SSLSocketFactory sSLSocketFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLSv1.2");
            sc.init(null, new TrustManager[]{new OKHttpUtils.TrustAllManager()},
                    new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {
            log.info("createSSLSocketFactory()-异常={}", e);
        }

        return sSLSocketFactory;
    }

    private static class TrustAllManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
            log.info("TrustAllManager.checkClientTrusted()-无信任证书！");

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
            log.info("TrustAllManager.checkServerTrusted()-无信任证书！");
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String requestedHost, SSLSession remoteServerSession) {
            return requestedHost.equalsIgnoreCase(remoteServerSession.getPeerHost());
        }
    }

    public static String map2String(final Map<String, String> messageMap) {

        Optional<String> messageStr = messageMap.entrySet().stream()
                .filter(m -> m.getValue() != null && !"".equals(m.getValue()) && !"null".equals(m.getValue()))
                .map(map -> map.getKey() + "=" + map.getValue())
                .reduce((a, b) -> a + "&" + b);

        return messageStr.isPresent() ? messageStr.get() : "";
    }

    public static String cleanUrl(String url) {
        return url.replaceAll("/+$", "");
    }


}
