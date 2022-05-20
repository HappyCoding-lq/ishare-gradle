package top.dddpeter.ishare.auth;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import top.dddpeter.ishare.common.annotation.EnableIShareFeignClients;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * 启动程序
 * 
 * @author ishare
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class,scanBasePackages = {"top.dddpeter.ishare"})
@EnableDiscoveryClient
@EnableIShareFeignClients
@EnableMethodCache(basePackages = "top.dddpeter.ishare.auth")
@EnableCreateCacheAnnotation
@Slf4j
public class AuthApplicaion
{
    public static void main(String[] args)
    {

        SpringApplication.run(AuthApplicaion.class, args);
    }
    @PostConstruct
    public void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }
}