package top.dddpeter.ishare.system.controller;

import org.springframework.web.bind.annotation.*;
import top.dddpeter.ishare.common.auth.annotation.HasPermissions;
import top.dddpeter.ishare.common.core.controller.BaseController;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.common.log.annotation.OperLog;
import top.dddpeter.ishare.common.log.enums.BusinessType;
import top.dddpeter.ishare.common.utils.poi.ExcelUtil;
import top.dddpeter.ishare.system.domain.Districts;
import top.dddpeter.ishare.system.service.IDistrictsService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 地区 信息操作处理
 * 
 * @author ishare
 * @date 2018-12-19
 */
@RestController
@RequestMapping("districts")
public class SysDistrictsController extends BaseController
{
    @Resource
    private IDistrictsService districtsService;

    /**
     * 查询地区列表
     */
    @HasPermissions("system:districts:list")
    @RequestMapping("/list")
    public ResultDTO list(Districts districts)
    {
        startPage();
        return result(districtsService.selectDistrictsList(districts));
    }

    /**
     * 导出地区列表
     */
    @HasPermissions("system:districts:export")
    @OperLog(title = "地区", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public ResultDTO export(Districts districts)
    {
        List<Districts> list = districtsService.selectDistrictsList(districts);
        ExcelUtil<Districts> util = new ExcelUtil<Districts>(Districts.class);
        return util.exportExcel(list, "districts");
    }

    /**
     * 新增保存地区
     */
    @HasPermissions("system:districts:add")
    @OperLog(title = "地区", businessType = BusinessType.INSERT)
    @PostMapping("save")
    public ResultDTO addSave(@RequestBody Districts districts)
    {
        districts.setPid(districts.getId() / 100);
        districts.setCreateTime(new Date());
        districts.setUpdateTime(new Date());
        districts.setOperator(getLoginName());
        return toResultDTO(districtsService.insertDistricts(districts));
    }

    /**
    
    /**
     * 修改保存地区
     */
    @HasPermissions("system:districts:edit")
    @OperLog(title = "地区", businessType = BusinessType.UPDATE)
    @PostMapping("update")
    public ResultDTO editSave(@RequestBody Districts districts)
    {
        districts.setPid(districts.getId() / 100);
        districts.setOperator(getLoginName());
        districts.setUpdateTime(new Date());
        return toResultDTO(districtsService.updateDistricts(districts));
    }

    /**
     * 删除地区
     */
    @HasPermissions("system:districts:remove")
    @OperLog(title = "地区", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public ResultDTO remove(String ids)
    {
        return toResultDTO(districtsService.deleteDistrictsByIds(ids));
    }
}