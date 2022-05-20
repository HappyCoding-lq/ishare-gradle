
package top.dddpeter.ishare.common.encryption.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import top.dddpeter.ishare.common.encryption.exception.EncryptedException;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.EnumMap;
import java.util.Map;

public class RSAUtil {

    private static final String SIGN_TYPE_RSA = "RSA";
    private static final String PADDING = "RSA/ECB/PKCS1Padding";
    private static final String SIGN_SHA256RSA_ALGORITHMS = "SHA1WithRSA";
    private static final int DEFAULT_BUFFER_SIZE = 4096;
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final int MAX_ENCRYPT_BLOCK = 255;
    private static final int MAX_DECRYPT_BLOCK = 256;
    private static final int KEY_SIZE = 2048;

    private RSAUtil() {}

    public static Map<RSAUtil.KEY, String> getRSAKeys() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(SIGN_TYPE_RSA);
            keyPairGenerator.initialize(KEY_SIZE);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
            Map<RSAUtil.KEY, String> map = new EnumMap<>(RSAUtil.KEY.class);
            map.put(RSAUtil.KEY.PUBLICKEY, Base64.encodeBase64String(rsaPublicKey.getEncoded()));
            map.put(RSAUtil.KEY.PRIVATEKEY, Base64.encodeBase64String(rsaPrivateKey.getEncoded()));
            return map;
        }catch (Exception ex){
            throw new EncryptedException("RSAUtil getRSAKeys error");
        }
    }

    public static String rsa256Sign(String content, String privateKey) {
        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8(SIGN_TYPE_RSA, new ByteArrayInputStream(privateKey.getBytes()));
            Signature signature = Signature.getInstance(SIGN_SHA256RSA_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes(CHARSET));
            return Base64.encodeBase64String(signature.sign());
        } catch (Exception ex) {
            throw new EncryptedException(String.format("RSAUtil rsa256Sign <content=%s> error", content));
        }
    }

    public static boolean rsa256CheckContent(String content, String sign, String publicKey) {
        try {
            PublicKey pubKey = getPublicKeyFromX509(SIGN_TYPE_RSA, new ByteArrayInputStream(publicKey.getBytes()));
            Signature signature = Signature.getInstance(SIGN_SHA256RSA_ALGORITHMS);
            signature.initVerify(pubKey);
            signature.update(content.getBytes(CHARSET));
            return signature.verify(Base64.decodeBase64(sign));
        } catch (Exception ex) {
            throw new EncryptedException(String.format("RSAUtil rsa256CheckContent<RSAcontent=%s, sign=%s> error", content, sign));
        }
    }

    public static String rsaDecrypt(String content, String privateKey) {
        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8(SIGN_TYPE_RSA, new ByteArrayInputStream(privateKey.getBytes()));
            Cipher cipher = Cipher.getInstance(PADDING);
            cipher.init(2, priKey);
            byte[] encryptedData = Base64.decodeBase64(content.getBytes(CHARSET));
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;

            for(int i = 0; inputLen - offSet > 0; offSet = (++i) * MAX_DECRYPT_BLOCK) {
                byte[] cache;
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }

                out.write(cache, 0, cache.length);
            }

            byte[] decryptedData = out.toByteArray();
            out.close();
            return new String(decryptedData, CHARSET);
        } catch (Exception ex) {
            throw new EncryptedException(String.format("RSAUtil rsaDecrypt<EncodeContent=%s> error ", content));
        }
    }

    public static String rsaEncrypt(String content, String publicKey) {
        try {
            PublicKey pubKey = getPublicKeyFromX509(SIGN_TYPE_RSA, new ByteArrayInputStream(publicKey.getBytes()));
            Cipher cipher = Cipher.getInstance(PADDING);
            cipher.init(1, pubKey);
            byte[] data = content.getBytes(CHARSET);
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;

            for(int i = 0; inputLen - offSet > 0; offSet = (++i) * MAX_ENCRYPT_BLOCK) {
                byte[] cache;
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }

                out.write(cache, 0, cache.length);
            }

            byte[] encryptedData = Base64.encodeBase64(out.toByteArray());
            out.close();
            return new String(encryptedData, CHARSET);
        } catch (Exception ex) {
            throw new EncryptedException(String.format("RSAUtil rsaDecrypt<EncryptContent=%s> error ", content));
        }
    }

    private static PrivateKey getPrivateKeyFromPKCS8(String algorithm, InputStream ins) {
        try {
            if (ins != null && !StringUtils.isEmpty(algorithm)) {
                KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
                byte[] encodedKey = readText(ins).getBytes();
                encodedKey = Base64.decodeBase64(encodedKey);
                return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
            } else {
                return null;
            }
        }catch (Exception ex){
            throw new EncryptedException("RSAUtil getPrivateKeyFromPKCS8 error");
        }
    }

    private static PublicKey getPublicKeyFromX509(String algorithm, InputStream ins) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            StringWriter writer = new StringWriter();
            io(new InputStreamReader(ins), writer);
            byte[] encodedKey = writer.toString().getBytes();
            encodedKey = Base64.decodeBase64(encodedKey);
            return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
        }catch (Exception ex){
            throw new EncryptedException("RSAUtil getPublicKeyFromX509 error");
        }
    }

    private static String readText(InputStream ins) throws IOException {
        Reader reader = new InputStreamReader(ins);
        StringWriter writer = new StringWriter();
        io(reader, writer);
        return writer.toString();
    }

    private static void io(Reader in, Writer out) throws IOException {
        char[] buffer = new char[DEFAULT_BUFFER_SIZE];

        int amount;
        while((amount = in.read(buffer)) >= 0) {
            out.write(buffer, 0, amount);
        }

    }

    public enum KEY {
        PUBLICKEY,
        PRIVATEKEY;
        private KEY() {}
    }

}
