package top.dddpeter.ishare.gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import top.dddpeter.ishare.common.constant.Constants;
import top.dddpeter.ishare.common.core.controller.BaseController;
import top.dddpeter.ishare.common.core.page.PageDomain;
import top.dddpeter.ishare.common.dto.PageResultDTO;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.common.exception.IShareException;
import top.dddpeter.ishare.gateway.domain.GatewayRouteDefine;
import top.dddpeter.ishare.gateway.dto.GenerateApiInfoDTO;
import top.dddpeter.ishare.gateway.service.GatewayRouteService;
import top.dddpeter.ishare.gateway.util.HttpUtil;
import top.dddpeter.ishare.gateway.util.RequestCommonUtil;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 动态路由Controller
 */
@RestController
@RequestMapping("/route")
@Slf4j
public class DynamicRouteController extends BaseController {

    @Resource
    private GatewayRouteService gatewayRouteService;

    @Resource
    private RequestCommonUtil reuqestCommonUtil;

    @Resource
    private DiscoveryClient discoveryClient;

    @Value("${spring.application.name}")
    private String applicationName;

    @GetMapping("/refresh")
    public ResultDTO localRefresh(ServerHttpRequest request) {
        String token = reuqestCommonUtil.checkAuth(request);
        gatewayRouteService.refreshAll();
        this.remoteRefresh(token);
        return ResultDTO.success("执行成功");
    }

    @GetMapping("/discoveryRemoteHostsRefresh")
    public ResultDTO discoveryRemoteHostsRefresh(ServerHttpRequest request) {
        log.debug("***************接收到远程刷新网关路由配置请求**************");
        reuqestCommonUtil.checkAuth(request);
        gatewayRouteService.refreshAll();
        log.debug("***************远程刷新网关路由配置请求结束**************");
        return ResultDTO.success("执行成功");
    }

    @PostMapping("/list")
    public PageResultDTO list(ServerHttpRequest request, @RequestBody GatewayRouteDefine gatewayRouteDefine, PageDomain pageDomain){
        reuqestCommonUtil.checkAuth(request);
        startPage(pageDomain);
        return PageResultDTO.success(gatewayRouteService.routeList(gatewayRouteDefine),
                gatewayRouteService.routeListCount(gatewayRouteDefine), pageDomain);
    }

    @PostMapping("/enable")
    public ResultDTO enable(ServerHttpRequest request, @RequestParam("routeId") String routeId){
        reuqestCommonUtil.checkAuth(request);
        gatewayRouteService.enableRoute(routeId);
        return ResultDTO.success();
    }

    @PostMapping("/add")
    public ResultDTO add(ServerHttpRequest request, @RequestBody GatewayRouteDefine gatewayRouteDefine){
        reuqestCommonUtil.checkAuth(request);
        gatewayRouteService.addRoute(gatewayRouteDefine);
        return ResultDTO.success();
    }

    @PostMapping("/update")
    public ResultDTO update(ServerHttpRequest request, @RequestBody @Valid GatewayRouteDefine gatewayRouteDefine){
        reuqestCommonUtil.checkAuth(request);
        gatewayRouteService.updateRoute(gatewayRouteDefine);
        return ResultDTO.success();
    }

    @PostMapping("/delete")
    public ResultDTO delete(ServerHttpRequest request, @RequestParam("routeId") String routeId){
        reuqestCommonUtil.checkAuth(request);
        gatewayRouteService.deleteRoute(routeId);
        return ResultDTO.success();
    }

    @PostMapping("/predicateValidCheck")
    public ResultDTO predicateValidCheck(ServerHttpRequest request, @RequestParam("predicateStr") String predicateStr){
        reuqestCommonUtil.checkAuth(request);
        gatewayRouteService.predicateValidCheck(predicateStr);
        return ResultDTO.success();
    }

    @PostMapping("/filterValidCheck")
    public ResultDTO filterValidCheck(ServerHttpRequest request, @RequestParam("filterStr") String filterStr){
        reuqestCommonUtil.checkAuth(request);
        gatewayRouteService.filterValidCheck(filterStr);
        return ResultDTO.success();
    }

    @PostMapping("/objectList")
    public ResultDTO routeObjectListQuery(ServerHttpRequest request, @RequestParam("objectType") String objectType){
        reuqestCommonUtil.checkAuth(request);
        return ResultDTO.success(gatewayRouteService.routeObjectListQuery(objectType));
    }

    @PostMapping("/reload")
    public ResultDTO localReload(ServerHttpRequest request, @RequestParam("routeId") String routeId){
        String token = reuqestCommonUtil.checkAuth(request);
        gatewayRouteService.reloadRoute(routeId);
        this.remoteReload(token, routeId);
        return ResultDTO.success();
    }

    @PostMapping("/discoveryRemoteHostsReload")
    public ResultDTO discoveryRemoteHostsReload(ServerHttpRequest request, @RequestParam("routeId") String routeId){
        log.debug("***************接收到远程reload网关路由配置请求**************");
        reuqestCommonUtil.checkAuth(request);
        gatewayRouteService.reloadRoute(routeId);
        log.debug("***************远程reload网关路由配置请求结束**************");
        return ResultDTO.success();
    }

    @PostMapping("/enableAndReload")
    public ResultDTO enableAndReload(ServerHttpRequest request, @RequestParam("routeId") String routeId){
        String token = reuqestCommonUtil.checkAuth(request);
        gatewayRouteService.enableAndReloadRoute(routeId);
        this.remoteReload(token, routeId);
        return ResultDTO.success();
    }

    @PostMapping("/addAndReload")
    public ResultDTO addAndReload(ServerHttpRequest request, @RequestBody GatewayRouteDefine gatewayRouteDefine){
        String token = reuqestCommonUtil.checkAuth(request);
        String routeId = gatewayRouteService.addAndReloadRoute(gatewayRouteDefine);
        this.remoteReload(token, routeId);
        return ResultDTO.success();
    }

    @PostMapping("/updateAndReload")
    public ResultDTO updateAndReload(ServerHttpRequest request, @RequestBody @Valid GatewayRouteDefine gatewayRouteDefine){
        String token = reuqestCommonUtil.checkAuth(request);
        gatewayRouteService.updateAndReloadRoute(gatewayRouteDefine);
        this.remoteReload(token, gatewayRouteDefine.getRouteId());
        return ResultDTO.success();
    }

    @PostMapping("/deleteAndReload")
    public ResultDTO deleteAndReload(ServerHttpRequest request, @RequestParam("routeId") String routeId){
        String token = reuqestCommonUtil.checkAuth(request);
        gatewayRouteService.deleteAndReloadRoute(routeId);
        this.remoteReload(token, routeId);
        return ResultDTO.success();
    }

    @GetMapping("/serviceIdList")
    public ResultDTO getServiceIdList(ServerHttpRequest request){
        reuqestCommonUtil.checkAuth(request);
        List<String> list = discoveryClient.getServices();
        list.sort(String::compareTo);
        return ResultDTO.success(list);
    }

    @GetMapping("/apiPathList")
    public ResultDTO getApiPathList(ServerHttpRequest request){
        reuqestCommonUtil.checkAuth(request);
        return ResultDTO.success(gatewayRouteService.getApiPathList());
    }

    @GetMapping("/pathInfoList")
    public ResultDTO getPathInfoList(ServerHttpRequest request, @RequestParam(value = "serviceId", required = false) String serviceId){
        reuqestCommonUtil.checkAuth(request);
        return ResultDTO.success(gatewayRouteService.getPathInfoList(serviceId));
    }

    @GetMapping("/clientList")
    public ResultDTO getClientList(ServerHttpRequest request){
        reuqestCommonUtil.checkAuth(request);
        return ResultDTO.success(gatewayRouteService.getClientList());
    }

    @PostMapping("/generateApiInfo")
    public ResultDTO generateApiInfo(ServerHttpRequest request, @Valid @RequestBody GenerateApiInfoDTO dto){
        reuqestCommonUtil.checkAuth(request);
        if(!discoveryClient.getServices().contains(dto.getServiceId())){
            return ResultDTO.failure("微服务名传值不正确");
        }
        return ResultDTO.success(gatewayRouteService.generateApiInfo(dto));
    }

    private void remoteRefresh(String token){
        List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances(applicationName);
        log.debug("discoveryClient serviceHostList:{}", serviceInstanceList.stream().map(ServiceInstance::getHost).collect(Collectors.toList()));
        BasicHeader header = new BasicHeader(Constants.TOKEN, token);
        String localHost = reuqestCommonUtil.getLocalHost();
        serviceInstanceList.forEach(serviceInstance -> {
            if(!serviceInstance.getHost().equals(localHost)){
                String host = serviceInstance.getHost();
                int port = serviceInstance.getPort();
                String url = "http://" + host + ":" + port + "/route/discoveryRemoteHostsRefresh";
                try {
                    String res = HttpUtil.httpGet(url, header);
                    log.info("remote refresh route response:{}", res);
                }catch (IShareException ex){
                    log.error("remote refresh route request error", ex);
                }
            }
        });
    }

    private void remoteReload(String token, String routeId) {
        List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances(applicationName);
        log.debug("discoveryClient serviceHostList:{}", serviceInstanceList.stream().map(ServiceInstance::getHost).collect(Collectors.toList()));
        BasicHeader header = new BasicHeader(Constants.TOKEN, token);
        String localHost = reuqestCommonUtil.getLocalHost();
        serviceInstanceList.forEach(serviceInstance -> {
            if(!serviceInstance.getHost().equals(localHost)){
                String host = serviceInstance.getHost();
                int port = serviceInstance.getPort();
                String url = "http://" + host + ":" + port + "/route/discoveryRemoteHostsReload?routeId=" + routeId;
                try {
                    String res = HttpUtil.httpPost(url, header, null);
                    log.info("remote reload route response:{}", res);
                }catch (IShareException ex){
                    log.error("remote reload route request error", ex);
                }
            }
        });
    }

}
