
package top.dddpeter.ishare.common.encryption.domain;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 加解密相关参数定义
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/20 10:58 上午
 */
@Data
public class RSAEncryptedDomain implements Serializable {

    private static final long serialVersionUID = -2930484192832235798L;

    /**
     * 渠道码
     */
    private String icode;

    /**
     * 加密数据
     */
    private String data;

    /**
     * AES的密钥(随机产生)
     */
    private String key;

    /**
     * AES的初始化向量(随机产生)
     */
    private String salt;

    /**
     * 签名数据
     */
    private String sign;

    public boolean checkEncrypt(){
        return StringUtils.isNotBlank(icode) && StringUtils.isNotBlank(data)
                && StringUtils.isNotBlank(key) && StringUtils.isNotBlank(salt) && StringUtils.isNotBlank(sign);
    }

}
