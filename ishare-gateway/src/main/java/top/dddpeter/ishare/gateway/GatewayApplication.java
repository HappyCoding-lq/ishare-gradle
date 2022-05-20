package top.dddpeter.ishare.gateway;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;
import top.dddpeter.ishare.common.annotation.EnableIShareFeignClients;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {
        "top.dddpeter.ishare.gateway", "top.dddpeter.ishare.common.config",
        "top.dddpeter.ishare.common.mongo", "top.dddpeter.ishare.common.utils",
        "top.dddpeter.ishare.common.redis", "top.dddpeter.ishare.job","top.dddpeter.ishare.common.oss"
})
@EnableIShareFeignClients
@MapperScan(basePackages = {"top.dddpeter.ishare.*.mapper","top.dddpeter.ishare.gateway.bank.mapper"})
@EnableMethodCache(basePackages = "top.dddpeter.ishare")
@EnableCreateCacheAnnotation
@EnableScheduling
public class GatewayApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(GatewayApplication.class, args);
    }
    @PostConstruct
    public void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }
}

