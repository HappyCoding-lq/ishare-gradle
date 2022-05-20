package top.dddpeter.ishare.common.enums;

/**
 * 渠道
 *
 * @author huangshuanbao
 * @date 2019-12-24
 */
public enum ChannelEnum
{
    OFFICIAL_WECHAT(0, "H04", "官微"),
    OFFICIAL_WEBSITE(1, "H04", "官网"),
    IB(2, "H06", "I保"),
    DLM(3, "H07", "哆徕咪"),
    OPEN_PLATFORM(4, "H04", "开放平台"),
    IHOME(5, null, "IHome"),
    IBOSS(6, null, "IBoss"),
    EC_API(7, "H04", "电商API对接"),
    EI_API(8, null, "电投API对接"),
    PAGE_API(9, "H04", "页面对接"),
    MIRCO_SHOP(10, null, "微店"),
    EM_WELFARE(11, null, "员工福利平台"),
    CORE(12, null, "核心系统");

    private final Integer code;
    private final String zjxtSource;
    private final String value;

    ChannelEnum(Integer code, String zjxtSource, String value)
    {
        this.code = code;
        this.zjxtSource = zjxtSource;
        this.value = value;
    }

    public Integer getCode()
    {
        return code;
    }

    public String getValue()
    {
        return value;
    }

    public String getZJXTSource()
    {
        return zjxtSource;
    }

    public static ChannelEnum getEnumByCode(Integer code) {
        if(code==null){
            return null;
        }
        for(ChannelEnum em : ChannelEnum.values()){
            if(em.getCode().equals(code)){
                return  em;
            }
        }
        return null;
    }

    public static String getValueByCode(Integer code){
        if(code==null){
            return null;
        }
        for(ChannelEnum em : ChannelEnum.values()){
            if(em.getCode().equals(code)){
                return  em.getValue();
            }
        }
        return null;
    }

    public static String getZJXTSourceByCode(Integer code){
        if(code==null){
            return null;
        }
        for(ChannelEnum em : ChannelEnum.values()){
            if(em.getCode().equals(code)){
                return  em.getZJXTSource();
            }
        }
        return null;
    }
}
