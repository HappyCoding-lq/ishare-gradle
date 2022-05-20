package top.dddpeter.ishare.job.admin.controller.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

/**
 * web mvc config
 *
 * @author hqins 2019-12-10
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Resource
    private PermissionInterceptor permissionInterceptor;
    @Resource
    private CookieInterceptor cookieInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(permissionInterceptor).addPathPatterns("/**");
        registry.addInterceptor(cookieInterceptor).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

}