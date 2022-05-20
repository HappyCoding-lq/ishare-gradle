package top.dddpeter.ishare.common.annotation;

import top.dddpeter.ishare.common.validator.BankcardValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 银行卡号校验注解
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/4/7 11:12 上午
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {BankcardValidator.class})
public @interface BankcardValid {

    String message() default "银行卡号格式不正确";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String emptyAlertMessage() default "银行卡号不能为空";

}
