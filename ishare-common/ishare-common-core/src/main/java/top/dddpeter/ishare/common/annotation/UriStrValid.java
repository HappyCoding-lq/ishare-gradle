package top.dddpeter.ishare.common.annotation;

import top.dddpeter.ishare.common.validator.UriStrValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * URI str校验注解
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {UriStrValidator.class})
public @interface UriStrValid {

    String message();

    // 作用参考@Validated和@Valid的区别
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean allowEmpty() default false;

}
