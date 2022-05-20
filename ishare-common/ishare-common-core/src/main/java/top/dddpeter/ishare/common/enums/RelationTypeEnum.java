package top.dddpeter.ishare.common.enums;

import top.dddpeter.ishare.common.utils.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 关系
 *
 * @author : lzj
 * @version : V1.0
 * @date : 2020/8/7 4:02 下午
 */
public enum RelationTypeEnum {
    /**
     * 关系
     */
    SELF("00","自己"),
    PARENT("01",	"父母"),
    SPOUSE("02",	"配偶"),
    CHILDREN("03",	"子女"),
    GRANDPARENT("04",	"祖父母"),
    GUARDIAN("05",	"监护人"),
    OTHER("06",	"其他"),
    POLICY_SERVICE_STAFF("07",	"保单服务人员"),
    LINEAL_KIN("08",	"直系亲属"),
    GRANDCHILDREN("09",	"祖孙"),
    UNDER_GUARDIANSHIP("10",	"被监护人"),
    MATERNAL_GRANDPARENT("11",	"外祖父母"),
    MATERNAL_GRANDCHILDREN("12",	"外祖孙"),

    ;

    private final String code;
    private final String value;

    RelationTypeEnum(String code, String value){
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


    public static RelationTypeEnum getEnumByCode(String code) {
        if(StringUtils.isEmpty(code)){
            return null;
        }
        for(RelationTypeEnum em : RelationTypeEnum.values()){
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
        for(RelationTypeEnum em : RelationTypeEnum.values()){
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
        for(RelationTypeEnum em : RelationTypeEnum.values()){
            map.put(em.code, em.value);
        }
        return map;
    }
}
