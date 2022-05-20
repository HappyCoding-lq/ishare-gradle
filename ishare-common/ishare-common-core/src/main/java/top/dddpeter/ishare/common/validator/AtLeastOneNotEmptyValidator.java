package top.dddpeter.ishare.common.validator;

import top.dddpeter.ishare.common.annotation.AtLeastOneNotEmpty;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

/**
 * 某些属性至少有一个不能为空校验注解处理器
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/4/21 5:40 下午
 */
public class AtLeastOneNotEmptyValidator implements ConstraintValidator<AtLeastOneNotEmpty, Object> {

    private String[] fields;

    @Override
    public void initialize(AtLeastOneNotEmpty atLeastOneNotEmpty) {
        this.fields = atLeastOneNotEmpty.fields();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        if (object == null) {
            return true;
        }
        try {
            for (String fieldName : fields) {
                Object property = getField(object, fieldName);

                if (property != null && !"".equals(property)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private Object getField(Object object, String fieldName) throws IllegalAccessException {
        if (object == null) {
            return null;
        }
        Class clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals(fieldName)) {
                field.setAccessible(true);
                return field.get(object);
            }
        }
        return null;
    }
}
