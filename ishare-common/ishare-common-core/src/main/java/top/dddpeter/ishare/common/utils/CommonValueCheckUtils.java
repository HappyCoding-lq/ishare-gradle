package top.dddpeter.ishare.common.utils;

/**
 * 通用值校验工具 银行卡号、手机号等
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/4/7 11:40 上午
 */
public class CommonValueCheckUtils {

    private CommonValueCheckUtils(){}

    /**
     * 校验银行卡卡号
     */
    public static boolean checkBankcard(String bankcard) {
        if (bankcard.length() < 15 || bankcard.length() > 19) {
            return false;
        }
        char bit = getBankcardCheckCode(bankcard.substring(0, bankcard.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return bankcard.charAt(bankcard.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     */
    private static char getBankcardCheckCode(String nonCheckCodeBankcard) {
        if (nonCheckCodeBankcard == null || nonCheckCodeBankcard.trim().length() == 0
                || !nonCheckCodeBankcard.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeBankcard.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

}
