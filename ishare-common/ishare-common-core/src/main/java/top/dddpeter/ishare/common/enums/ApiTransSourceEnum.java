package top.dddpeter.ishare.common.enums;

import top.dddpeter.ishare.common.utils.StringUtils;

/**
 * api接口交易系统来源枚举
 *
 * @author huangshuanbao
 */
public enum ApiTransSourceEnum
{
    EC("EC", "电商"),
    CS("CS", "客服系统"),
    IB("IB", "I保"),
    DLM("DLM", "哆来咪"),
    ISHARE("ISHARE", "中台自身"),
    PC("PC", "产品迭代"),
    NC("NC", "新核心"),
    YF("YF", "员工福利平台"),
    NA("NA", "IhomePro"),
    ;

    private final String code;
    private final String value;

    ApiTransSourceEnum(String code, String value)
    {
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

    public static ApiTransSourceEnum getEnumByCode(String code) {
        if(StringUtils.isEmpty(code)){
            return null;
        }
        for(ApiTransSourceEnum em : ApiTransSourceEnum.values()){
            if(em.getCode().equals(code)){
                return  em;
            }
        }
        return null;
    }

}
