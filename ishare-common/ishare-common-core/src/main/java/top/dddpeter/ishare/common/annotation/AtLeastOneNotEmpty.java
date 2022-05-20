package top.dddpeter.ishare.common.annotation;

import top.dddpeter.ishare.common.validator.AtLeastOneNotEmptyValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 某些属性至少有一个不能为空校验注解
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/4/21 5:37 下午
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {AtLeastOneNotEmptyValidator.class})
@Repeatable(AtLeastOneNotEmpties.class)
public @interface AtLeastOneNotEmpty {

    String message() default "{至少有一个属性不能为空}";

    // 作用参考@Validated和@Valid的区别
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] fields();

}
