package top.dddpeter.ishare.common.validator;

import lombok.extern.slf4j.Slf4j;
import top.dddpeter.ishare.common.annotation.BankcardValid;
import top.dddpeter.ishare.common.utils.CommonValueCheckUtils;
import top.dddpeter.ishare.common.utils.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 银行卡号参数校验处理类
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/4/7 11:14 上午
 */
@Slf4j
public class BankcardValidator implements ConstraintValidator<BankcardValid, String> {

    private BankcardValid constraintAnnotation;

    @Override
    public void initialize(BankcardValid constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(constraintAnnotation.emptyAlertMessage()).addConstraintViolation();
            return false;
        }
        return CommonValueCheckUtils.checkBankcard(value);
    }

}
