package top.dddpeter.ishare.system.controller;

import org.springframework.web.bind.annotation.*;
import top.dddpeter.ishare.common.auth.annotation.HasPermissions;
import top.dddpeter.ishare.common.core.controller.BaseController;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.common.log.annotation.OperLog;
import top.dddpeter.ishare.common.log.enums.BusinessType;
import top.dddpeter.ishare.system.annotation.LoginUser;
import top.dddpeter.ishare.system.domain.SysMenu;
import top.dddpeter.ishare.system.domain.SysUser;
import top.dddpeter.ishare.system.service.ISysMenuService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * 菜单权限 
 * 
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("menu")
public class SysMenuController extends BaseController
{
    @Resource
    private ISysMenuService sysMenuService;

    /**
     * 查询菜单权限
     */
    @GetMapping("get/{menuId}")
    public SysMenu get(@PathVariable("menuId") Long menuId)
    {
        return sysMenuService.selectMenuById(menuId);
    }

    @GetMapping("perms/{userId}")
    public Set<String> perms(@PathVariable("userId") Long userId)
    {
        return sysMenuService.selectPermsByUserId(userId);
    }

    /**
     * 查询菜单权限
     */
    @GetMapping("user")
    public List<SysMenu> user(@LoginUser SysUser sysUser)
    {
        return sysMenuService.selectMenusByUser(sysUser);
    }

    /**
     * 根据角色编号查询菜单编号（用于勾选）
     * @param roleId
     * @return
     * @author zmr
     */
    @GetMapping("role/{roleId}")
    public List<SysMenu> role(@PathVariable("roleId") Long roleId)
    {
        if (null == roleId || roleId <= 0) {
            return null;
        }
        return sysMenuService.selectMenuIdsByRoleId(roleId);
    }

    /**
     * 查询菜单权限列表
     */
    @HasPermissions("system:menu:view")
    @GetMapping("list")
    public ResultDTO list(SysMenu sysMenu)
    {
        return result(sysMenuService.selectMenuList(sysMenu));
    }

    /**
     * 新增保存菜单权限
     */
    @PostMapping("save")
    @OperLog(title = "菜单管理", businessType = BusinessType.INSERT)
    public ResultDTO addSave(@RequestBody SysMenu sysMenu)
    {
        return toResultDTO(sysMenuService.insertMenu(sysMenu));
    }

    /**
     * 修改保存菜单权限
     */
    @OperLog(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PostMapping("update")
    public ResultDTO editSave(@RequestBody SysMenu sysMenu)
    {
        return toResultDTO(sysMenuService.updateMenu(sysMenu));
    }

    /**
     * 删除菜单权限
     */
    @OperLog(title = "菜单管理", businessType = BusinessType.DELETE)
    @PostMapping("remove/{menuId}")
    public ResultDTO remove(@PathVariable("menuId") Long menuId)
    {
        return toResultDTO(sysMenuService.deleteMenuById(menuId));
    }
}
