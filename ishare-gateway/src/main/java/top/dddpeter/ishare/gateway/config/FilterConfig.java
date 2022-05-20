package top.dddpeter.ishare.gateway.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import top.dddpeter.ishare.common.oss.config.OssProxyProperties;
import top.dddpeter.ishare.gateway.filter.*;

import javax.annotation.Resource;

/**
 * @author : ishare
 */
@Configuration
public class FilterConfig {
    @Resource
    OssProxyProperties ossProxyProperties;

    @Bean
    @Order(value = Ordered.HIGHEST_PRECEDENCE)
    public CacheRequestFilter cacheRequestFilter(){
        return new CacheRequestFilter(Ordered.HIGHEST_PRECEDENCE);
    }

    @Bean
    @Order(value = Ordered.HIGHEST_PRECEDENCE+1)
    public RequestLogGlobalFilter logRequestGlobalFilter(){
        return new RequestLogGlobalFilter(Ordered.HIGHEST_PRECEDENCE+1);
    }

    @Bean
    @Order(value = Ordered.HIGHEST_PRECEDENCE+2)
    public AuthFilter authFilter(){
        return new AuthFilter(Ordered.HIGHEST_PRECEDENCE+2);
    }

    @Bean
    @Order(value = Ordered.HIGHEST_PRECEDENCE+3)
    public OpenApiRequestGlobalFilter openApiRequestGlobalFilter(){
        return new OpenApiRequestGlobalFilter(Ordered.HIGHEST_PRECEDENCE+3);
    }

    @Bean
    public OpenApiRequestFilter openApiRequestGatewayFilter(){
        return new OpenApiRequestFilter();
    }

    @Bean
    public ImgCodeFilter imgCodeFilter(){
        return new ImgCodeFilter();
    }

    @Bean
    public OssProxyFilter ossProxyFilter(){ return new OssProxyFilter(ossProxyProperties);}

}
