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
import top.dddpeter.ishare.gateway.domain.GatewayCrossOriginConfig;
import top.dddpeter.ishare.gateway.service.CrossOriginConfigService;
import top.dddpeter.ishare.gateway.util.HttpUtil;
import top.dddpeter.ishare.gateway.util.RequestCommonUtil;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 网关跨域配置controller
 *
 * @author : huangshuanbao
 */
@RestController
@RequestMapping("/cross")
@Slf4j
public class CrossOriginConfigController extends BaseController {

    @Resource
    private CrossOriginConfigService configService;

    @Resource
    private RequestCommonUtil reuqestCommonUtil;

    @Resource
    private DiscoveryClient discoveryClient;

    @Value("${spring.application.name}")
    private String applicationName;

    @Resource(name="myCrossOriginMap")
    private Map<String, List<GatewayCrossOriginConfig>> myCrossOriginMap;

    @PostMapping("/list")
    public PageResultDTO list(ServerHttpRequest request, @RequestBody(required = false) GatewayCrossOriginConfig crossOriginConfig, PageDomain pageDomain){
        reuqestCommonUtil.checkAuth(request);
        startPage(pageDomain);
        return PageResultDTO.success(configService.configList(crossOriginConfig),
                configService.configListCount(crossOriginConfig), pageDomain);
    }

    @PostMapping("/pathList")
    public ResultDTO pathList(ServerHttpRequest request, @RequestParam(required = false, name = "path") String path){
        reuqestCommonUtil.checkAuth(request);
        return ResultDTO.success(configService.pathList(path));
    }

    @PostMapping("/pathCrossList")
    public ResultDTO pathCrossList(ServerHttpRequest request, @RequestParam("path") String path){
        reuqestCommonUtil.checkAuth(request);
        return ResultDTO.success(configService.pathCrossList(path));
    }

    @PostMapping("/deletePathCross")
    public ResultDTO deletePathCross(ServerHttpRequest request, @RequestParam("path") String path){
        String token = reuqestCommonUtil.checkAuth(request);
        String ids = configService.deletePathCross(path);
        this.remoteReload(token, ids);
        return ResultDTO.success();
    }

    @PostMapping("/refresh")
    public ResultDTO localRefresh(ServerHttpRequest request){
        String token = reuqestCommonUtil.checkAuth(request);
        configService.refreshConfig();
        this.remoteRefresh(token);
        return ResultDTO.success();
    }

    @PostMapping("/discoveryRemoteHostsRefresh")
    public ResultDTO discoveryRemoteHostsRefresh(ServerHttpRequest request){
        log.debug("***************接收到远程刷新CORS配置请求**************");
        reuqestCommonUtil.checkAuth(request);
        configService.refreshConfig();
        log.debug("***************远程刷新CORS配置请求结束**************");
        return ResultDTO.success();
    }

    @PostMapping("/reload")
    public ResultDTO localReload(ServerHttpRequest request, @RequestParam String id){
        String token = reuqestCommonUtil.checkAuth(request);
        configService.reloadConfig(id);
        this.remoteReload(token, id);
        return ResultDTO.success();
    }

    @PostMapping("/discoveryRemoteHostsReload")
    public ResultDTO discoveryRemoteHostsReload(ServerHttpRequest request, @RequestParam String id){
        log.debug("***************接收到远程reload CORS配置请求**************");
        reuqestCommonUtil.checkAuth(request);
        configService.reloadConfig(id);
        log.debug("***************远程reload CORS配置请求结束**************");
        return ResultDTO.success();
    }

    @PostMapping("/add")
    public ResultDTO add(ServerHttpRequest request, @RequestBody @Valid GatewayCrossOriginConfig crossOriginConfig){
        String token = reuqestCommonUtil.checkAuth(request);
        Long id = configService.addConfig(crossOriginConfig);
        this.remoteReload(token, id+"");
        return ResultDTO.success();
    }

    @PostMapping("/update")
    public ResultDTO update(ServerHttpRequest request, @RequestBody @Valid GatewayCrossOriginConfig crossOriginConfig){
        String token = reuqestCommonUtil.checkAuth(request);
        configService.updateConfig(crossOriginConfig);
        this.remoteReload(token, crossOriginConfig.getId()+"");
        return ResultDTO.success();
    }

    @PostMapping("/delete")
    public ResultDTO delete(ServerHttpRequest request, @RequestParam("ids") String ids){
        String token = reuqestCommonUtil.checkAuth(request);
        configService.deleteConfig(ids);
        this.remoteReload(token, ids);
        return ResultDTO.success();
    }

    private void remoteRefresh(String token){
        List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances(applicationName);
        log.debug("discoveryClient serviceHostList:{}", serviceInstanceList.stream().map(ServiceInstance::getHost).collect(Collectors.toList()));
        BasicHeader header = new BasicHeader(Constants.TOKEN, token);
        String localHost = reuqestCommonUtil.getLocalHost();
        serviceInstanceList.forEach(serviceInstance -> {
            if(!serviceInstance.getHost().equals(localHost)) {
                String host = serviceInstance.getHost();
                int port = serviceInstance.getPort();
                String url = "http://" + host + ":" + port + "/cross/discoveryRemoteHostsRefresh";
                try {
                    String res = HttpUtil.httpPost(url, header, null);
                    log.info("remote refresh CORS config response:{}", res);
                } catch (IShareException ex) {
                    log.error("remote refresh CORS config request error", ex);
                }
            }
        });
    }

    private void remoteReload(String token, String id){
        List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances(applicationName);
        log.debug("discoveryClient serviceHostList:{}", serviceInstanceList.stream().map(ServiceInstance::getHost).collect(Collectors.toList()));
        BasicHeader header = new BasicHeader(Constants.TOKEN, token);
        String localHost = reuqestCommonUtil.getLocalHost();
        serviceInstanceList.forEach(serviceInstance -> {
            if(!serviceInstance.getHost().equals(localHost)){
                String host = serviceInstance.getHost();
                int port = serviceInstance.getPort();
                String url = "http://" + host + ":" + port + "/cross/discoveryRemoteHostsReload?id=" + id;
                try {
                    String res = HttpUtil.httpPost(url, header, null);
                    log.info("remote reload CORS config response:{}", res);
                }catch (IShareException ex){
                    log.error("remote reload CORS config request error", ex);
                }
            }
        });
    }

}
