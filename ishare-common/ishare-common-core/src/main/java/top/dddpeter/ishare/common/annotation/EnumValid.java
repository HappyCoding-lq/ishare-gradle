package top.dddpeter.ishare.common.annotation;

import top.dddpeter.ishare.common.validator.EnumValidator;
import top.dddpeter.ishare.common.validator.ValidatorConstants;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 枚举校验注解
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/18 2:50 下午
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {EnumValidator.class})
public @interface EnumValid {

    String message();

    // 作用参考@Validated和@Valid的区别
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 目标枚举类
     */
    Class<? extends Enum> target();

    /**
     * 目标枚举类获取code方法 默认为getCode
     */
    String methodName() default ValidatorConstants.ENUM_GET_CODE_DEFAULT_METHOD_NAME;

    /**
     * 是否允许空值 默认不允许
     */
    boolean allowEmpty() default false;

}
