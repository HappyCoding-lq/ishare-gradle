package top.dddpeter.ishare.common.enums;

import top.dddpeter.ishare.common.utils.StringUtils;

/**
 * json类型枚举
 */
public enum JsonTypeEnum {

    OBJECT("OBJECT", "json object"),
    ARRAY("ARRAY", "json array");

    private final String code;
    private final String value;

    JsonTypeEnum(String code, String value){
        this.code = code;
        this.value = value;
    }

    public String getCode()
    {
        return code;
    }

    public String getValue()
    {
        return value;
    }

    public static JsonTypeEnum getEnumByCode(String code) {
        if(StringUtils.isEmpty(code)){
            return null;
        }
        for(JsonTypeEnum em : JsonTypeEnum.values()){
            if(em.getCode().equals(code)){
                return  em;
            }
        }
        return null;
    }

}
