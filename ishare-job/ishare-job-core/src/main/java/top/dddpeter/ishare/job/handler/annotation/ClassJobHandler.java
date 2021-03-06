package top.dddpeter.ishare.job.handler.annotation;

import java.lang.annotation.*;

/**
 * annotation for job handler
 * @author 2016-5-17 21:06:49
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ClassJobHandler {

    String value() default "";
    
}
