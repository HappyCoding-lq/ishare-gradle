package top.dddpeter.ishare.system.controller;

import org.springframework.web.bind.annotation.*;
import top.dddpeter.ishare.common.auth.annotation.HasPermissions;
import top.dddpeter.ishare.common.core.controller.BaseController;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.common.log.annotation.OperLog;
import top.dddpeter.ishare.common.log.enums.BusinessType;
import top.dddpeter.ishare.system.domain.SysLogininfor;
import top.dddpeter.ishare.system.service.ISysLogininforService;

import javax.annotation.Resource;

/**
 * 系统访问记录 提供者
 * 
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("logininfor")
public class SysLogininforController extends BaseController
{
    @Resource
    private ISysLogininforService sysLogininforService;

    /**
     * 查询系统访问记录列表
     */
    @GetMapping("list")
    public ResultDTO list(SysLogininfor sysLogininfor)
    {
        startPage();
        return result(sysLogininforService.selectLogininforList(sysLogininfor));
    }

    /**
     * 新增保存系统访问记录
     */
    @PostMapping("save")
    public void addSave(@RequestBody SysLogininfor sysLogininfor)
    {
        sysLogininforService.insertLogininfor(sysLogininfor);
    }

    
    /**
     * 删除系统访问记录
     */
    @OperLog(title = "访问日志", businessType = BusinessType.DELETE)
    @HasPermissions("monitor:loginlog:remove")
    @PostMapping("remove")
    public ResultDTO remove(String ids)
    {
        return toResultDTO(sysLogininforService.deleteLogininforByIds(ids));
    }

    @OperLog(title = "访问日志", businessType = BusinessType.CLEAN)
    @HasPermissions("monitor:loginlog:remove")
    @PostMapping("/clean")
    public ResultDTO clean()
    {
        sysLogininforService.cleanLogininfor();
        return ResultDTO.success();
    }
    
}
