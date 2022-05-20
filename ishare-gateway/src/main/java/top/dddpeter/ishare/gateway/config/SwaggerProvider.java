package top.dddpeter.ishare.gateway.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import top.dddpeter.ishare.gateway.constant.CommonConstants;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SwaggerProvider
 * @PackageName top.dddpeter.ishare.gateway.config
 * @Description 
 * @Author daiz
 * @Date 2019/8/16 10:04
 * @Version 1.0
 */
@Component
@Primary
@AllArgsConstructor
@Slf4j
@Profile({"dev", "dat", "uat"})
public class SwaggerProvider implements SwaggerResourcesProvider {

    @Resource(name="swaggerSourcesRouteLocator")
    private RouteLocator swaggerSourcesRouteLocator;

    @Value("${ishare.swagger.manageServiceIds}")
    private List<String> manageServiceIds;

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        swaggerSourcesRouteLocator.getRoutes().subscribe(route -> {
            String serviceId = route.getUri().toString().substring(5);
            if(manageServiceIds.contains(serviceId)){
                resources.add(swaggerResource(serviceId, "/" + route.getId() +CommonConstants.SWAGGER_DOCS_PATH));
            }
        });
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        log.debug("name:{},location:{}",name,location);
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}