package top.dddpeter.ishare.gateway.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * 网关请求日志
 *
 * @author huangshuanbao
 *
 */
@Data
@Document("gateway_request_log")
public class GatewayRequestLog implements Serializable {

    private static final long serialVersionUID = -4199210189270046164L;

    /** 请求id 主键*/
    @Id
    private String logId;

    /** 请求path */
    private String path;

    /** User-Agent */
    private String userAgent;

    /** 匹配路由类型 RouteTypeEnum */
    private String routeType;

    /** 匹配路由描述 */
    private String routeDesc;

    /** 处理的微服务 serviceId */
    private String serviceId;

    /** 客户端ID */
    private String appId;

    /** login_name */
    private String loginName;

    /** 接口编码 */
    private String apiTransCode;

    /** 请求参数 */
    private String requestInfo;

    /** 响应信息 */
    private String responseInfo;

    /** 请求方法 */
    private String requestMethod;

    /** 请求方式 */
    private String requestContentType;

    /** ip */
    private String requestIp;

    /** 地点 */
    private String requestLocation;

    /** 请求时间 */
    @Indexed(direction= IndexDirection.DESCENDING)
    private String requestTime;

    /** 响应时间 */
    private String responseTime;

    /** 响应耗时 毫秒数 */
    private Long accessTime;

}
