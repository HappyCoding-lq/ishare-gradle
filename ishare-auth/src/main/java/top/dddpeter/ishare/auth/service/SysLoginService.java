package top.dddpeter.ishare.auth.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import top.dddpeter.ishare.auth.exception.UserBlockedException;
import top.dddpeter.ishare.auth.exception.UserDeleteException;
import top.dddpeter.ishare.auth.exception.UserNotExistsException;
import top.dddpeter.ishare.auth.exception.UserPasswordNotMatchException;
import top.dddpeter.ishare.common.constant.Constants;
import top.dddpeter.ishare.common.constant.UserConstants;
import top.dddpeter.ishare.common.enums.UserStatus;
import top.dddpeter.ishare.common.log.publish.PublishFactory;
import top.dddpeter.ishare.common.utils.DateUtils;
import top.dddpeter.ishare.common.utils.IpUtils;
import top.dddpeter.ishare.common.utils.MessageUtils;
import top.dddpeter.ishare.common.utils.ServletUtils;
import top.dddpeter.ishare.system.domain.SysUser;
import top.dddpeter.ishare.system.feign.RemoteUserService;
import top.dddpeter.ishare.system.util.PasswordUtil;

import javax.annotation.Resource;

/**
 * @author hqins
 */
@Component
public class SysLoginService
{

    @Resource
    private RemoteUserService  userService;

    /**
     * 登录
     */
    public SysUser login(String username, String password)
    {
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(username, password))
        {
            PublishFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null"));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            PublishFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.password.not.match"));
            throw new UserPasswordNotMatchException();
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            PublishFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.password.not.match"));
            throw new UserPasswordNotMatchException();
        }
        // 查询用户信息
        SysUser user = userService.selectSysUserByUsername(username);
        if (user == null)
        {
            PublishFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.not.exists"));
            throw new UserNotExistsException();
        }
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            PublishFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.password.delete"));
            throw new UserDeleteException();
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            PublishFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.blocked", user.getRemark()));
            throw new UserBlockedException();
        }
        if (!PasswordUtil.matches(user, password))
        {
            throw new UserPasswordNotMatchException();
        }
        //发布登录事件
        PublishFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));
        recordLoginInfo(user);
        return user;
    }

    /**
     * 记录登录信息
     */
    public void recordLoginInfo(SysUser user)
    {
        user.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        user.setLoginDate(DateUtils.getNowDate());
        userService.updateUserLoginRecord(user);
    }

    public void logout(String loginName)
    {
        PublishFactory.recordLogininfor(loginName, Constants.LOGOUT, MessageUtils.message("user.logout.success"));
    }
}