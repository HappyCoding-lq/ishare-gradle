package top.dddpeter.ishare.common.annotation;

import top.dddpeter.ishare.common.enums.DatePatternEnum;
import top.dddpeter.ishare.common.validator.DateStringValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 时间类型字符串格式校验注解
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {DateStringValidator.class})
public @interface DateStringValid {

    String message();

    // 作用参考@Validated和@Valid的区别
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 允许的格式
     */
    DatePatternEnum[] datePattern();

    /**
     * 是否允许空值 默认不允许
     */
    boolean allowEmpty() default false;

}
