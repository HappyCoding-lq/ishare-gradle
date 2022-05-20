package top.dddpeter.ishare.system.util;

import top.dddpeter.ishare.common.utils.security.Md5Utils;
import top.dddpeter.ishare.system.domain.SysUser;

/**
 * @author hqins
 */
public class PasswordUtil
{
    public static boolean matches(SysUser user, String newPassword)
    {
        return user.getPassword().equals(encryptPassword(user.getLoginName(), newPassword, user.getSalt()));
    }

    public static String encryptPassword(String username, String password, String salt)
    {
        return Md5Utils.hash(username + password + salt);
    }
}