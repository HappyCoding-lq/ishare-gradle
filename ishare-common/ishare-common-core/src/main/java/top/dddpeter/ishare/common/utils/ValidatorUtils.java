package top.dddpeter.ishare.common.utils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.Set;

/**
 * hibernate-validator校验工具类
 * 参考文档：http://docs.jboss.org/hibernate/validator/5.4/reference/en-US/html_single/
 */
public class ValidatorUtils {

    private ValidatorUtils() {}

    private static Validator validator;
    static
    {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     * @param object        待校验对象
     * @param groups        待校验的组
     * @throws ValidationException  校验不通过，则报ValidationException异常
     */
    public static void validateEntity(Object object, Class<?> ... groups) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty())
        {
            ConstraintViolation<Object> constraint = constraintViolations.iterator()
                    .next();
            throw new ValidationException(constraint.getMessage());
        }
    }
}
