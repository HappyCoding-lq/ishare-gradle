package top.dddpeter.ishare.common.encryption.utils;

import org.apache.commons.codec.binary.Base64;
import top.dddpeter.ishare.common.encryption.domain.RSAKeys;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * RSA密钥生成工具
 * 
 * @author : huangshuanbao
 * @date : 2020/2/20 10:50 上午
 */
public class GenerateRsaKeyUtil {

    private GenerateRsaKeyUtil() {}

    /**
     * 生成一对RSA密钥
     * 
     * @author : huangshuanbao
     * @date : 2020/2/19 8:50 下午
     * @return : RSAKeys
     */
    public static RSAKeys createKeyPairs() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        String publicKey = Base64.encodeBase64String((keyPair.getPublic()).getEncoded());
        String privateKey = Base64.encodeBase64String((keyPair.getPrivate()).getEncoded());
        RSAKeys dto = new RSAKeys();
        dto.setPublicKey(publicKey);
        dto.setPrivateKey(privateKey);
        return dto;
    }

}
