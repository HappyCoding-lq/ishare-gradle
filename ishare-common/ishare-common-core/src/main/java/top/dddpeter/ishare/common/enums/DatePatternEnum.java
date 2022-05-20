package top.dddpeter.ishare.common.enums;

import top.dddpeter.ishare.common.utils.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 常用时间格式化枚举
 */
public enum DatePatternEnum {

    yyyy_MM("yyyy-MM"),
    yyyy_MM_dd("yyyy-MM-dd"),
    yyyy_MM_dd_HH_mm_ss("yyyy-MM-dd HH:mm:ss"),
    HH_mm_ss("HH:mm:ss"),

    yyyyMM("yyyyMM"),
    yyyyMMdd("yyyyMMdd"),
    yyyyMMddHHmmss("yyyyMMddHHmmss"),
    HHmmss("HHmmss"),

    CHINESE_yyyy_MM("yyyy年MM月"),
    CHINESE_yyyy_MM_dd("yyyy年MM月dd日"),
    CHINESE_yyyy_MM_dd_HH_mm_ss("yyyy年MM月dd日 HH时mm分ss秒"),
    CHINESE_HH_mm_ss("HH时mm分ss秒");

    private final String code;

    DatePatternEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static DatePatternEnum getEnumByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (DatePatternEnum em : DatePatternEnum.values()) {
            if (em.getCode().equals(code)) {
                return em;
            }
        }
        return null;
    }

    public static String[] toCodeArray(DatePatternEnum[] enums){
        return Arrays.stream(enums).map(DatePatternEnum::getCode).collect(Collectors.joining(",")).split(",");
    }

}
