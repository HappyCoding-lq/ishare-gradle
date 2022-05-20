package top.dddpeter.ishare.gateway.service;

import top.dddpeter.ishare.gateway.domain.GatewayRouteDefine;
import top.dddpeter.ishare.gateway.domain.GatewayRouteObject;
import top.dddpeter.ishare.gateway.dto.GenerateApiInfoDTO;
import top.dddpeter.ishare.gateway.vo.ClientInfoVO;
import top.dddpeter.ishare.gateway.vo.GenerateApiInfoVO;

import java.util.List;
import java.util.Map;

/**
 * 网关动态路由管理service层
 */
public interface GatewayRouteService {

    void refreshAll();

    List<GatewayRouteDefine> routeList(GatewayRouteDefine gatewayRouteDefine);

    int routeListCount(GatewayRouteDefine gatewayRouteDefine);

    void enableRoute(String routeId);

    String addRoute(GatewayRouteDefine gatewayRouteDefine);

    void updateRoute(GatewayRouteDefine gatewayRouteDefine);

    void deleteRoute(String routeId);

    void predicateValidCheck(String predicateStr);

    void filterValidCheck(String filterStr);

    List<GatewayRouteObject> routeObjectListQuery(String objectType);

    void reloadRoute(String routeId);

    void enableAndReloadRoute(String routeId);

    String addAndReloadRoute(GatewayRouteDefine gatewayRouteDefine);

    void updateAndReloadRoute(GatewayRouteDefine gatewayRouteDefine);

    void deleteAndReloadRoute(String routeId);

    List<Map<String, String>> getApiPathList();

    List<Map<String, String>> getPathInfoList(String serviceId);

    List<ClientInfoVO> getClientList();

    GenerateApiInfoVO generateApiInfo(GenerateApiInfoDTO dto);

}
