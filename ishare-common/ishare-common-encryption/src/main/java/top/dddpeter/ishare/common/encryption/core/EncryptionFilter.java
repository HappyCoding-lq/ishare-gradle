package top.dddpeter.ishare.common.encryption.core;

import lombok.extern.slf4j.Slf4j;
import top.dddpeter.ishare.common.encryption.algorithm.EncryptAlgorithm;
import top.dddpeter.ishare.common.encryption.domain.EncryptionKeyDomain;
import top.dddpeter.ishare.common.encryption.exception.EncryptedException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 数据加解密过滤器
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/24 1:24 下午
 */
@Slf4j
public class EncryptionFilter implements Filter {

    private EncryptionConfig encryptionConfig;

    private EncryptAlgorithm encryptAlgorithm;

    public EncryptionFilter(EncryptionConfig config, EncryptAlgorithm encryptAlgorithm) {
        this.encryptionConfig = config;
        this.encryptAlgorithm = encryptAlgorithm;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        log.debug("RequestURI: {}", uri);

        // 调试模式不加解密
        if (encryptionConfig.isDebug()) {
            chain.doFilter(req, resp);
            return;
        }

        boolean decryptionStatus = this.contains(encryptionConfig.getRequestDecyptUriList(), uri, req.getMethod());
        boolean encryptionStatus = this.contains(encryptionConfig.getResponseEncryptUriList(), uri, req.getMethod());
        boolean decryptionIgnoreStatus = this.contains(encryptionConfig.getRequestDecyptUriIgnoreList(), uri, req.getMethod());
        boolean encryptionIgnoreStatus = this.contains(encryptionConfig.getResponseEncryptUriIgnoreList(), uri, req.getMethod());

        // 没有配置具体加解密的URI默认全部都开启加解密
        if (encryptionConfig.getRequestDecyptUriList().isEmpty()
                && encryptionConfig.getResponseEncryptUriList().isEmpty()) {
            decryptionStatus = true;
            encryptionStatus = true;
        }

        // 接口在忽略加密列表中
        if (encryptionIgnoreStatus) {
            encryptionStatus = false;
        }

        // 接口在忽略解密列表中
        if (decryptionIgnoreStatus) {
            decryptionStatus = false;
        }

        // 没有加解密操作
        if (!decryptionStatus && !encryptionStatus) {
            chain.doFilter(req, resp);
            return;
        }

        EncryptionResponseWrapper responseWrapper = null;
        EncryptionReqestWrapper reqestWrapper = null;

        // 渠道码
        String icode = null;

        // 密钥信息
        Map<String, EncryptionKeyDomain> keyDomain;
        try {
            keyDomain = encryptionConfig.getEncryptionKeyDomain();
        }catch (EncryptedException e){
            log.error(e.getMessage(), e);
            writeResponseContent(response, e.getMessage());
            return;
        }

        // 解密后数据
        String decyptRequestData;

        // 配置了需要解密才处理
        if (decryptionStatus) {
            reqestWrapper = new EncryptionReqestWrapper(req);
            String requestData = reqestWrapper.getRequestData();
            log.debug("RequestData: {}", requestData);
            log.info("当前加解密方式为: {}", encryptAlgorithm.getEncryptType());
            try {
                encryptAlgorithm.checkContent(requestData);
                icode = encryptAlgorithm.getICodeFromEncryptStr(requestData);
                if(!EncryptionKeyDomain.check(keyDomain, icode)){
                    log.error("加解密配置异常");
                    writeResponseContent(response, "加解密配置异常");
                    return;
                }
                decyptRequestData = encryptAlgorithm.decrypt(requestData, keyDomain.get(icode).getKeys());
                log.debug("DecyptRequestData: {}", decyptRequestData);
                reqestWrapper.setRequestData(decyptRequestData);
            } catch (EncryptedException e) {
                log.error("请求数据解密失败", e);
                writeResponseContent(response, e.getMessage());
                return;
            }
        }

        if (encryptionStatus) {
            responseWrapper = new EncryptionResponseWrapper(resp);
        }

        // 同时需要加解密
        if (encryptionStatus && decryptionStatus) {
            chain.doFilter(reqestWrapper, responseWrapper);
        } else if (encryptionStatus) { //只需要响应加密
            chain.doFilter(req, responseWrapper);
        } else { //只需要请求解密
            chain.doFilter(reqestWrapper, resp);
        }

        // 配置了需要加密才处理
        if (encryptionStatus) {
            String responseData = responseWrapper.getResponseData();
            log.debug("ResponeData: {}", responseData);
            try {
                responseData = encryptAlgorithm.encrypt(responseData, icode, keyDomain.get(icode).getKeys());
                log.debug("EncryptResponeData: {}", responseData);
                writeResponseContent(response, responseData);
            } catch (EncryptedException e) {
                log.error("响应数据加密失败", e);
                writeResponseContent(response, e.getMessage());
            }
        }

    }

    private boolean contains(List<String> list, String uri, String methodType) {
        if (list.contains(uri)) {
            return true;
        }
        String prefixUri = methodType.toLowerCase() + ":" + uri;
        return list.contains(prefixUri);
    }

    private void writeResponseContent(ServletResponse response, String responseData) throws IOException {
        ServletOutputStream out = null;
        try {
            response.setCharacterEncoding(encryptionConfig.getResponseCharset());
            out = response.getOutputStream();
            out.write(responseData.getBytes(encryptionConfig.getResponseCharset()));
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

}
