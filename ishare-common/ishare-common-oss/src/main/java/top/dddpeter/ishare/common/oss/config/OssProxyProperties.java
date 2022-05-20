package top.dddpeter.ishare.common.oss.config;


import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConditionalOnProperty(prefix = "ossproxy", value = "host")
@ConfigurationProperties(prefix = "ossproxy")
public class OssProxyProperties {
    private String host;
    private String localHost;
}
