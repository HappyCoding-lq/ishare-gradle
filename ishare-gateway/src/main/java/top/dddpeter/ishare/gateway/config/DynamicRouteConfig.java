package top.dddpeter.ishare.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.dddpeter.ishare.gateway.domain.MyRouteDefinition;
import top.dddpeter.ishare.gateway.dynamic.DynamicRoutePublisherAware;
import top.dddpeter.ishare.gateway.mapper.GatewayRouteDefineMapper;

import java.util.List;

/**
 * 动态路由加载配置
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/3/10 11:48 上午
 */
@Configuration
@Slf4j
public class DynamicRouteConfig {

    @Bean("myRouteDefinitionList")
    public List<MyRouteDefinition> routeDefinitionInit(@Autowired GatewayRouteDefineMapper gatewayRouteDefineMapper, @Autowired DynamicRoutePublisherAware aware){
        log.info("*****************动态路由初始化开始*****************");
        List<MyRouteDefinition> myRouteDefinitionList = aware.routeDefinitionPublish(gatewayRouteDefineMapper);
        aware.publish();
        log.info("*****************动态路由初始化结束*****************");
        log.debug("route define list: {}", myRouteDefinitionList);
        return myRouteDefinitionList;
    }

}
