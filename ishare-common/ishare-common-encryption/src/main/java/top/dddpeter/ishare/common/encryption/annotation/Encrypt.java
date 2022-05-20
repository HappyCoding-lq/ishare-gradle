package top.dddpeter.ishare.common.encryption.annotation;

import java.lang.annotation.*;

/**
 * 加密注解
 * 
 * <p>加了此注解的接口将进行数据加密操作<p>
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/24 1:14 下午
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Encrypt {

	String value() default "";
	
}