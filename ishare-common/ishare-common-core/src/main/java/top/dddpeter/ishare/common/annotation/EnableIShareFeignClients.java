package top.dddpeter.ishare.common.annotation;

import org.springframework.cloud.openfeign.EnableFeignClients;

import java.lang.annotation.*;


/**
 * IShare自定义通用EnableFeignClients注解
 *
 * @author huangshuanbao
 * @date 2020-01-20
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableFeignClients
public @interface EnableIShareFeignClients
{
    String[] value() default {};

    String[] basePackages() default {"top.dddpeter.ishare"};

    Class<?>[] basePackageClasses() default {};

    Class<?>[] defaultConfiguration() default {};

    Class<?>[] clients() default {};
}