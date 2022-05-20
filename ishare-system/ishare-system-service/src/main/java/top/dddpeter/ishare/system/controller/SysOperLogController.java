package top.dddpeter.ishare.system.controller;

import org.springframework.web.bind.annotation.*;
import top.dddpeter.ishare.common.auth.annotation.HasPermissions;
import top.dddpeter.ishare.common.core.controller.BaseController;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.common.log.annotation.OperLog;
import top.dddpeter.ishare.common.log.enums.BusinessType;
import top.dddpeter.ishare.common.utils.poi.ExcelUtil;
import top.dddpeter.ishare.system.domain.SysOperLog;
import top.dddpeter.ishare.system.service.ISysOperLogService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 操作日志记录 提供者
 * 
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("operLog")
public class SysOperLogController extends BaseController
{
    @Resource
    private ISysOperLogService sysOperLogService;

    /**
     * 查询操作日志记录
     */
    @GetMapping("get/{operId}")
    public SysOperLog get(@PathVariable("operId") Long operId)
    {
        return sysOperLogService.selectOperLogById(operId);
    }

    /**
     * 查询操作日志记录列表
     */
    @HasPermissions("monitor:operlog:list")
    @RequestMapping("list")
    public ResultDTO list(SysOperLog sysOperLog)
    {
        startPage();
        return result(sysOperLogService.selectOperLogList(sysOperLog));
    }

    @OperLog(title = "操作日志", businessType = BusinessType.EXPORT)
    @HasPermissions("monitor:operlog:export")
    @PostMapping("/export")
    public ResultDTO export(SysOperLog operLog)
    {
        List<SysOperLog> list = sysOperLogService.selectOperLogList(operLog);
        ExcelUtil<SysOperLog> util = new ExcelUtil<SysOperLog>(SysOperLog.class);
        return util.exportExcel(list, "操作日志");
    }

    /**
     * 新增保存操作日志记录
     */
    @PostMapping("save")
    public void addSave(@RequestBody SysOperLog sysOperLog)
    {
        sysOperLogService.insertOperlog(sysOperLog);
    }

    /**
     * 删除操作日志记录
     */
    @HasPermissions("monitor:operlog:remove")
    @PostMapping("remove")
    public ResultDTO remove(String ids)
    {
        return toResultDTO(sysOperLogService.deleteOperLogByIds(ids));
    }

    @OperLog(title = "操作日志", businessType = BusinessType.CLEAN)
    @HasPermissions("monitor:operlog:remove")
    @PostMapping("/clean")
    public ResultDTO clean()
    {
        sysOperLogService.cleanOperLog();
        return ResultDTO.success();
    }
}
