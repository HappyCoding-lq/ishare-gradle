package top.dddpeter.ishare;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;
import top.dddpeter.ishare.common.annotation.EnableIShareFeignClients;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * 启动程序
 * 
 * @author ishare
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableIShareFeignClients
@MapperScan("top.dddpeter.ishare.*.mapper")
@EnableMethodCache(basePackages = "top.dddpeter.ishare.system")
@EnableCreateCacheAnnotation
public class SystemApplicaion
{
    public static void main(String[] args)
    {
        SpringApplication.run(SystemApplicaion.class, args);
    }
    @PostConstruct
    public void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }
}