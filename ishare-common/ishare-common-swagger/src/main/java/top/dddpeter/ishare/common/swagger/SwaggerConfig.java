package top.dddpeter.ishare.common.swagger;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * @ClassName SwaggerConfig
 * @PackageName top.dddpeter.ishare.system.config
 * @Description 
 * @Author daiz
 * @Date 2019/8/16 9:57
 * @Version 1.0
 */
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
@Import(BeanValidatorPluginsConfiguration.class)
@Profile({"dev", "dat", "uat"})
public class SwaggerConfig
{
    @Value("${swagger.title:IShare接口文档}")
    private String title;
    @Value("${swagger.description:中台系统}")
    private String description;
    @Value("${swagger.author:横琴人寿}")
    private String author;
    @Value("${swagger.url:https://www.e-hqins.com}")
    private String url;
    @Value("${swagger.email:lijinde@hqins.cn}")
    private String email;
    @Value("${swagger.version:1.0}")
    private String version;
    @Value("${swagger.is.enable}")
    private boolean isEnable;

    @Bean(name="userApi")
    @Order(value = 1)
    public Docket groupRestApi()
    {
        return new Docket(DocumentationType.SWAGGER_2).enable(isEnable)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)).paths(PathSelectors.any())
                .build().securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(Lists.<SecurityScheme> newArrayList(token(), appId(), appSecret()));
    }

    private ApiInfo apiInfo()
    {
        return new ApiInfoBuilder().title(title).description(description)
                .contact(new Contact(author, url, email)).version(version).build();
    }

    private ApiKey token()
    {
        return new ApiKey("TOKEN(TOKEN和（APP_ID,APP_SECRET）方式二选一）", "token", "header");
    }
    private ApiKey appId()
    {
        return new ApiKey("APP_ID（优先）", "app_id", "header");
    }
    private ApiKey appSecret()
    {
        return new ApiKey("APP_SECRET（优先）", "app_secret", "header");
    }

    private SecurityContext securityContext()
    {
        return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.regex("/.*")).build();
    }

    List<SecurityReference> defaultAuth()
    {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("BearerToken", authorizationScopes));
    }
}