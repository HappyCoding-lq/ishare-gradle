package top.dddpeter.ishare.common.validator;

import lombok.extern.slf4j.Slf4j;
import top.dddpeter.ishare.common.annotation.DateStringValid;
import top.dddpeter.ishare.common.enums.DatePatternEnum;
import top.dddpeter.ishare.common.utils.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;

import static top.dddpeter.ishare.common.utils.DateUtils.parseDate;


/**
 * 时间类型字符串格式校验处理类
 */
@Slf4j
public class DateStringValidator implements ConstraintValidator<DateStringValid, String> {

    // 校验注解
    private DateStringValid constraintAnnotation;

    @Override
    public void initialize(DateStringValid constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(!constraintAnnotation.allowEmpty() && StringUtils.isEmpty(value)){
            return false;
        }
        if(StringUtils.isNotEmpty(value)){
            DatePatternEnum[] enums = constraintAnnotation.datePattern();
            try {
                parseDate(value, DatePatternEnum.toCodeArray(enums));
            } catch (ParseException e) {
                return false;
            }
        }
        return true;
    }

}
