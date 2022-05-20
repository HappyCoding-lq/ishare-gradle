package top.dddpeter.ishare.system.controller;

import org.springframework.web.bind.annotation.*;
import top.dddpeter.ishare.common.auth.annotation.HasPermissions;
import top.dddpeter.ishare.common.core.controller.BaseController;
import top.dddpeter.ishare.common.core.page.PageDomain;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.common.log.annotation.OperLog;
import top.dddpeter.ishare.common.log.enums.BusinessType;
import top.dddpeter.ishare.system.domain.SysDictType;
import top.dddpeter.ishare.system.service.ISysDictTypeService;

import javax.annotation.Resource;

/**
 * 字典类型 提供者
 * 
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("dict/type")
public class SysDictTypeController extends BaseController
{
	
	@Resource
	private ISysDictTypeService sysDictTypeService;
	
	/**
	 * 查询字典类型
	 */
	@GetMapping("get/{dictId}")
	public SysDictType get(@PathVariable("dictId") Long dictId)
	{
		return sysDictTypeService.selectDictTypeById(dictId);
		
	}
	
	/**
	 * 查询字典类型列表
	 */
	@GetMapping("list")
	@HasPermissions("system:dict:list")
	public ResultDTO list(SysDictType sysDictType, PageDomain page)
	{
		startPage();
        return result(sysDictTypeService.selectDictTypeList(sysDictType));
	}
	
	
	/**
	 * 新增保存字典类型
	 */
	@OperLog(title = "字典类型", businessType = BusinessType.INSERT)
    @HasPermissions("system:dict:add")
	@PostMapping("save")
	public ResultDTO addSave(@RequestBody SysDictType sysDictType)
	{		
		return toResultDTO(sysDictTypeService.insertDictType(sysDictType));
	}

	/**
	 * 修改保存字典类型
	 */
	@OperLog(title = "字典类型", businessType = BusinessType.UPDATE)
    @HasPermissions("system:dict:edit")
	@PostMapping("update")
	public ResultDTO editSave(@RequestBody SysDictType sysDictType)
	{		
		return toResultDTO(sysDictTypeService.updateDictType(sysDictType));
	}
	
	/**
	 * 删除字典类型
	 * @throws Exception 
	 */
	@OperLog(title = "字典类型", businessType = BusinessType.DELETE)
	@HasPermissions("system:dict:remove")
	@PostMapping("remove")
	public ResultDTO remove(String ids) throws Exception
	{		
		return toResultDTO(sysDictTypeService.deleteDictTypeByIds(ids));
	}
	
}
