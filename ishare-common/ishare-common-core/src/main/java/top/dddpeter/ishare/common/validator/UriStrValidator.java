package top.dddpeter.ishare.common.validator;

import org.springframework.web.util.UriComponentsBuilder;
import top.dddpeter.ishare.common.annotation.UriStrValid;
import top.dddpeter.ishare.common.utils.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.net.URI;

/**
 * URI str 参数校验处理类
 */
public class UriStrValidator implements ConstraintValidator<UriStrValid, String> {

    // 枚举校验注解
    private UriStrValid constraintAnnotation;

    @Override
    public void initialize(UriStrValid constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            boolean allowEmpty = constraintAnnotation.allowEmpty();
            if (!allowEmpty && StringUtils.isEmpty(value)) {
                return false;
            }
            if (StringUtils.isNotNull(value) && StringUtils.isNotEmpty(value)) {
                if (value.startsWith("http")) {
                    UriComponentsBuilder.fromHttpUrl(value).build().toUri();
                } else {
                    URI uri = UriComponentsBuilder.fromUriString(value).build().toUri();
                    if(StringUtils.isEmpty(uri.getScheme())){
                        return false;
                    }
                }
            }
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return false;
        }
        return true;
    }

}
