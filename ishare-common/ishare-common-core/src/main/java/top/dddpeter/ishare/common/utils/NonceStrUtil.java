package top.dddpeter.ishare.common.utils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * 随机字符串生成工具
 *
 * @author : ishare
 */
public class NonceStrUtil {

    private static final String NUM_SYMBOLS = "0123456789"; // 数字

    private static final String LETTER_SYMBOLS = "abcdefghijklmnopqrstuvwxyz"; // 字母

    private static final Random RANDOM = new SecureRandom();

    private NonceStrUtil() {}

    public static String getNumsNonceStr(int length) {
        char[] nonceChars = new char[length];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = NUM_SYMBOLS.charAt(RANDOM.nextInt(NUM_SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

    public static String getLettersNonceStr(int length) {
        char[] nonceChars = new char[length];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = LETTER_SYMBOLS.charAt(RANDOM.nextInt(LETTER_SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

}
