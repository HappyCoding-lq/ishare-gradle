package top.dddpeter.ishare.system.feign.factory;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.dddpeter.ishare.system.domain.SysLogininfor;
import top.dddpeter.ishare.system.domain.SysOperLog;
import top.dddpeter.ishare.system.feign.RemoteLogService;

@Slf4j
@Component
public class RemoteLogFallbackFactory implements FallbackFactory<RemoteLogService>
{
    @Override
    public RemoteLogService create(Throwable throwable)
    {
        log.error(throwable.getMessage());
        return new RemoteLogService()
        {
            @Override
            public void insertOperlog(SysOperLog operLog)
            {
            }

            @Override
            public void insertLoginlog(SysLogininfor logininfor)
            {
            }
        };
    }
}
