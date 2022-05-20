package top.dddpeter.ishare.gateway.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public enum AuthTypeEnum {

    TOKEN("token", "token鉴权"),
    CLIENT("client", "客户端鉴权"),
    NO_AUTH("noAuth", "免鉴权");

    private final String code;

    private final String value;

    AuthTypeEnum(String code, String value){
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
        for(AuthTypeEnum em : AuthTypeEnum.values()){
            if(code.equals(em.getCode())){
                return em.getValue();
            }
        }
        return null;
    }

    public static Map<Object, String> getCodeValueMap (){
        Map map = new LinkedHashMap();
        for(AuthTypeEnum em : AuthTypeEnum.values()){
            map.put(em.code, em.value);
        }
        return map;
    }

}
