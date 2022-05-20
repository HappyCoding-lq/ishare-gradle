
package top.dddpeter.ishare.common.encryption.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomUtils;
import top.dddpeter.ishare.common.encryption.exception.EncryptedException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

@Slf4j
public class AESUtil {

    private AESUtil() {}

    private static final String CHARSET = StandardCharsets.UTF_8.name();

    private static final String ENCRYPTED_TYPE_AES = "AES";

    private static final int KEY_LENGTH = 128;

    private static final int RANDOM_SIZE = 16;

    private static final String TRANSFORMATION = "AES/CFB/PKCS5Padding";

    /**
     * 生成16进制的 AES key
     */
    public static String generateKeyStr() {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance(ENCRYPTED_TYPE_AES);
            kgen.init(KEY_LENGTH);
            SecretKey sKey = kgen.generateKey();
            return byteToHexString(sKey.getEncoded());
        }catch (Exception ex){
            throw new EncryptedException("AESUtil generateKeyStr error");
        }
    }

    /**
     * 生成16进制AES key并转二进制后进行Base64编码
     */
    public static String generateKeyWithBase64() {
        try {
            return Base64.encodeBase64String(hexStringToByte(generateKeyStr()));
        }catch (Exception ex){
            throw new EncryptedException("AESUtil generateKeyWithBase64 error");
        }
    }

    /**
     * 生成16进制的 AES iv
     */
    public static String generateIvStr() {
        try {
            return byteToHexString(RandomUtils.nextBytes(RANDOM_SIZE));
        }catch (Exception ex){
            throw new EncryptedException("AESUtil generateIvStr error");
        }
    }

    /**
     * 生成16进制AES iv并转二进制后进行Base64编码
     */
    public static String generateIvWithBase64() {
        try {
            return Base64.encodeBase64String(RandomUtils.nextBytes(RANDOM_SIZE));
        }catch (Exception ex){
            throw new EncryptedException("AESUtil generateIvWithBase64 error");
        }
    }

    /**
     * 对str通过keyStrWithBase64和ivStrWithBase64进行AES加密
     */
    public static String encryptWithBase64(String str, String keyStrWithBase64, String ivStrWithBase64) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(1, getKey(keyStrWithBase64), getIv(ivStrWithBase64));
            byte[] encryptData = cipher.doFinal(str.getBytes(CHARSET));
            return Base64.encodeBase64String(encryptData);
        } catch (Exception ex) {
            throw new EncryptedException("AESUtil encrypt error");
        }
    }

    /**
     * 对str通过keyStr和ivStr进行AES加密
     */
    public static String encrypt(String str, String keyStr, String ivStr) {
        try {
            String aesKeyWithBase64 = Base64.encodeBase64String(hexStringToByte(keyStr));
            String aesIVWithBase64 = Base64.encodeBase64String(hexStringToByte(ivStr));
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(1, getKey(aesKeyWithBase64), getIv(aesIVWithBase64));
            byte[] encryptData = cipher.doFinal(str.getBytes(CHARSET));
            return Base64.encodeBase64String(encryptData);
        } catch (Exception ex) {
            throw new EncryptedException("AESUtil encrypt error");
        }
    }

    /**
     * 对str通过keyStrWithBase64和ivStrWithBase64进行AES解密
     */
    public static String decryptWithBase64(String str, String keyStrWithBase64, String ivStrWithBase64) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(2, getKey(keyStrWithBase64), getIv(ivStrWithBase64));
            byte[] decryptData = cipher.doFinal(Base64.decodeBase64(str.getBytes(CHARSET)));
            return new String(decryptData, CHARSET);
        } catch (Exception ex) {
            throw new EncryptedException("AESUtil decrypt error");
        }
    }

    /**
     * 对str通过keyStr和ivStr进行AES解密
     */
    public static String decrypt(String str, String keyStr, String ivStr) {
        try {
            String aesKeyWithBase64 = Base64.encodeBase64String(hexStringToByte(keyStr));
            String aesIVWithBase64 = Base64.encodeBase64String(hexStringToByte(ivStr));
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(2, getKey(aesKeyWithBase64), getIv(aesIVWithBase64));
            byte[] decryptData = cipher.doFinal(Base64.decodeBase64(str.getBytes(CHARSET)));
            return new String(decryptData, CHARSET);
        } catch (Exception ex) {
            throw new EncryptedException("AESUtil decrypt error");
        }
    }

    public static Key getKey(String keyStrWithBase64) {
        return new SecretKeySpec(Base64.decodeBase64(keyStrWithBase64.getBytes()), ENCRYPTED_TYPE_AES);
    }

    public static AlgorithmParameterSpec getIv(String ivStrWithBase64) {
        return new IvParameterSpec(Base64.decodeBase64(ivStrWithBase64));
    }

    /**
     * byte数组转化为16进制字符串
     */
    public static String byteToHexString(byte[] bytes) {
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                String strHex = Integer.toHexString(bytes[i]);
                if (strHex.length() > 3) {
                    sb.append(strHex.substring(6));
                } else {
                    if (strHex.length() < 2) {
                        sb.append("0" + strHex);
                    } else {
                        sb.append(strHex);
                    }
                }
            }
            return sb.toString();
        }catch (Exception ex){
            throw new EncryptedException("AES byteToHexString error");
        }
    }

    /**
     * 十六进制string转二进制byte[]
     */
    public static byte[] hexStringToByte(String s) {
        try {
            byte[] baKeyword = new byte[s.length() / 2];
            for (int i = 0; i < baKeyword.length; i++) {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            }
            return baKeyword;
        }catch (Exception ex){
            throw new EncryptedException("AES hexStringToByte error");
        }
    }

}
