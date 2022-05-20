package top.dddpeter.ishare.common.oss.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component(value = "ossClientProperties")
@ConfigurationProperties(prefix = "oss")
@ConditionalOnProperty( prefix = "oss", value = "endpoint")
public class OssClientProperties {
    private String publicEndpoint;
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    /* not required */
    private String key;
    private String profile;

}
