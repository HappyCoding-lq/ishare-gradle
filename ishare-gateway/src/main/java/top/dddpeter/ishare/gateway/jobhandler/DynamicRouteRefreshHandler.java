package top.dddpeter.ishare.gateway.jobhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.dddpeter.ishare.gateway.domain.MyRouteDefinition;
import top.dddpeter.ishare.gateway.dynamic.DynamicRoutePublisherAware;
import top.dddpeter.ishare.gateway.mapper.GatewayRouteDefineMapper;
import top.dddpeter.ishare.job.biz.model.ReturnT;
import top.dddpeter.ishare.job.handler.IJobHandler;
import top.dddpeter.ishare.job.handler.annotation.ClassJobHandler;
import top.dddpeter.ishare.job.log.JobLogger;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 网关路由定时刷新任务调度job handler
 */
@Component
@ClassJobHandler(value = "dynamic_route_refresh_handler")
@Slf4j
public class DynamicRouteRefreshHandler extends IJobHandler {

    @Resource
    private List<MyRouteDefinition> myRouteDefinitionList;

    @Resource
    private GatewayRouteDefineMapper gatewayRouteDefineMapper;

    @Resource
    private DynamicRoutePublisherAware aware;

    @Override
    public ReturnT<String> execute(String paramStr) {
        log.info("*****************动态路由定时刷新任务开始执行*****************");
        JobLogger.log("======>开始执行动态路由定时刷新调度任务, 任务参数：{}<======", paramStr);
        if(myRouteDefinitionList == null){
            return SUCCESS;
        }
        if(!myRouteDefinitionList.isEmpty()){
            aware.clearRoutes(myRouteDefinitionList.stream().map(MyRouteDefinition::getRouteDefinition).collect(Collectors.toList()));
            myRouteDefinitionList.clear();
        }
        myRouteDefinitionList.addAll(aware.routeDefinitionPublish(gatewayRouteDefineMapper));
        log.debug("route define list: {}", myRouteDefinitionList);
        log.info("*****************动态路由定时刷新任务结束执行*****************");
        JobLogger.log("======>动态路由定时刷新调度任务执行完毕<======");
        return SUCCESS;
    }

}
