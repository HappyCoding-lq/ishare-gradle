package top.dddpeter.ishare.common.encryption.core;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import top.dddpeter.ishare.common.encryption.constants.EncryptedConstant;
import top.dddpeter.ishare.common.encryption.domain.ChannelAesKeyDomain;
import top.dddpeter.ishare.common.encryption.domain.ChannelRsaKeyDomain;
import top.dddpeter.ishare.common.encryption.domain.EncryptionKeyDomain;
import top.dddpeter.ishare.common.encryption.enums.EncryptTypeEnum;
import top.dddpeter.ishare.common.encryption.exception.EncryptedException;
import top.dddpeter.ishare.common.encryption.init.ApiEncryptDataInit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 加解密配置类
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/24 1:14 下午
 */
@ConfigurationProperties(prefix = EncryptedConstant.ENCRYPTED_CONFIG_PREFIX_OF_CHANNEL_PUB_KEY)
public class EncryptionConfig {

    /**
     * 加密方式 默认为RSA方式
     */
    private String encryptType = EncryptTypeEnum.RSA.getCode();

    /**
     * RSA私钥串
     */
    private String rsaPrivateKey;

    /**
     * 渠道方RSA公钥串集合
     */
    private List<ChannelRsaKeyDomain> channelRsaPublicKeyList;

    /**
     * 渠道方AES密钥集合
     */
    private List<ChannelAesKeyDomain> channelAesKeyList;

    /**
     * 需要对响应内容进行加密的接口URI<br>
     * 比如：/user/list<br>
     * 不支持@PathVariable格式的URI
     */
    private List<String> responseEncryptUriList = new ArrayList<>();

    /**
     * 需要对请求内容进行解密的接口URI<br>
     * 比如：/user/list<br>
     * 不支持@PathVariable格式的URI
     */
    private List<String> requestDecyptUriList = new ArrayList<>();

    /**
     * 忽略加密的接口URI<br>
     * 比如：/user/list<br>
     * 不支持@PathVariable格式的URI
     */
    private List<String> responseEncryptUriIgnoreList = new ArrayList<>();

    /**
     * 忽略对请求内容进行解密的接口URI<br>
     * 比如：/user/list<br>
     * 不支持@PathVariable格式的URI
     */
    private List<String> requestDecyptUriIgnoreList = new ArrayList<>();

    /**
     * 响应数据编码
     */
    private String responseCharset = "UTF-8";

    /**
     * 开启调试模式，调试模式下不进行加解密操作，用于像Swagger这种在线API测试场景
     */
    private boolean debug = false;

    /**
     * 过滤器拦截模式
     */
    private String[] urlPatterns = new String[]{"/*"};

    /**
     * 过滤器执行顺序
     */
    private int order = 1;

    public EncryptionConfig() {
        super();
    }

    public EncryptionConfig(RsaBuilder builder) {
        super();
        this.encryptType = EncryptTypeEnum.RSA.getCode();
        this.rsaPrivateKey = builder.rsaPrivateKey;
        this.channelRsaPublicKeyList = builder.channelRsaPublicKeyList;
        this.responseEncryptUriList = builder.responseEncryptUriList;
        this.requestDecyptUriList = builder.requestDecyptUriList;
        this.responseEncryptUriIgnoreList = builder.responseEncryptUriIgnoreList;
        this.requestDecyptUriIgnoreList = builder.requestDecyptUriIgnoreList;
        this.responseCharset = builder.responseCharset;
        this.debug = builder.debug;
        this.urlPatterns = builder.urlPatterns;
        this.order = builder.order;
    }

    public EncryptionConfig(AesBuilder builder) {
        super();
        this.encryptType = EncryptTypeEnum.AES.getCode();
        this.channelAesKeyList = builder.channelAesKeyList;
        this.responseEncryptUriList = builder.responseEncryptUriList;
        this.requestDecyptUriList = builder.requestDecyptUriList;
        this.responseEncryptUriIgnoreList = builder.responseEncryptUriIgnoreList;
        this.requestDecyptUriIgnoreList = builder.requestDecyptUriIgnoreList;
        this.responseCharset = builder.responseCharset;
        this.debug = builder.debug;
        this.urlPatterns = builder.urlPatterns;
        this.order = builder.order;
    }

    public String getEncryptType() {
        return encryptType;
    }

    public void setEncryptType(String encryptType) {
        this.encryptType = encryptType;
    }

    public String getRsaPrivateKey() {
        return rsaPrivateKey;
    }

    public void setRsaPrivateKey(String rsaPrivateKey) {
        this.rsaPrivateKey = rsaPrivateKey;
    }

    public void setChannelRsaPublicKeyList(List<ChannelRsaKeyDomain> channelRsaPublicKeyList) {
        this.channelRsaPublicKeyList = channelRsaPublicKeyList;
    }

    public List<ChannelRsaKeyDomain> getChannelRsaPublicKeyList() {
        return channelRsaPublicKeyList;
    }

    public List<ChannelAesKeyDomain> getChannelAesKeyList() {
        return channelAesKeyList;
    }

    public void setChannelAesKeyList(List<ChannelAesKeyDomain> channelAesKeyList) {
        this.channelAesKeyList = channelAesKeyList;
    }

    public Map<String, EncryptionKeyDomain> getChannelRsaPublicKeyMapList() {
        if(StringUtils.isBlank(rsaPrivateKey) || channelRsaPublicKeyList== null || channelRsaPublicKeyList.isEmpty()){
            throw new EncryptedException("RSA加解密配置异常");
        }
        channelRsaPublicKeyList.forEach(channelRsaKeyDomain -> {
            if(channelRsaKeyDomain == null || !channelRsaKeyDomain.checkData()){
                throw new EncryptedException("RSA加解密配置异常");
            }
        });
        return channelRsaPublicKeyList.stream().collect(Collectors.toMap(ChannelRsaKeyDomain::getIcode, item -> {
            EncryptionKeyDomain domain = new EncryptionKeyDomain();
            domain.setIcode(item.getIcode());
            domain.setKeys(item.getPublicKey(), rsaPrivateKey);
            return domain;
        }));
    }

    public Map<String, EncryptionKeyDomain> getChannelAesKeyMapList() {
        if(channelAesKeyList== null || channelAesKeyList.isEmpty()){
            throw new EncryptedException("AES加解密配置异常");
        }
        channelAesKeyList.forEach(channelAesKeyDomain -> {
            if(channelAesKeyDomain == null || !channelAesKeyDomain.checkData()){
                throw new EncryptedException("AES加解密配置异常");
            }
        });
        return channelAesKeyList.stream().collect(Collectors.toMap(ChannelAesKeyDomain::getIcode, item -> {
            EncryptionKeyDomain domain = new EncryptionKeyDomain();
            domain.setIcode(item.getIcode());
            domain.setKeys(item.getAesKey(), item.getAesSalt());
            return domain;
        }));
    }

    public Map<String, EncryptionKeyDomain> getEncryptionKeyDomain(){
        try {
            if (EncryptTypeEnum.RSA.getCode().equals(getEncryptType())) {
                return getChannelRsaPublicKeyMapList();
            } else if (EncryptTypeEnum.AES.getCode().equals(getEncryptType())) {
                return getChannelAesKeyMapList();
            } else {
                throw new EncryptedException("加解密方式配置异常");
            }
        }catch (EncryptedException ex){
            throw ex;
        }catch (Exception ex) {
            throw new EncryptedException("加解密配置异常");
        }
    }

    public List<String> getResponseEncryptUriList() {
        // 配置了注解则用注解获取的URI
        if (!ApiEncryptDataInit.responseEncryptUriList.isEmpty()) {
            return ApiEncryptDataInit.responseEncryptUriList;
        }
        return responseEncryptUriList;
    }

    public void setResponseEncryptUriList(List<String> responseEncryptUriList) {
        this.responseEncryptUriList = responseEncryptUriList;
    }

    public List<String> getRequestDecyptUriList() {
        // 配置了注解则用注解获取的URI
        if (!ApiEncryptDataInit.requestDecyptUriList.isEmpty()) {
            return ApiEncryptDataInit.requestDecyptUriList;
        }
        return requestDecyptUriList;
    }

    public void setRequestDecyptUriList(List<String> requestDecyptUriList) {
        this.requestDecyptUriList = requestDecyptUriList;
    }

    public String getResponseCharset() {
        return responseCharset;
    }

    public void setResponseCharset(String responseCharset) {
        this.responseCharset = responseCharset;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setUrlPatterns(String[] urlPatterns) {
        this.urlPatterns = urlPatterns;
    }

    public String[] getUrlPatterns() {
        return urlPatterns;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public List<String> getResponseEncryptUriIgnoreList() {
        // 配置了注解则用注解获取的URI
        if (!ApiEncryptDataInit.responseEncryptUriIgnoreList.isEmpty()) {
            return ApiEncryptDataInit.responseEncryptUriIgnoreList;
        }
        return responseEncryptUriIgnoreList;
    }

    public void setResponseEncryptUriIgnoreList(List<String> responseEncryptUriIgnoreList) {
        this.responseEncryptUriIgnoreList = responseEncryptUriIgnoreList;
    }

    public List<String> getRequestDecyptUriIgnoreList() {
        // 配置了注解则用注解获取的URI
        if (!ApiEncryptDataInit.requestDecyptUriIgnoreList.isEmpty()) {
            return ApiEncryptDataInit.requestDecyptUriIgnoreList;
        }
        return requestDecyptUriIgnoreList;
    }

    public void setRequestDecyptUriIgnoreList(List<String> requestDecyptUriIgnoreList) {
        this.requestDecyptUriIgnoreList = requestDecyptUriIgnoreList;
    }

    public abstract static class CommonBuilder {

        /**
         * 需要对响应内容进行加密的接口URI<br>
         * 比如：/user/list<br>
         * 不支持@PathVariable格式的URI
         */
        List<String> responseEncryptUriList;

        /**
         * 需要对请求内容进行解密的接口URI<br>
         * 比如：/user/list<br>
         * 不支持@PathVariable格式的URI
         */
        List<String> requestDecyptUriList;

        /**
         * 忽略加密的接口URI<br>
         * 比如：/user/list<br>
         * 不支持@PathVariable格式的URI
         */
        List<String> responseEncryptUriIgnoreList;

        /**
         * 忽略对请求内容进行解密的接口URI<br>
         * 比如：/user/list<br>
         * 不支持@PathVariable格式的URI
         */
        List<String> requestDecyptUriIgnoreList;

        /**
         * 响应数据编码
         */
        String responseCharset;
        /**
         * 开启调试模式，调试模式下不进行加解密操作，用于像Swagger这种在线API测试场景
         */
        boolean debug;

        /**
         * 过滤器拦截模式
         */
        String[] urlPatterns;

        /**
         * 过滤器执行顺序
         */
        int order;

        protected abstract CommonBuilder responseEncryptUriList(List<String> value);

        protected abstract CommonBuilder requestDecyptUriList(List<String> value);

        protected abstract CommonBuilder responseEncryptUriIgnoreList(List<String> value);

        protected abstract CommonBuilder requestDecyptUriIgnoreList(List<String> value);

        protected abstract CommonBuilder responseCharset(String value);

        protected abstract CommonBuilder debug(boolean value);

        protected abstract CommonBuilder urlPatterns(String[] value);

        protected abstract CommonBuilder order(int value);

        protected abstract EncryptionConfig build();

    }

    public static class RsaBuilder extends CommonBuilder {

        /**
         * RSA私钥串
         */
        private String rsaPrivateKey;

        /**
         * 渠道方RSA公钥串集合
         */
        private List<ChannelRsaKeyDomain> channelRsaPublicKeyList;

        public RsaBuilder rsaPrivateKey(String value) {
            this.rsaPrivateKey = value;
            return this;
        }

        public RsaBuilder channelRsaPublicKeyList(List<ChannelRsaKeyDomain> value) {
            this.channelRsaPublicKeyList = value;
            return this;
        }

        @Override
        public RsaBuilder responseEncryptUriList(List<String> value) {
            this.responseEncryptUriList = value;
            return this;
        }

        @Override
        public RsaBuilder requestDecyptUriList(List<String> value) {
            this.requestDecyptUriList = value;
            return this;
        }

        @Override
        public RsaBuilder responseEncryptUriIgnoreList(List<String> value) {
            this.responseEncryptUriIgnoreList = value;
            return this;
        }

        @Override
        public RsaBuilder requestDecyptUriIgnoreList(List<String> value) {
            this.requestDecyptUriIgnoreList = value;
            return this;
        }

        @Override
        public RsaBuilder responseCharset(String value) {
            this.responseCharset = value;
            return this;
        }

        @Override
        public RsaBuilder debug(boolean value) {
            this.debug = value;
            return this;
        }

        @Override
        public RsaBuilder urlPatterns(String[] value) {
            this.urlPatterns = value;
            return this;
        }

        @Override
        public RsaBuilder order(int value) {
            this.order = value;
            return this;
        }

        @Override
        public EncryptionConfig build() {
            return new EncryptionConfig(this);
        }
    }

    public static class AesBuilder extends CommonBuilder {

        /**
         * 渠道方AES密钥集合
         */
        private List<ChannelAesKeyDomain> channelAesKeyList;

        public AesBuilder channelAesKeyList(List<ChannelAesKeyDomain> value) {
            this.channelAesKeyList = value;
            return this;
        }

        @Override
        public AesBuilder responseEncryptUriList(List<String> value) {
            this.responseEncryptUriList = value;
            return this;
        }

        @Override
        public AesBuilder requestDecyptUriList(List<String> value) {
            this.requestDecyptUriList = value;
            return this;
        }

        @Override
        public AesBuilder responseEncryptUriIgnoreList(List<String> value) {
            this.responseEncryptUriIgnoreList = value;
            return this;
        }

        @Override
        public AesBuilder requestDecyptUriIgnoreList(List<String> value) {
            this.requestDecyptUriIgnoreList = value;
            return this;
        }

        @Override
        public AesBuilder responseCharset(String value) {
            this.responseCharset = value;
            return this;
        }

        @Override
        public AesBuilder debug(boolean value) {
            this.debug = value;
            return this;
        }

        @Override
        public AesBuilder urlPatterns(String[] value) {
            this.urlPatterns = value;
            return this;
        }

        @Override
        public AesBuilder order(int value) {
            this.order = value;
            return this;
        }

        @Override
        public EncryptionConfig build() {
            return new EncryptionConfig(this);
        }
    }

}
