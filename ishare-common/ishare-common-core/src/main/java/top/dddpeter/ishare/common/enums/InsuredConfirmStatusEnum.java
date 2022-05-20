package top.dddpeter.ishare.common.enums;

import top.dddpeter.ishare.common.utils.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 被保人确认状态
 *
 * @author : lzj
 * @version : V1.0
 * @date : 2020/8/7 4:02 下午
 */
public enum InsuredConfirmStatusEnum {

    WAIT("WAIT","待确认"),
    EXAMINE("EXAMINE","审核中"),
    SUCCESS("SUCCESS","已确认"),
    FAILED("FAILED","审核失败"),



            ;

    private final String code;
    private final String value;

    InsuredConfirmStatusEnum(String code, String value){
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


    public static InsuredConfirmStatusEnum getEnumByCode(String code) {
        if(StringUtils.isEmpty(code)){
            return null;
        }
        for(InsuredConfirmStatusEnum em : InsuredConfirmStatusEnum.values()){
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
        for(InsuredConfirmStatusEnum em : InsuredConfirmStatusEnum.values()){
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
        for(InsuredConfirmStatusEnum em : InsuredConfirmStatusEnum.values()){
            map.put(em.code, em.value);
        }
        return map;
    }
}
