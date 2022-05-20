package top.dddpeter.ishare.common.validator;

/**
 * Validator注解校验相关常量定义
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/18 5:03 下午
 */
public final class ValidatorConstants {

    private ValidatorConstants(){}

    // 枚举code校验默认方法
    public static final String ENUM_GET_CODE_DEFAULT_METHOD_NAME = "getCode";

    // url正则校验
    public static final String REQUEST_URL_REGEXP = "https?://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";

    // 手机号正则校验
    public static final String MOBILE_REGEXP = "^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\\d{8}$";

}
