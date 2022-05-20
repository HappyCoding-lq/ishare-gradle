package top.dddpeter.ishare.gateway.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public enum RouteTypeEnum {

    COMMON_API("commonAPI","公共API"),
    INNER("inner", "内部使用"),
    OPEN_URL("openURL", "开放链接"),
    OPEN_API("openAPI", "开放API");


    private final String code;

    private final String value;

    RouteTypeEnum(String code, String value){
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static String getValueByCode (String code){
        if (code==null || code.length()<=0) {
            return null;
        }
        for(RouteTypeEnum em : RouteTypeEnum.values()){
            if(code.equals(em.getCode())){
                return em.getValue();
            }
        }
        return null;
    }

    public static Map<Object, String> getCodeValueMap (){
        Map map = new LinkedHashMap();
        for(RouteTypeEnum em : RouteTypeEnum.values()){
            map.put(em.code, em.value);
        }
        return map;
    }

}
