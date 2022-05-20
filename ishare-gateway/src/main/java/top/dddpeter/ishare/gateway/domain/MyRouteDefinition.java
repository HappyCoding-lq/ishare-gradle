package top.dddpeter.ishare.gateway.domain;

import lombok.Data;
import org.springframework.cloud.gateway.route.RouteDefinition;

/**
 * @author : huangshuanbao
 */
@Data
public class MyRouteDefinition {

    private RouteDefinition routeDefinition;

    private GatewayRouteDefine gatewayRouteDefine;

}
