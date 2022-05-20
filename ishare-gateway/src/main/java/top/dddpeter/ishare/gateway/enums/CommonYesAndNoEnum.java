package top.dddpeter.ishare.gateway.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public enum CommonYesAndNoEnum {

    YES("YES", "是"),
    NO("NO", "否");

    private final String code;

    private final String value;

    CommonYesAndNoEnum(String code, String value){
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
        for(CommonYesAndNoEnum em : CommonYesAndNoEnum.values()){
            if(code.equals(em.getCode())){
                return em.getValue();
            }
        }
        return null;
    }

    public static Map<Object, String> getCodeValueMap (){
        Map map = new LinkedHashMap();
        for(CommonYesAndNoEnum em : CommonYesAndNoEnum.values()){
            map.put(em.code, em.value);
        }
        return map;
    }

}
