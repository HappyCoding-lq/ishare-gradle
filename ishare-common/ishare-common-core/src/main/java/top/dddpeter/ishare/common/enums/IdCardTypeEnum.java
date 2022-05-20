package top.dddpeter.ishare.common.enums;

import top.dddpeter.ishare.common.utils.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 证件类型（资金的以签约接口的证件类型编码为准）
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/3/2 4:02 下午
 */
public enum IdCardTypeEnum {

    SHENFENZHENG("0","身份证", "0"),
    WAIGUO_HUZHAO("1","外国公民护照", "M"),
    JUNGUNZHENG("2","军人证（军官证）", "3"),
    JIAZHAO("3","驾照", "Z"),
    HUKOUBEN("4","户口本", "1"),
    XUESHENGZHENG("5","学生证", "Z"),
    GONGZUOZHENG("6","工作证", "Z"),
    CHUSHENGZHENG("7","出生证", "Z"),
    OTHER("8","其它证件", "Z"),
    NO_CARD("9","无证件", null),
    SHIBINGZHENG("A","士兵证", "4"),
    GANGGAO_LWDL_TXZ("B","港澳居民来往大陆通行证", "5"),
    LINSHI_SHENFENZHENG("C","临时身份证", "7"),
    JINGGUANZHENG("D","警官证", "9"),
    TW_LWDL_TXZ("E","台湾居民来往大陆通行证", "6"),
    DLLW_GANGAOTAI_TXZ("F","中国居民来往港澳台通行证", "Z"),
    GANGAO_JUZHUZHENG("G","港澳居民居住证", "A"),
    TW_JUZHUZHENG("H","台湾居民居住证", "A"),
    WAIGUOYONGJIU_JZSFZ("I","外国人永久居留身份证", "8"),
    CHINA_PASSPORT("J","中国护照","2");

    private final String code;
    private final String value;
    // 资金系统码表
    private final String zjxtCode;

    IdCardTypeEnum(String code, String value, String zjxtCode){
        this.code = code;
        this.value = value;
        this.zjxtCode = zjxtCode;
    }

    public String getCode()
    {
        return code;
    }

    public String getValue()
    {
        return value;
    }

    public String getZJXTCode() {
        return zjxtCode;
    }

    public static IdCardTypeEnum getEnumByCode(String code) {
        if(StringUtils.isEmpty(code)){
            return null;
        }
        for(IdCardTypeEnum em : IdCardTypeEnum.values()){
            if(em.getCode().equals(code)){
                return  em;
            }
        }
        return null;
    }

    public static String getValueByCode(String code){
        if(StringUtils.isEmpty(code)){
            return null;
        }
        for(IdCardTypeEnum em : IdCardTypeEnum.values()){
            if(em.getCode().equals(code)){
                return  em.getValue();
            }
        }
        return null;
    }

    public static String getZJXTCodeByCode(String code){
        if(StringUtils.isEmpty(code)){
            return null;
        }
        for(IdCardTypeEnum em : IdCardTypeEnum.values()){
            if(em.getCode().equals(code)){
                return  em.getZJXTCode();
            }
        }
        return null;
    }

    public static String getValuesBySplitCodes(String splitCodes) {
        if(StringUtils.isEmpty(splitCodes)){
            return null;
        }
        String[] codeArray = splitCodes.split(",");
        StringBuilder returnStr = new StringBuilder("");
        for(String code : codeArray){
            try{
                String value = getValueByCode(code);
                if(value==null){
                    return null;
                }
                returnStr.append(value).append(",");
            }catch(NumberFormatException ex){
                return null;
            }
        }
        returnStr.delete(returnStr.length()-1, returnStr.length());
        return returnStr.toString();
    }

    public static Map<Object, String> getCodeValueMap (){
        Map map = new LinkedHashMap();
        for(IdCardTypeEnum em : IdCardTypeEnum.values()){
            map.put(em.code, em.value);
        }
        return map;
    }
}
