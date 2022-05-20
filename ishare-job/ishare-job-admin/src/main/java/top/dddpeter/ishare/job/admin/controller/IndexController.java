package top.dddpeter.ishare.job.admin.controller;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.dddpeter.ishare.job.admin.controller.annotation.PermissionLimit;
import top.dddpeter.ishare.job.admin.service.JobService;
import top.dddpeter.ishare.job.admin.service.LoginService;
import top.dddpeter.ishare.job.biz.model.ReturnT;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * index controller
 * @author hqins 2019-12-10
 */
@Controller
public class IndexController {

	@Resource
	private JobService jobService;
	@Resource
	private LoginService loginService;


	@RequestMapping("/")
	public String index(Model model,HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> dashboardMap = jobService.dashboardInfo();
		model.addAllAttributes(dashboardMap);
		return "index";
	}
	@RequestMapping("/dashboard")
	public String dashboard(Model model,HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> dashboardMap = jobService.dashboardInfo();
		model.addAllAttributes(dashboardMap);
		return "board.index";
	}
    @RequestMapping("/chartInfo")
	@ResponseBody
	public ReturnT<Map<String, Object>> chartInfo(Date startDate, Date endDate) {
        ReturnT<Map<String, Object>> chartInfo = jobService.chartInfo(startDate, endDate);
        return chartInfo;
    }
	
	@RequestMapping("/toLogin")
	@PermissionLimit(limit=false)
	public String toLogin(HttpServletRequest request, HttpServletResponse response) {
		if (loginService.ifLogin(request, response) != null) {
			return "redirect:/";
		}
		return "login";
	}
	
	@RequestMapping(value="login", method=RequestMethod.POST)
	@ResponseBody
	@PermissionLimit(limit=false)
	public ReturnT<String> loginDo(HttpServletRequest request, HttpServletResponse response, String userName, String password, String ifRemember){
		boolean ifRem = (ifRemember!=null && ifRemember.trim().length()>0 && "on".equals(ifRemember))?true:false;
		return loginService.login(request, response, userName, password, ifRem);
	}
	
	@RequestMapping(value="logout", method=RequestMethod.POST)
	@ResponseBody
	@PermissionLimit(limit=false)
	public ReturnT<String> logout(HttpServletRequest request, HttpServletResponse response){
		return loginService.logout(request, response);
	}
	


	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
}
