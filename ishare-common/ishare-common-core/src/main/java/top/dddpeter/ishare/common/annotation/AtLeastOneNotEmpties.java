package top.dddpeter.ishare.common.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AtLeastOneNotEmpties {

    AtLeastOneNotEmpty[] value();

}
