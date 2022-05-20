package top.dddpeter.ishare.system.controller;

import org.springframework.web.bind.annotation.*;
import top.dddpeter.ishare.common.core.controller.BaseController;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.system.domain.SysDept;
import top.dddpeter.ishare.system.service.ISysDeptService;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 部门 提供者
 * 
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("dept")
public class SysDeptController extends BaseController
{
    @Resource
    private ISysDeptService sysDeptService;

    /**
     * 查询部门
     */
    @GetMapping("get/{deptId}")
    public SysDept get(@PathVariable("deptId") Long deptId)
    {
        return sysDeptService.selectDeptById(deptId);
    }

    /**
     * 查询部门列表
     */
    @GetMapping("list")
    public ResultDTO list(SysDept sysDept)
    {
        startPage();
        return result(sysDeptService.selectDeptList(sysDept));
    }

    /**
     * 新增保存部门
     */
    @PostMapping("save")
    public ResultDTO addSave(@RequestBody SysDept sysDept)
    {
        return toResultDTO(sysDeptService.insertDept(sysDept));
    }

    /**
     * 修改保存部门
     */
    @PostMapping("update")
    public ResultDTO editSave(@RequestBody SysDept sysDept)
    {
        return toResultDTO(sysDeptService.updateDept(sysDept));
    }

    /**
     * 删除部门
     */
    @PostMapping("remove/{deptId}")
    public ResultDTO remove(@PathVariable("deptId") Long deptId)
    {
        return toResultDTO(sysDeptService.deleteDeptById(deptId));
    }
    
    /**
     * 加载角色部门（数据权限）列表树
     */
    @GetMapping("/role/{roleId}")
    public Set<String> deptTreeData(@PathVariable("roleId" )Long roleId)
    {
        if (null == roleId || roleId <= 0) {
            return null;
        }
        return sysDeptService.roleDeptIds(roleId);
    }
}
