package top.dddpeter.ishare.common.encryption.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import top.dddpeter.ishare.common.encryption.algorithm.DefaultAesEncryptAlgorithm;
import top.dddpeter.ishare.common.encryption.algorithm.DefaultRsaEncryptAlgorithm;
import top.dddpeter.ishare.common.encryption.algorithm.EncryptAlgorithm;
import top.dddpeter.ishare.common.encryption.core.EncryptionConfig;
import top.dddpeter.ishare.common.encryption.core.EncryptionFilter;
import top.dddpeter.ishare.common.encryption.enums.EncryptTypeEnum;
import top.dddpeter.ishare.common.encryption.init.ApiEncryptDataInit;


/**
 * 加解密自动配置
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/24 1:14 下午
 */
@EnableConfigurationProperties(EncryptionConfig.class)
public class EncryptAutoConfiguration {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
    public FilterRegistrationBean filterRegistration(@Autowired EncryptionConfig encryptionConfig,
													 @Autowired(required = false) EncryptAlgorithm encryptAlgorithm) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        if (encryptAlgorithm != null) {
        	registration.setFilter(new EncryptionFilter(encryptionConfig, encryptAlgorithm));
		} else if(EncryptTypeEnum.RSA.getCode().equals(encryptionConfig.getEncryptType())){
			registration.setFilter(new EncryptionFilter(encryptionConfig, new DefaultRsaEncryptAlgorithm()));
		} else if(EncryptTypeEnum.AES.getCode().equals(encryptionConfig.getEncryptType())){
			registration.setFilter(new EncryptionFilter(encryptionConfig, new DefaultAesEncryptAlgorithm()));
		}
        registration.addUrlPatterns(encryptionConfig.getUrlPatterns());
        registration.setName("EncryptionFilter");
        registration.setOrder(encryptionConfig.getOrder());
        return registration;
    }
	
	@Bean
	public ApiEncryptDataInit apiEncryptDataInit() {
		return new ApiEncryptDataInit();
	}
}
