package top.dddpeter.ishare.gateway.util;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import top.dddpeter.ishare.gateway.domain.GatewayRouteDefine;
import top.dddpeter.ishare.gateway.domain.MyRouteDefinition;

import javax.annotation.Resource;
import java.util.List;

/**
 * 自定义路由工具类
 *
 * @author : huangshuanbao
 */
@Component
public class GatewayRouteDefineUtil {

    @Resource
    private List<MyRouteDefinition> myRouteDefinitionList;

    /**
     * 根据网关路由信息获取自定义路由信息
     */
    public GatewayRouteDefine getDefine(Route route) {
        return this.getDefine(route.getId());
    }

    /**
     * 根据路由ID获取自定义路由信息
     */
    public GatewayRouteDefine getDefine(String routeId) {
        if(myRouteDefinitionList == null || myRouteDefinitionList.isEmpty()){
            return null;
        }
        MyRouteDefinition myRouteDefinition = myRouteDefinitionList.stream().filter(everyOne -> everyOne.getGatewayRouteDefine()!=null && everyOne.getGatewayRouteDefine().getRouteId().equals(routeId)).findAny().orElse(null);
        return myRouteDefinition==null? null : myRouteDefinition.getGatewayRouteDefine();
    }

    /**
     * 根据请求获取匹配到的路由信息
     */
    public GatewayRouteDefine getMatchGatewayRouteDefine(ServerWebExchange exchange) {
        Route route = (Route) exchange.getAttributes().get(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        return this.getDefine(route);
    }

}
