package top.dddpeter.ishare.gateway.vo;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
public class GatewayRequestLogListQueryVO implements Serializable {

    private static final long serialVersionUID = 7406300455214945290L;

    /** 请求id */
    @Id
    private String logId;

    /** 请求path */
    private String path;

    /** 匹配路由类型 RouteType */
    private String routeType;

    /** 匹配路由描述 */
    private String routeDesc;

    /** 微服务ID */
    private String serviceId;

    /** app_id */
    private String appId;

    /** login_name */
    private String loginName;

    /** 接口编码 */
    private String apiTransCode;

    /** 请求方法 */
    private String requestMethod;

    /** 请求方式 */
    private String requestContentType;

    /** ip */
    private String requestIp;

    /** 地点 */
    private String requestLocation;

    /** 请求时间 */
    private String requestTime;

    /** 响应时间 */
    private String responseTime;

    /** 响应耗时 毫秒数 */
    private Long accessTime;

}
