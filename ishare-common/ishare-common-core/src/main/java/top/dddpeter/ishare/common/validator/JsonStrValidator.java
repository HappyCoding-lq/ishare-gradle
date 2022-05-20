package top.dddpeter.ishare.common.validator;

import com.alibaba.fastjson.JSON;
import top.dddpeter.ishare.common.annotation.JsonStrValid;
import top.dddpeter.ishare.common.enums.JsonTypeEnum;
import top.dddpeter.ishare.common.utils.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * json字符串参数格式校验器
 */
public class JsonStrValidator implements ConstraintValidator<JsonStrValid, String> {

    private JsonTypeEnum jsonTypeEnum;

    private Class<?> targetClass;

    private boolean allowEmpty;
    Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");
    @Override
    public void initialize(JsonStrValid constraintAnnotation) {
        this.jsonTypeEnum = constraintAnnotation.jsonType();
        this.allowEmpty = constraintAnnotation.allowEmpty();
        this.targetClass = constraintAnnotation.targetClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (!allowEmpty && StringUtils.isEmpty(value)) {
            return false;
        }

        if(StringUtils.isNotEmpty(value)){

            Matcher m = pattern.matcher(value);
            value = m.replaceAll("");
        }

        if (!allowEmpty && ("{}".equals(value.replace(" ", ""))
                || "[]".equals(value.replace(" ", ""))
                || "[{}]".equals(value.replace(" ", "")))) {
            return false;
        }

        if (StringUtils.isNotNull(value) && StringUtils.isNotEmpty(value)) {
            try {
                switch (jsonTypeEnum) {
                    case OBJECT:
                        if (Map.class.isAssignableFrom(targetClass)) {
                            JSON.parseObject(value, Map.class);
                        } else {
                            JSON.parseObject(value, targetClass);
                        }
                        break;
                    case ARRAY:
                        JSON.parseArray(value, targetClass);
                        break;
                    default:
                        return false;
                }
            } catch (Exception ex) {
                return false;
            }
        }
        return true;

    }

}
