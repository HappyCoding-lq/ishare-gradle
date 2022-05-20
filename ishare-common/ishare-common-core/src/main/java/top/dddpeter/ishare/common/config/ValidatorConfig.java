package top.dddpeter.ishare.common.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * 注解校验校验模式配置
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/5 4:55 下午
 */
@Configuration
public class ValidatorConfig {

    @Bean
    public Validator validator(){
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory();
        return validatorFactory.getValidator();
    }

}
