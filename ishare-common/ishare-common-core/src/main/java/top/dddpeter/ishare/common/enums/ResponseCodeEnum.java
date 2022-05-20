package top.dddpeter.ishare.common.enums;

/**
 * ResultDTO响应状态码枚举
 * 
 * @author huangshuanbao
 * @date 2019-12-24
 */
public enum ResponseCodeEnum
{
    //通用状态码
    SUCCESS("200", "处理成功"),
    PARAM_WRONG("400", "参数检查未通过"),
    UNAUTHORIZED("401", "授权异常"),
    FORBIDDEN("403","拒绝访问"),
    ERROR("500", "处理失败"),

    //订单中心相关状态码(1开头的四位数)
    ORDER_CENTER_SUCCESS("1000","处理成功"),
    MAIN_ORDER_ID_NULL_ERROR("1001", "主订单号不能为空"),

    //支付中心相关状态码(2开头的四位数)
    PAY_CENTER_SUCCESS("2000","处理成功");

    private final String code;
    private final String messsage;

    ResponseCodeEnum(String code, String messsage)
    {
        this.code = code;
        this.messsage = messsage;
    }

    public String getCode()
    {
        return code;
    }

    public String getMessage()
    {
        return messsage;
    }
}
