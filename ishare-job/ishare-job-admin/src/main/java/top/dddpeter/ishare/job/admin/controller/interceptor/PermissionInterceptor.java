package top.dddpeter.ishare.job.admin.controller.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import top.dddpeter.ishare.job.admin.controller.annotation.PermissionLimit;
import top.dddpeter.ishare.job.admin.core.model.JobUser;
import top.dddpeter.ishare.job.admin.core.util.I18nUtil;
import top.dddpeter.ishare.job.admin.service.LoginService;
import top.dddpeter.ishare.job.biz.model.ReturnT;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限拦截
 *
 * @author hqins 2019-12-10
 */
@Component
public class PermissionInterceptor extends HandlerInterceptorAdapter {

	@Resource
	private LoginService loginService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		if (!(handler instanceof HandlerMethod)) {
			return super.preHandle(request, response, handler);
		}

		// if need login
		boolean needLogin = true;
		boolean needAdminuser = false;
		HandlerMethod method = (HandlerMethod)handler;
		PermissionLimit permission = method.getMethodAnnotation(PermissionLimit.class);
		if (permission!=null) {
			needLogin = permission.limit();
			needAdminuser = permission.adminuser();
		}
		String token = request.getParameter("token");

		if (needLogin) {
			JobUser loginUser = loginService.ifLogin(request, response);
			if (loginUser == null) {
				if(token!=null){
					ReturnT<String> returnT = loginService.login(request, response,token);
					if(returnT.getCode() != ReturnT.SUCCESS_CODE){
						response.sendRedirect(request.getContextPath() + "/toLogin");
					}
					else{
						response.sendRedirect(request.getContextPath() + request.getRequestURI());
					}
				}
				else{
					response.sendRedirect(request.getContextPath() + "/toLogin");
				}
				//request.getRequestDispatcher("/toLogin").forward(request, response);
				return false;
			}
			if (needAdminuser && loginUser.getRole()!=1) {
				throw new RuntimeException(I18nUtil.getString("system_permission_limit"));
			}
			request.setAttribute(LoginService.LOGIN_IDENTITY_KEY, loginUser);
		}

		return super.preHandle(request, response, handler);
	}
	
}
