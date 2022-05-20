package top.dddpeter.ishare.system.controller;

import org.springframework.web.bind.annotation.*;
import top.dddpeter.ishare.common.core.controller.BaseController;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.system.domain.SysConfig;
import top.dddpeter.ishare.system.service.ISysConfigService;

import javax.annotation.Resource;

/**
 * 参数配置 提供者
 * 
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("config")
public class SysConfigController extends BaseController
{
	
	@Resource
	private ISysConfigService sysConfigService;
	
	/**
	 * 查询参数配置
	 */
	@GetMapping("get/{configId}")
	public SysConfig get(@PathVariable("configId") Long configId)
	{
		return sysConfigService.selectConfigById(configId);
		
	}
	
	/**
	 * 查询参数配置列表
	 */
	@GetMapping("list")
	public ResultDTO list(SysConfig sysConfig)
	{
		startPage();
        return result(sysConfigService.selectConfigList(sysConfig));
	}
	
	
	/**
	 * 新增保存参数配置
	 */
	@PostMapping("save")
	public ResultDTO addSave(@RequestBody SysConfig sysConfig)
	{		
		return toResultDTO(sysConfigService.insertConfig(sysConfig));
	}

	/**
	 * 修改保存参数配置
	 */
	@PostMapping("update")
	public ResultDTO editSave(@RequestBody SysConfig sysConfig)
	{		
		return toResultDTO(sysConfigService.updateConfig(sysConfig));
	}
	
	/**
	 * 删除参数配置
	 */
	@PostMapping("remove")
	public ResultDTO remove(String ids)
	{		
		return toResultDTO(sysConfigService.deleteConfigByIds(ids));
	}
	
}
