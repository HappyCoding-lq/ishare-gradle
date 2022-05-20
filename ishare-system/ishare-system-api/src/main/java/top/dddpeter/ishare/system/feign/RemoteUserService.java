package top.dddpeter.ishare.system.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.dddpeter.ishare.common.constant.ServiceNameConstants;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.system.domain.SysUser;
import top.dddpeter.ishare.system.feign.factory.RemoteUserFallbackFactory;

/**
 * 用户 Feign服务层
 * 
 * @author zmr
 * @date 2019-05-20
 */
@FeignClient(name = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService
{
    @GetMapping("user/find/{username}")
    public SysUser selectSysUserByUsername(@PathVariable("username") String username);

    @PostMapping("user/update/login")
    public ResultDTO updateUserLoginRecord(@RequestBody SysUser user);
}
