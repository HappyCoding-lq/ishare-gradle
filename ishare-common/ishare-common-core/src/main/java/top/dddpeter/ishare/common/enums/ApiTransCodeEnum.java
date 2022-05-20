package top.dddpeter.ishare.common.enums;

import top.dddpeter.ishare.common.utils.StringUtils;

/**
 * api接口交易编码枚举
 *
 * @author huangshuanbao
 */
public enum ApiTransCodeEnum {

    /** 支付中心 */
    PAY_CENTER_GET_PAY_URL("PAY_CENTER_GET_PAY_URL", "获取支付页面地址接口"),
    PAY_CENTER_PAY_NOTIFY("PAY_CENTER_PAY_NOTIFY", "支付后台通知接口"),
    PAY_CENTER_PAY_STATUS_QUERY("PAY_CENTER_PAY_STATUS_QUERY", "支付状态查询接口"),

    /** 用户中心 */
    USER_CENTER_VISITOR_TRACK_NEW("USER_CENTER_VISITOR_TRACK_NEW",  "用户中心访客跟踪记录保存接口"),
    USER_CENTER_VISITOR_TRACK_END("USER_CENTER_VISITOR_TRACK_END",  "用户中心访客跟踪访问结束保存接口"),
    ISHARE_USER_INSUREDCONFIRM_PUSHDATA("ISHARE-USER-INSUREDCONFIRM-PUSHDATA",  "被保人确认数据推送接口"),
    ISHARE_USER_INSUREDCONFIRM_GETSTATUS("ISHARE-USER-INSUREDCONFIRM-GETSTATUS",  "被保人确认状态查询推送接口");

    private final String code;
    private final String value;

    ApiTransCodeEnum(String code, String value){
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

    public static ApiTransCodeEnum getEnumByCode(String code) {
        if(StringUtils.isEmpty(code)){
            return null;
        }
        for(ApiTransCodeEnum em : ApiTransCodeEnum.values()){
            if(em.getCode().equals(code)){
                return  em;
            }
        }
        return null;
    }

}
