package top.dddpeter.ishare.system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.dddpeter.ishare.system.resolver.LoginUserHandlerResolver;

import javax.annotation.Resource;
import java.util.List;

/**
 * MVC配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer
{
    @Resource
    private LoginUserHandlerResolver loginUserHandlerResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers)
    {
        argumentResolvers.add(loginUserHandlerResolver);
    }
}