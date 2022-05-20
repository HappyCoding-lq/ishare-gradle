package top.dddpeter.ishare.common.enums;

/**
 * 商品类别
 * 
 * @author huangshuanbao
 * @date 2019-12-24
 */
public enum GoodsTypeEnum
{
    TD("TD", "投单"),
    SW("SW", "实物"),
    BQ("BQ", "保全"),
    LP("LP", "理赔");

    private final String code;
    private final String value;

    GoodsTypeEnum(String code, String value)
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

    public static GoodsTypeEnum getEnumByCode(String code) {
        if(code==null || code.length()<=0){
            return null;
        }
        for(GoodsTypeEnum em : GoodsTypeEnum.values()){
            if(em.getCode().equals(code)){
                return  em;
            }
        }
        return null;
    }

    public static String getValueByCode(String code){
        if(code==null || code.length()<=0){
            return null;
        }
        for(GoodsTypeEnum em : GoodsTypeEnum.values()){
            if(em.getCode().equals(code)){
                return  em.getValue();
            }
        }
        return null;
    }
}
