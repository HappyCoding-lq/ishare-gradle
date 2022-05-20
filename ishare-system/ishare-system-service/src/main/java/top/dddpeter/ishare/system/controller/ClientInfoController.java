package top.dddpeter.ishare.system.controller;

import org.springframework.web.bind.annotation.*;
import top.dddpeter.ishare.common.core.controller.BaseController;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.system.domain.ClientInfo;
import top.dddpeter.ishare.system.service.IClientInfoService;

import javax.annotation.Resource;

/**
 * clientInfo 提供者
 * 
 * @author ishare
 * @date 2019-12-03
 */
@RestController
@RequestMapping("clientInfo")
public class ClientInfoController extends BaseController
{
	
	@Resource
	private IClientInfoService clientInfoService;
	
	/**
	 * 查询${tableComment}
	 */
	@GetMapping("get/{id}")
	public ClientInfo get(@PathVariable("id") Long id)
	{
		return clientInfoService.selectClientInfoById(id);
		
	}
	
	/**
	 * 查询clientInfo列表
	 */
	@GetMapping("list")
	public ResultDTO list(ClientInfo clientInfo)
	{
		startPage();
        return result(clientInfoService.selectClientInfoList(clientInfo));
	}
	
	
	/**
	 * 新增保存clientInfo
	 */
	@PostMapping("save")
	public ResultDTO addSave(@RequestBody ClientInfo clientInfo)
	{		
		return toResultDTO(clientInfoService.insertClientInfo(clientInfo));
	}

	/**
	 * 修改保存clientInfo
	 */
	@PostMapping("update")
	public ResultDTO editSave(@RequestBody ClientInfo clientInfo)
	{		
		return toResultDTO(clientInfoService.updateClientInfo(clientInfo));
	}
	
	/**
	 * 删除${tableComment}
	 */
	@PostMapping("remove")
	public ResultDTO remove(String ids)
	{		
		return toResultDTO(clientInfoService.deleteClientInfoByIds(ids));
	}



	@GetMapping("secret")
	public ResultDTO clientSecretGenerate(){
		return  ResultDTO.success("200","ok",clientInfoService.clientSecretGenerate());

	}




	
}
