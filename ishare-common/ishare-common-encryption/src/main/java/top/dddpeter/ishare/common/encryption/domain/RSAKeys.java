package top.dddpeter.ishare.common.encryption.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * RSA密钥对信息
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/20 10:47 上午
 */
@Data
public class RSAKeys implements Serializable {

    private static final long serialVersionUID = -5179186134541285817L;

    /**
     * RSA公钥串
     */
    private String publicKey;

    /**
     * RSA私钥串
     */
    private String privateKey;

}
