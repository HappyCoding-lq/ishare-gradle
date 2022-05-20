package top.dddpeter.ishare.system.feign.factory;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.system.domain.SysUser;
import top.dddpeter.ishare.system.feign.RemoteUserService;

@Slf4j
@Component
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserService>
{
    @Override
    public RemoteUserService create(Throwable throwable)
    {
        log.error(throwable.getMessage());
        return new RemoteUserService()
        {
            @Override
            public SysUser selectSysUserByUsername(String username)
            {
                return null;
            }

            @Override
            public ResultDTO updateUserLoginRecord(SysUser user)
            {
                return ResultDTO.failure();
            }
        };
    }
}
