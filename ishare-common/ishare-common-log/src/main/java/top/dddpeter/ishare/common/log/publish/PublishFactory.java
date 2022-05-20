package top.dddpeter.ishare.common.log.publish;

import eu.bitwalker.useragentutils.UserAgent;
import top.dddpeter.ishare.common.constant.Constants;
import top.dddpeter.ishare.common.log.event.SysLogininforEvent;
import top.dddpeter.ishare.common.utils.AddressUtils;
import top.dddpeter.ishare.common.utils.IpUtils;
import top.dddpeter.ishare.common.utils.ServletUtils;
import top.dddpeter.ishare.common.utils.spring.SpringContextHolder;
import top.dddpeter.ishare.system.domain.SysLogininfor;

import javax.servlet.http.HttpServletRequest;

public class PublishFactory
{
    /**
     * 记录登陆信息
     * 
     * @param username 用户名
     * @param status 状态
     * @param message 消息
     * @param args 列表
     */
    public static void recordLogininfor(final String username, final String status, final String message,
            final Object ... args)
    {
        HttpServletRequest request = ServletUtils.getRequest();
        final UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr(request);
        // 获取客户端操作系统
        String os = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        // 封装对象
        SysLogininfor logininfor = new SysLogininfor();
        logininfor.setLoginName(username);
        logininfor.setIpaddr(ip);
        logininfor.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        logininfor.setBrowser(browser);
        logininfor.setOs(os);
        logininfor.setMsg(message);
        // 日志状态
        if (Constants.LOGIN_SUCCESS.equals(status) || Constants.LOGOUT.equals(status))
        {
            logininfor.setStatus(Constants.SUCCESS);
        }
        else if (Constants.LOGIN_FAIL.equals(status))
        {
            logininfor.setStatus(Constants.FAIL);
        }
        // 发布事件
        SpringContextHolder.publishEvent(new SysLogininforEvent(logininfor));
    }
}
