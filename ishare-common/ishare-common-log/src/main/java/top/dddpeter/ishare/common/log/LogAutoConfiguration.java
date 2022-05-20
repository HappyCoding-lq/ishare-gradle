package top.dddpeter.ishare.common.log;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import top.dddpeter.ishare.common.log.aspect.OperLogAspect;
import top.dddpeter.ishare.common.log.listen.LogListener;
import top.dddpeter.ishare.system.feign.RemoteLogService;

import javax.annotation.Resource;

@EnableAsync
@Configuration
@AllArgsConstructor
@ConditionalOnWebApplication
public class LogAutoConfiguration
{

    @Resource
    private final RemoteLogService logService;

    @Bean
    public LogListener sysOperLogListener()
    {
        return new LogListener(logService);
    }

    @Bean
    public OperLogAspect operLogAspect()
    {
        return new OperLogAspect();
    }
}