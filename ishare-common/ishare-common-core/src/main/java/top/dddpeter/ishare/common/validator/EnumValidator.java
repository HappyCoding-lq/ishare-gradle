package top.dddpeter.ishare.common.validator;

import lombok.extern.slf4j.Slf4j;
import top.dddpeter.ishare.common.annotation.EnumValid;
import top.dddpeter.ishare.common.exception.IShareException;
import top.dddpeter.ishare.common.utils.EnumUtils;
import top.dddpeter.ishare.common.utils.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;


/**
 * 枚举参数校验处理类
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/18 2:58 下午
 */
@Slf4j
public class EnumValidator implements ConstraintValidator<EnumValid, Serializable> {

    // 枚举校验注解
    private EnumValid constraintAnnotation;

    @Override
    public void initialize(EnumValid constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Serializable value, ConstraintValidatorContext context) {
        try {
            Class<? extends Enum> emClass = constraintAnnotation.target();
            String methodName = constraintAnnotation.methodName();
            boolean allowEmpty = constraintAnnotation.allowEmpty();
            if (!allowEmpty || StringUtils.isNotNull(value) && StringUtils.isNotEmpty(value.toString())) {
                return EnumUtils.containsCode(emClass, methodName, value);
            } else {
                return true;
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new IShareException("EnumValidator call isValid() method exception.");
        }
    }

}
