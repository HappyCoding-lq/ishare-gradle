package top.dddpeter.ishare.system.controller;

import org.springframework.web.bind.annotation.*;
import top.dddpeter.ishare.common.auth.annotation.HasPermissions;
import top.dddpeter.ishare.common.constant.UserConstants;
import top.dddpeter.ishare.common.core.controller.BaseController;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.common.log.annotation.OperLog;
import top.dddpeter.ishare.common.log.enums.BusinessType;
import top.dddpeter.ishare.common.utils.RandomUtil;
import top.dddpeter.ishare.system.annotation.LoginUser;
import top.dddpeter.ishare.system.domain.SysUser;
import top.dddpeter.ishare.system.service.ISysMenuService;
import top.dddpeter.ishare.system.service.ISysUserService;
import top.dddpeter.ishare.system.util.PasswordUtil;

import javax.annotation.Resource;

/**
 * 用户 提供者
 * 
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("user")
public class SysUserController extends BaseController
{
    @Resource
    private ISysUserService sysUserService;

    @Resource
    private ISysMenuService sysMenuService;

    /**
     * 查询用户
     */
    @GetMapping("get/{userId}")
    public SysUser get(@PathVariable("userId") Long userId)
    {
        return sysUserService.selectUserById(userId);
    }

    @GetMapping("info")
    public SysUser info(@LoginUser SysUser sysUser)
    {
        sysUser.setButtons(sysMenuService.selectPermsByUserId(sysUser.getUserId()));
        return sysUser;
    }

    /**
     * 查询用户
     */
    @GetMapping("find/{username}")
    public SysUser findByUsername(@PathVariable("username") String username)
    {
        return sysUserService.selectUserByLoginName(username);
    }

    /**
     * 查询用户列表
     */
    @GetMapping("list")
    public ResultDTO list(SysUser sysUser)
    {
        startPage();
        return result(sysUserService.selectUserList(sysUser));
    }

    /**
     * 新增保存用户
     */
    @HasPermissions("system:user:add")
    @PostMapping("save")
    @OperLog(title = "用户管理", businessType = BusinessType.INSERT)
    public ResultDTO addSave(@RequestBody SysUser sysUser)
    {
        if (UserConstants.USER_NAME_NOT_UNIQUE.equals(sysUserService.checkLoginNameUnique(sysUser.getLoginName())))
        {
            return ResultDTO.failure("新增用户'" + sysUser.getLoginName() + "'失败，登录账号已存在");
        }
        else if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(sysUserService.checkPhoneUnique(sysUser)))
        {
            return ResultDTO.failure("新增用户'" + sysUser.getLoginName() + "'失败，手机号码已存在");
        }
        else if (UserConstants.USER_EMAIL_NOT_UNIQUE.equals(sysUserService.checkEmailUnique(sysUser)))
        {
            return ResultDTO.failure("新增用户'" + sysUser.getLoginName() + "'失败，邮箱账号已存在");
        }
        sysUser.setSalt(RandomUtil.randomStr(6));
        sysUser.setPassword(
                PasswordUtil.encryptPassword(sysUser.getLoginName(), sysUser.getPassword(), sysUser.getSalt()));
        sysUser.setCreateBy(getLoginName());
        return toResultDTO(sysUserService.insertUser(sysUser));
    }

    /**
     * 修改保存用户
     */
    @HasPermissions("system:user:edit")
    @OperLog(title = "用户管理", businessType = BusinessType.UPDATE)
    @PostMapping("update")
    public ResultDTO editSave(@RequestBody SysUser sysUser)
    {
        if (null != sysUser.getUserId() && SysUser.isAdmin(sysUser.getUserId()))
        {
            return ResultDTO.failure("不允许修改超级管理员用户");
        }
        else if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(sysUserService.checkPhoneUnique(sysUser)))
        {
            return ResultDTO.failure("修改用户'" + sysUser.getLoginName() + "'失败，手机号码已存在");
        }
        else if (UserConstants.USER_EMAIL_NOT_UNIQUE.equals(sysUserService.checkEmailUnique(sysUser)))
        {
            return ResultDTO.failure("修改用户'" + sysUser.getLoginName() + "'失败，邮箱账号已存在");
        }
        return toResultDTO(sysUserService.updateUser(sysUser));
    }

    /**
     * 修改用户信息
     * @param sysUser
     * @return
     * @author zmr
     */
    @HasPermissions("system:user:edit")
    @PostMapping("update/info")
    @OperLog(title = "用户管理", businessType = BusinessType.UPDATE)
    public ResultDTO updateInfo(@RequestBody SysUser sysUser)
    {
        return toResultDTO(sysUserService.updateUserInfo(sysUser));
    }

    /**
     * 记录登陆信息
     * @param sysUser
     * @return
     * @author zmr
     */
    @PostMapping("update/login")
    public ResultDTO updateLoginRecord(@RequestBody SysUser sysUser)
    {
        return toResultDTO(sysUserService.updateUser(sysUser));
    }

    @HasPermissions("system:user:resetPwd")
    @OperLog(title = "重置密码", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    public ResultDTO resetPwdSave(@RequestBody SysUser user)
    {
        user.setSalt(RandomUtil.randomStr(6));
        user.setPassword(PasswordUtil.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        return toResultDTO(sysUserService.resetUserPwd(user));
    }

    /**
     * 修改状态
     * @param sysUser
     * @return
     * @author zmr
     */
    @HasPermissions("system:user:edit")
    @PostMapping("status")
    @OperLog(title = "用户管理", businessType = BusinessType.UPDATE)
    public ResultDTO status(@RequestBody SysUser sysUser)
    {
        return toResultDTO(sysUserService.changeStatus(sysUser));
    }

    /**
     * 删除用户
     * @throws Exception 
     */
    @HasPermissions("system:user:remove")
    @OperLog(title = "用户管理", businessType = BusinessType.DELETE)
    @PostMapping("remove")
    public ResultDTO remove(String ids) throws Exception
    {
        return toResultDTO(sysUserService.deleteUserByIds(ids));
    }
}
