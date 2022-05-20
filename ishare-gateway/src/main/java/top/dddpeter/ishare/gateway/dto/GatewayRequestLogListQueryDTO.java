package top.dddpeter.ishare.gateway.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GatewayRequestLogListQueryDTO implements Serializable {

    private static final long serialVersionUID = -6760643056623303310L;

    /** 匹配路由类型 RouteTypeEnum */
    private String routeType;

    /** 微服务ID */
    private String serviceId;

    /** 请求path */
    private String path;

    /** 客户端ID */
    private String appId;

    /** token授权方式的登录用户名 */
    private String loginName;

    /** 请求时间起 */
    private String requestTimeBegin;

    /** 请求时间止 */
    private String requestTimeEnd;

    /** 请求参数 模糊查询 */
    private String requestInfo;

    /** 响应信息 模糊查询 */
    private String responseInfo;

}
