package top.dddpeter.ishare.tool;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import top.dddpeter.ishare.common.annotation.EnableIShareFeignClients;

import javax.annotation.PostConstruct;
import java.util.TimeZone;


/**
 * @author hqins
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableIShareFeignClients
@MapperScan("top.dddpeter.ishare.tool.sequence.dao")
@EnableMethodCache(basePackages = "top.dddpeter.ishare.tool")
@EnableCreateCacheAnnotation
public class ToolApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ToolApplication.class).run(args);
    }
    @PostConstruct
    public void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }
}
