package top.dddpeter.ishare.common.encryption.annotation;

import java.lang.annotation.*;

/**
 * 加解密注解
 * 
 * <p>加了此注解的接口将进行数据解密和数据加密操作<p>
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/24 1:14 下午
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DecryptAndEncrypt {

	String value() default "";
	
}