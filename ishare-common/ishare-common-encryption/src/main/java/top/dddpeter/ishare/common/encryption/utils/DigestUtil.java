
package top.dddpeter.ishare.common.encryption.utils;

import org.apache.commons.lang3.StringUtils;
import top.dddpeter.ishare.common.encryption.exception.EncryptedException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.SortedMap;

public final class DigestUtil {

    private static final String CHARSET = StandardCharsets.UTF_8.name();

    private static final String DIGEST_ALGORITHM_SHA_256 = "SHA-256";

    private static final String DIGEST_ALGORITHM_MD5 = "MD5";

    private static final String DIGEST_ALGORITHM_SHA1 = "SHA1";

    private DigestUtil() {
    }

    public static String md5(String s) {
        return md5(s, CHARSET);
    }

    public static String md5(String s, String charset) {

        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] btInput;
            if (charset != null && !"".equals(charset)) {
                btInput = s.getBytes(charset);
            } else {
                btInput = s.getBytes();
            }
            MessageDigest mdInst = MessageDigest.getInstance(DIGEST_ALGORITHM_MD5);
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;

            for (int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 15];
                str[k++] = hexDigits[byte0 & 15];
            }

            return new String(str);
        } catch (Exception ex) {
            throw new EncryptedException("DigestUtil md5 error");
        }
    }

    public static String sha1(String s) {
        return sha1(s, CHARSET);
    }

    public static String sha1(String s, String charset) {
        try {
            byte[] btInput;
            if (charset != null && !"".equals(charset)) {
                btInput = s.getBytes(charset);
            } else {
                btInput = s.getBytes();
            }

            MessageDigest mdInst = MessageDigest.getInstance(DIGEST_ALGORITHM_SHA1);
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            StringBuilder sb = new StringBuilder(md.length * 2);

            for (int i = 0; i < md.length; ++i) {
                sb.append(Character.forDigit((md[i] & 240) >> 4, 16));
                sb.append(Character.forDigit(md[i] & 15, 16));
            }
            return sb.toString();
        } catch (Exception ex) {
            throw new EncryptedException("DigestUtil sha1 error");
        }
    }

    public static String md5ForMap(SortedMap<String, Object> treeMap, String keyValue) {
        return md5ForMap(treeMap, keyValue, CHARSET);
    }

    public static String md5ForMap(SortedMap<String, Object> treeMap, String keyValue, String charset) {
        if (treeMap != null && treeMap.size() != 0) {
            StringBuilder signStrBuilder = new StringBuilder();
            Iterator iter = treeMap.entrySet().iterator();

            while (iter.hasNext()) {
                Entry<?, ?> entry = (Entry) iter.next();
                if (entry.getValue() != null && StringUtils.isNotEmpty(entry.getValue().toString())) {
                    signStrBuilder.append("&").append((String) entry.getKey()).append("=").append(entry.getValue());
                }
            }

            if (StringUtils.isNotEmpty(keyValue)) {
                signStrBuilder.append("&key=").append(keyValue);
            }
            String signStr = signStrBuilder.substring(1);
            return md5(signStr, charset);
        } else {
            return "";
        }
    }

    public static String md5ForMapString(SortedMap<String, String> treeMap, String keyValue) {
        return md5ForMapString(treeMap, keyValue, CHARSET);
    }

    public static String md5ForMapString(SortedMap<String, String> treeMap, String keyValue, String charset) {
        if (treeMap != null && treeMap.size() != 0) {
            StringBuilder signStrBuilder = new StringBuilder();
            Iterator iter = treeMap.entrySet().iterator();

            while (iter.hasNext()) {
                Entry<String, String> entry = (Entry) iter.next();
                if (StringUtils.isNotEmpty(entry.getValue())) {
                    signStrBuilder.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }

            if (StringUtils.isNotEmpty(keyValue)) {
                signStrBuilder.append("&key=").append(keyValue);
            }

            String signStr = signStrBuilder.substring(1);
            return md5(signStr, charset);
        } else {
            return "";
        }
    }

    public static String sha256Sign(String input) {
        try {
            MessageDigest mDigest = MessageDigest.getInstance(DIGEST_ALGORITHM_SHA_256);
            byte[] result = mDigest.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < result.length; ++i) {
                sb.append(Integer.toString((result[i] & 255) + 256, 16).substring(1));
            }
            return sb.toString();
        } catch (Exception ex) {
            throw new EncryptedException("DigestUtil sha256Sign error");
        }
    }
}
