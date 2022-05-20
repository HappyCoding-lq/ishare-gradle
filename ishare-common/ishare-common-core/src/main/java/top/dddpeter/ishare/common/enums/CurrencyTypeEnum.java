package top.dddpeter.ishare.common.enums;

import top.dddpeter.ishare.common.utils.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 币种类型
 *
 * @author : lzj
 * @version : V1.0
 * @date : 2020/8/7 4:02 下午
 */
public enum CurrencyTypeEnum {

    CNY("CNY","人民币"),
    HKD("HKD","港元"),
    USD("USD","美元"),

    ;

    private final String code;
    private final String value;

    CurrencyTypeEnum(String code, String value){
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


    public static CurrencyTypeEnum getEnumByCode(String code) {
        if(StringUtils.isEmpty(code)){
            return null;
        }
        for(CurrencyTypeEnum em : CurrencyTypeEnum.values()){
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
        for(CurrencyTypeEnum em : CurrencyTypeEnum.values()){
            if(em.getCode().equals(code)){
                return  em.getValue();
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
        for(CurrencyTypeEnum em : CurrencyTypeEnum.values()){
            map.put(em.code, em.value);
        }
        return map;
    }
}
