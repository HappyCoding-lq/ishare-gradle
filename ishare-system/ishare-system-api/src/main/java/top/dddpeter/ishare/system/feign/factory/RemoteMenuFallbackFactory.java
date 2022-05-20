package top.dddpeter.ishare.system.feign.factory;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.dddpeter.ishare.system.feign.RemoteMenuService;

import java.util.Set;

@Slf4j
@Component
public class RemoteMenuFallbackFactory implements FallbackFactory<RemoteMenuService>
{
    @Override
    public RemoteMenuService create(Throwable throwable)
    {
        log.error(throwable.getMessage());
        return new RemoteMenuService()
        {

            @Override
            public Set<String> selectPermsByUserId(Long userId)
            {
                return null;
            }
        };
    }
}
