package top.dddpeter.ishare.auth.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.dddpeter.ishare.auth.dto.LoginFormDTO;
import top.dddpeter.ishare.auth.service.AccessTokenService;
import top.dddpeter.ishare.auth.service.SysLoginService;
import top.dddpeter.ishare.auth.vo.LoginFormVO;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.system.domain.SysUser;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
public class TokenController
{
    @Resource
    private AccessTokenService tokenService;

    @Resource
    private SysLoginService    sysLoginService;

    @PostMapping("login")
    @ApiOperation(value = "用户登录接口", httpMethod = "POST", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, response = LoginFormVO.class)
    public ResultDTO login(@RequestBody LoginFormDTO form)
    {
        // 用户登录
        SysUser user = sysLoginService.login(form.getUsername(), form.getPassword());
        // 获取登录token
        return ResultDTO.success(tokenService.createToken(user));
    }

    @PostMapping("logout")
    @ApiOperation(value = "用户退出登录接口", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE, response = ResultDTO.class)
    @ApiImplicitParams(@ApiImplicitParam(paramType = "header", name = "token", dataType = "String", required = true, value = "用户登录的token"))
    public ResultDTO logout(HttpServletRequest request)
    {
        String token = request.getHeader("token");
        SysUser user = tokenService.queryByToken(token);
        if (null != user)
        {
            sysLoginService.logout(user.getLoginName());
            tokenService.expireToken(user.getUserId());
        }
        return ResultDTO.success();
    }
}
