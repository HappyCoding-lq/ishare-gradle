package top.dddpeter.ishare.common.encryption.annotation;

import java.lang.annotation.*;

/**
 * 忽略加密注解
 * 
 * <p>加了此注解的接口将不进行数据加密操作<p>
 * <p>适用于全局开启加解密操作，但是想忽略某些接口场景<p>
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/24 1:14 下午
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EncryptIgnore {

	String value() default "";
	
}
