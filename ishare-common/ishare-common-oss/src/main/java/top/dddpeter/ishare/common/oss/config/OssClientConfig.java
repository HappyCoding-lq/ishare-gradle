package top.dddpeter.ishare.common.oss.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;


@Configuration
@ConditionalOnBean(name="ossClientProperties")
public class OssClientConfig {
    @Resource
    OssClientProperties ossClientProperties;
    @Bean
    public OSS ossClient(){
        OSS oss = new OSSClientBuilder().build(
                ossClientProperties.getEndpoint(),
                ossClientProperties.getAccessKeyId(),
                ossClientProperties.getAccessKeySecret());
        return  oss;
    }
}
