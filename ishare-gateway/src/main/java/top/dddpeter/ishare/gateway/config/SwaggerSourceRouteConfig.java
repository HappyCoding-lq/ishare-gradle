package top.dddpeter.ishare.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import top.dddpeter.ishare.gateway.constant.CommonConstants;

import java.util.List;
import java.util.UUID;

/**
 * swagger source route 配置
 *
 * @author : huangshuanbao
 */
@Configuration
@Slf4j
@Profile({"dev", "dat", "uat"})//屏蔽生产
public class SwaggerSourceRouteConfig {

    @Value("${ishare.swagger.manageServiceIds}")
    private List<String> manageServiceIds;

    @Bean("swaggerSourcesRouteLocator")
    public RouteLocator swaggerSourcesRouteLocator(RouteLocatorBuilder routeLocatorBuilder, DiscoveryClient discoveryClient) {
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        List<String> serviceList = discoveryClient.getServices();
        serviceList.sort(String::compareTo);
        serviceList.forEach(serviceId-> {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            if(manageServiceIds.contains(serviceId)){
                routes.route(uuid,
                        r ->r.path("/" + uuid + "/**")
                                .filters(f->f.stripPrefix(1))
                                .uri(CommonConstants.SERVICE_URI_PREFIX+serviceId));
            }
        });
        return routes.build();
    }

}
