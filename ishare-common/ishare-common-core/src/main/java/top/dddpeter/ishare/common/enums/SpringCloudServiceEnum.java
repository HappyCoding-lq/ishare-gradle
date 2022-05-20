package top.dddpeter.ishare.common.enums;

import top.dddpeter.ishare.common.utils.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 微服务枚举
 */
public enum SpringCloudServiceEnum {

    ISHARE_GATEWAY("ishare-gateway", "中台网关", null),
    ISHARE_AUTH("ishare-auth", "中台权限认证", "auth"),
    ISHARE_SYSTEM("ishare-system", "中台系统管理", "system"),
    ISHARE_TOOL("ishare-tool", "中台工具类项目", "tool"),
    ISHARE_GEN("ishare-gen", "中台代码生成工具项目", "gen"),
    ISHARE_JOB_ADMIN("ishare-job-admin", "中台任务调度管理中心", null),
    ISHARE_ORDER("ishare-order", "中台订单中心", "order"),
    ISHARE_PAY("ishare-pay", "中台支付中心", "pay"),
    ISHARE_INTELLIGENCE("ishare-intelligence", "中台商务智能中心", "intelligence"),
    ISHARE_USER("ishare-user", "中台用户中心", "user"),
    ISHARE_PRODUCT("ishare-product", "中台产品中心", "product");

    private final String code;
    private final String value;
    private final String swaggerPrefix;

    SpringCloudServiceEnum(String code, String value, String swaggerPrefix){
        this.code = code;
        this.value = value;
        this.swaggerPrefix = swaggerPrefix;
    }

    public String getCode()
    {
        return code;
    }

    public String getValue()
    {
        return value;
    }

    public String getSwaggerPrefix() {
        return swaggerPrefix;
    }

    public static SpringCloudServiceEnum getEnumByCode(String code) {
        if(StringUtils.isEmpty(code)){
            return null;
        }
        for(SpringCloudServiceEnum em : SpringCloudServiceEnum.values()){
            if(em.getCode().equals(code)){
                return  em;
            }
        }
        return null;
    }

    public static Map<Object, String> getCodeValueMap (){
        Map map = new LinkedHashMap();
        for(SpringCloudServiceEnum em : SpringCloudServiceEnum.values()){
            map.put(em.code, em.value);
        }
        return map;
    }

    public static Map<Object, String> getSwaggerPrefixValueMap (){
        Map map = new LinkedHashMap();
        for(SpringCloudServiceEnum em : SpringCloudServiceEnum.values()){
            if(StringUtils.isNotEmpty(em.swaggerPrefix)){
                map.put(em.swaggerPrefix, em.getCode() + "(" + em.value + ")");
            }
        }
        return map;
    }

}
