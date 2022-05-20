package top.dddpeter.ishare.tool.sequence.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.dddpeter.ishare.common.core.controller.BaseController;
import top.dddpeter.ishare.tool.sequence.service.MaxNoPubFunService;

import javax.annotation.Resource;


/**
 *  产生流水号
 * 
 * @author ishare
 * @date 2019-12-06
 */
@RestController
@RequestMapping("/maxNo")
public class MaxNoPubfunController extends BaseController
{
	
	@Resource
	private MaxNoPubFunService maxNoPubFunService;
	
	/**
	 *
	 */
	@GetMapping("/createMaxNo/{noType}/{noLength}")
	public String get(@PathVariable("noType") String noType,@PathVariable("noLength") int noLength)
	{
		return maxNoPubFunService.createMaxNo(noType,noLength);
		
	}
	
}
