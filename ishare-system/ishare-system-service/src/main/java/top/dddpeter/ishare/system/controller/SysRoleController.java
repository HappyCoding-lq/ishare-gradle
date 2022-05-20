package top.dddpeter.ishare.system.controller;

import org.springframework.web.bind.annotation.*;
import top.dddpeter.ishare.common.auth.annotation.HasPermissions;
import top.dddpeter.ishare.common.core.controller.BaseController;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.common.log.annotation.OperLog;
import top.dddpeter.ishare.common.log.enums.BusinessType;
import top.dddpeter.ishare.system.domain.SysRole;
import top.dddpeter.ishare.system.service.ISysRoleService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 角色 提供者
 * 
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("role")
public class SysRoleController extends BaseController
{
    @Resource
    private ISysRoleService sysRoleService;

    /**
     * 查询角色
     */
    @GetMapping("get/{roleId}")
    public SysRole get(@PathVariable("roleId") Long roleId)
    {
        return sysRoleService.selectRoleById(roleId);
    }

    /**
     * 查询角色列表
     */
    @GetMapping("list")
    public ResultDTO list(SysRole sysRole)
    {
        startPage();
        return result(sysRoleService.selectRoleList(sysRole));
    }

    @GetMapping("all")
    public ResultDTO all()
    {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rows", sysRoleService.selectRoleAll());
        return ResultDTO.success(resultMap);
    }

    /**
     * 新增保存角色
     */
    @PostMapping("save")
    @OperLog(title = "角色管理", businessType = BusinessType.INSERT)
    public ResultDTO addSave(@RequestBody SysRole sysRole)
    {
        return toResultDTO(sysRoleService.insertRole(sysRole));
    }

    /**
     * 修改保存角色
     */
    @OperLog(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("update")
    public ResultDTO editSave(@RequestBody SysRole sysRole)
    {
        return toResultDTO(sysRoleService.updateRole(sysRole));
    }

    /**
     * 修改保存角色
     */
    @OperLog(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("status")
    public ResultDTO status(@RequestBody SysRole sysRole)
    {
        return toResultDTO(sysRoleService.changeStatus(sysRole));
    }
    
    /**
     * 保存角色分配数据权限
     */
    @HasPermissions("system:role:edit")
    @OperLog(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("/authDataScope")
    public ResultDTO authDataScopeSave(@RequestBody SysRole role)
    {
        role.setUpdateBy(getLoginName());
        if (sysRoleService.authDataScope(role) > 0)
        {
            return ResultDTO.success();
        }
        return ResultDTO.failure();
    }

    /**
     * 删除角色
     * @throws Exception 
     */
    @OperLog(title = "角色管理", businessType = BusinessType.DELETE)
    @PostMapping("remove")
    public ResultDTO remove(String ids) throws Exception
    {
        return toResultDTO(sysRoleService.deleteRoleByIds(ids));
    }
}
