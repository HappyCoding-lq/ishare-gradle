package top.dddpeter.ishare.common.encryption.utils;

import top.dddpeter.ishare.common.encryption.domain.AESEncryptedDomain;
import top.dddpeter.ishare.common.encryption.domain.RSAEncryptedDomain;
import top.dddpeter.ishare.common.encryption.exception.EncryptedException;


public class DefaultAESUtil {

    private DefaultAESUtil() {}

    /**
     * 对加密对象进行AES、RSA混合解密
     */
    public static String decryptRsaEncryptBody(RSAEncryptedDomain domain, String outerOrganizationPrivateKey) {
        try {
            String key = RSAUtil.rsaDecrypt(domain.getKey(), outerOrganizationPrivateKey);
            String salt = RSAUtil.rsaDecrypt(domain.getSalt(), outerOrganizationPrivateKey);
            return AESUtil.decryptWithBase64(domain.getData(), key, salt);
        } catch (Exception ex) {
            throw new EncryptedException("AESUtil decryptData error");
        }
    }

    /**
     * 对json串进行AES、RSA混合加密
     */
    public static RSAEncryptedDomain buildRsaEncryptBody(String dataString, String icode, String outerOrganizationPublicKey) {
        try {
            String aesKeyWithBase64 = AESUtil.generateKeyWithBase64();
            String aesIVWithBase64 = AESUtil.generateIvWithBase64();
            String encryptData = AESUtil.encryptWithBase64(dataString, aesKeyWithBase64, aesIVWithBase64);
            String key = RSAUtil.rsaEncrypt(aesKeyWithBase64, outerOrganizationPublicKey);
            String salt = RSAUtil.rsaEncrypt(aesIVWithBase64, outerOrganizationPublicKey);
            RSAEncryptedDomain encryptedBody = new RSAEncryptedDomain();
            encryptedBody.setData(encryptData);
            encryptedBody.setKey(key);
            encryptedBody.setSalt(salt);
            encryptedBody.setIcode(icode);
            return encryptedBody;
        }catch (Exception ex){
            throw new EncryptedException("AESUtil buildEncryptBody error");
        }
    }

    /**
     * 对str通过aesKey和aesSalt进行AES解密
     */
    public static String decryptAesEncryptBody(AESEncryptedDomain domain, String aesKey, String aesSalt) {
        try {
            return AESUtil.decrypt(domain.getData(), aesKey, aesSalt);
        } catch (Exception ex) {
            throw new EncryptedException("AESUtil decrypt error");
        }
    }

    /**
     * 对str通过aesKey和aesSalt进行AES加密
     */
    public static AESEncryptedDomain buildAesEncryptBody(String dataString, String icode, String aesKey, String aesSalt) {
        try {
            AESEncryptedDomain domain = new AESEncryptedDomain();
            domain.setIcode(icode);
            domain.setData(AESUtil.encrypt(dataString, aesKey, aesSalt));
            return domain;
        } catch (Exception ex) {
            throw new EncryptedException("AESUtil encrypt error");
        }
    }

    /**
     * 生成16进制的 AES key
     */
    public static String generateAesKey() {
        return AESUtil.generateKeyStr();
    }

    /**
     * 生成16进制的 AES salt
     */
    public static String generateAesSalt() {
        return AESUtil.generateIvStr();
    }

}
