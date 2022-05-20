package top.dddpeter.ishare.gateway.dynamic;

import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import top.dddpeter.ishare.gateway.domain.GatewayRouteDefine;
import top.dddpeter.ishare.gateway.domain.MyRouteDefinition;
import top.dddpeter.ishare.gateway.mapper.GatewayRouteDefineMapper;
import top.dddpeter.ishare.gateway.util.GatewayRoutePublishUtil;

import javax.annotation.Resource;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * 动态路由实现
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/3/10 11:05 上午
 */
@Component
public class DynamicRoutePublisherAware implements ApplicationEventPublisherAware {

    @Resource
    private RouteDefinitionWriter routeDefinitionWriter;

    private ApplicationEventPublisher publisher;

    @Resource
    private GatewayRoutePublishUtil gatewayRoutePublishUtil;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    public void addRoute(RouteDefinition definition) {
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
    }

    public void deleteRoute(String id) {
        this.routeDefinitionWriter.delete(Mono.just(id)).subscribe();
    }

    public void updateRoute(RouteDefinition definition) {
        this.routeDefinitionWriter.delete(Mono.just(definition.getId())).subscribe();
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
    }

    public void publish(){
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    public void clearRoutes(List<RouteDefinition> routeDefinitions) {
        routeDefinitions.stream().forEach(routeDefinition -> deleteRoute(routeDefinition.getId()));
    }

    public List<MyRouteDefinition> routeDefinitionPublish(GatewayRouteDefineMapper gatewayRouteDefineMapper) {
        List<GatewayRouteDefine> gatewayRouteDefineList = gatewayRouteDefineMapper.selectAll();
        List<MyRouteDefinition> list = new ArrayList<>();
        for (GatewayRouteDefine gatewayRouteDefine : gatewayRouteDefineList) {
            gatewayRouteDefine.setDefaultFieldsValue();
            MyRouteDefinition myRouteDefinition = this.packRouteDefinition(gatewayRouteDefine);
            if(myRouteDefinition == null){
                continue;
            }
            list.add(myRouteDefinition);
        }
        list.forEach(myRouteDefinition -> this.addRoute(myRouteDefinition.getRouteDefinition()));
        return list;
    }

    public MyRouteDefinition packRouteDefinition(GatewayRouteDefine gatewayRouteDefine){
        if (!"YES".equals(gatewayRouteDefine.getIsEnable())) {
            return null;
        }
        try {
            gatewayRoutePublishUtil.publishCheckDefine(gatewayRouteDefine);
        } catch (Exception ex) {
            return null;
        }
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId(gatewayRouteDefine.getRouteId());
        URI uri;
        if (gatewayRouteDefine.getRouteUri().startsWith("http")) {
            uri = UriComponentsBuilder.fromHttpUrl(gatewayRouteDefine.getRouteUri()).build().toUri();
        } else {
            uri = URI.create(gatewayRouteDefine.getRouteUri());
        }
        routeDefinition.setUri(uri);
        routeDefinition.setOrder(gatewayRouteDefine.getRouteOrder());

        //设置断言
        routeDefinition.setPredicates(gatewayRouteDefine.getPredicateDefinition());
        //设置过滤器
        routeDefinition.setFilters(gatewayRouteDefine.getFilterDefinition());

        MyRouteDefinition myRouteDefinition = new MyRouteDefinition();
        myRouteDefinition.setRouteDefinition(routeDefinition);
        myRouteDefinition.setGatewayRouteDefine(gatewayRouteDefine);
        return myRouteDefinition;
    }

}
