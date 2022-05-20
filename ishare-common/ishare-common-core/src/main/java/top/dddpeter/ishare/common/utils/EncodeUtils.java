package top.dddpeter.ishare.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class EncodeUtils {

    private EncodeUtils() {
        throw new AssertionError("请不要对我实例化!");
    }

    private static final Charset DEFAULT_CHARSET_UTF8 = StandardCharsets.UTF_8;

    public static String encodeBase64Str(String str, Charset charset) {
        return new String(Base64.encodeBase64(str.getBytes(charset)), charset);
    }

    public static String encodeBase64Url(String str, Charset charset) {
        return Base64.encodeBase64URLSafeString(str.getBytes(charset));
    }

    public static String encodeBase64Url(String str){
        return encodeBase64Str(str,DEFAULT_CHARSET_UTF8);
    }

    public static String encodeBase64Str(String str) {
        return encodeBase64Str(str, DEFAULT_CHARSET_UTF8);
    }

    public static byte[] encodeMd5(String str, Charset charset) {
        return DigestUtils.md5(str.getBytes(charset));
    }

    public static byte[] encodeMd5(String str) {
        return encodeMd5(str, DEFAULT_CHARSET_UTF8);
    }

    public static String encodeMd5HexCase(String str, Charset charset, boolean uppercase) {
        return (uppercase ? DigestUtils.md5Hex(str.getBytes(charset)).toUpperCase() : DigestUtils.md5Hex(str.getBytes(charset)).toLowerCase());
    }

    public static String encodeMd5Hex(String str, Charset charset) {
        return DigestUtils.md5Hex(str.getBytes(charset));
    }

    public static String encodeMd5Hex(String str) {
        return encodeMd5Hex(str, DEFAULT_CHARSET_UTF8);
    }

    public static String encodeMd5HexUpCase(String str) {

        return encodeMd5HexCase(str, true);
    }

    public static String encodeMd5HexCase(String str, boolean uppercase) {

        return encodeMd5HexCase(str, DEFAULT_CHARSET_UTF8, uppercase);
    }

    public static String base64Md5Url(String str) {
        return Base64.encodeBase64URLSafeString(encodeMd5(str));
    }

    public static String decodeBase64(String encodeBase64String){
        return decodeBase64(encodeBase64String,DEFAULT_CHARSET_UTF8);
    }

    public static String decodeBase64(byte[] bytes){
       return Base64.encodeBase64String(bytes);
    }

    public static String decodeBase64(String encodeBase64String,Charset charset){
        return new String(Base64.decodeBase64(encodeBase64String),charset);
    }

    /**
     * MD5验证方法
     *
     * @param text 明文
     * @param key  密钥
     * @param md5  密文
     * @return true/false
     */
    public static boolean verify(String text, String key, String md5) {
        // 根据传入的密钥进行验证
        return encodeMd5Hex(text + key).equalsIgnoreCase(md5);
    }


    /**
     * 获取一个文件的md5值(可处理大文件)
     * @return md5值
     */
    public static String getFileMd5(File file) {
        FileInputStream fileInputStream = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, length);
            }
            return new String(Hex.encodeHex(digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                if (fileInputStream != null){
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
