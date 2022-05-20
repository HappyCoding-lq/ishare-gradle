package top.dddpeter.ishare.system.controller;

import org.springframework.web.bind.annotation.*;
import top.dddpeter.ishare.common.core.controller.BaseController;
import top.dddpeter.ishare.common.core.page.PageDomain;
import top.dddpeter.ishare.system.domain.SysNotice;
import top.dddpeter.ishare.system.service.ISysNoticeService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 通知公告 提供者
 * 
 * @author zmr
 * @date 2019-05-20
 */
@RestController
@RequestMapping("notice")
public class SysNoticeClient extends BaseController
{
	
	@Resource
	private ISysNoticeService sysNoticeService;
	
	/**
	 * 查询通知公告
	 */
	@GetMapping("get/{noticeId}")
	public SysNotice get(@PathVariable("noticeId") Long noticeId)
	{
		return sysNoticeService.selectNoticeById(noticeId);
		
	}
	
	/**
	 * 查询通知公告列表
	 */
	@PostMapping("list")
	public List<SysNotice> list(SysNotice sysNotice, PageDomain page)
	{
		startPage();
        return sysNoticeService.selectNoticeList(sysNotice);
	}
	
	
	/**
	 * 新增保存通知公告
	 */
	@PostMapping("save")
	public int addSave(SysNotice sysNotice)
	{		
		return sysNoticeService.insertNotice(sysNotice);
	}

	/**
	 * 修改保存通知公告
	 */
	@PostMapping("update")
	public int editSave(SysNotice sysNotice)
	{		
		return sysNoticeService.updateNotice(sysNotice);
	}
	
	/**
	 * 删除通知公告
	 */
	@PostMapping("remove")
	public int remove(String ids)
	{		
		return sysNoticeService.deleteNoticeByIds(ids);
	}
	
}
