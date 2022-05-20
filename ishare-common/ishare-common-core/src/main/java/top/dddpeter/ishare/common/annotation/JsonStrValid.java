package top.dddpeter.ishare.common.annotation;

import top.dddpeter.ishare.common.enums.JsonTypeEnum;
import top.dddpeter.ishare.common.validator.JsonStrValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * json字符串校验注解
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {JsonStrValidator.class})
public @interface JsonStrValid {

    String message();

    // 作用参考@Validated和@Valid的区别
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    JsonTypeEnum jsonType();

    Class<?> targetClass();

    boolean allowEmpty() default false;

}
