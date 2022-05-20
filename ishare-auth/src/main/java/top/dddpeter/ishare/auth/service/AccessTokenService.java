package top.dddpeter.ishare.auth.service;

import cn.hutool.core.util.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import top.dddpeter.ishare.auth.vo.LoginFormVO;
import top.dddpeter.ishare.common.constant.Constants;
import top.dddpeter.ishare.common.redis.annotation.RedisEvict;
import top.dddpeter.ishare.common.redis.util.RedisUtils;
import top.dddpeter.ishare.system.domain.SysUser;

import javax.annotation.Resource;

@Service("accessTokenService")
public class AccessTokenService
{
    @Resource
    private RedisUtils redis;

    /**
     * 12小时后过期
     */
    private static final long   EXPIRE        = 12 * 60 * 60L;

    private static final String ACCESS_TOKEN  = Constants.ACCESS_TOKEN;

    private static final String ACCESS_USERID = Constants.ACCESS_USERID;

    public SysUser queryByToken(String token)
    {
        return redis.get(ACCESS_TOKEN + token, SysUser.class);
    }

    @RedisEvict(key = "user_perms", fieldKey = "#sysUser.userId")
    public LoginFormVO createToken(SysUser sysUser)
    {
        // 生成token
        String token = IdUtil.fastSimpleUUID();
        // 保存或更新用户token
        LoginFormVO vo = new LoginFormVO();
        vo.setUserId(sysUser.getUserId());
        vo.setToken(token);
        vo.setExpire(EXPIRE);
        redis.set(ACCESS_TOKEN + token, sysUser, EXPIRE);
        redis.set(ACCESS_USERID + sysUser.getUserId(), token, EXPIRE);
        return vo;
    }

    public void expireToken(long userId)
    {
        String token = redis.get(ACCESS_USERID + userId);
        if (StringUtils.isNotBlank(token))
        {
            redis.delete(ACCESS_USERID + userId);
            redis.delete(ACCESS_TOKEN + token);
        }
    }
}