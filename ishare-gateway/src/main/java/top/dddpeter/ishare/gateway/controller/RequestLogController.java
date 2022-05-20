package top.dddpeter.ishare.gateway.controller;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.dddpeter.ishare.common.annotation.EnumValid;
import top.dddpeter.ishare.common.core.controller.BaseController;
import top.dddpeter.ishare.common.core.page.PageDomain;
import top.dddpeter.ishare.common.dto.PageResultDTO;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.gateway.dto.GatewayRequestLogListQueryDTO;
import top.dddpeter.ishare.gateway.enums.RouteTypeEnum;
import top.dddpeter.ishare.gateway.service.RequestLogService;
import top.dddpeter.ishare.gateway.util.RequestCommonUtil;

import javax.annotation.Resource;
import javax.validation.constraints.Min;


/**
 * 网关请求日志controller
 */
@RestController
@RequestMapping("/requestLog")
@Validated
public class RequestLogController extends BaseController {

    @Resource
    private RequestLogService requestLogService;

    @Resource
    private RequestCommonUtil reuqestCommonUtil;

    @PostMapping("/list")
    public PageResultDTO list(ServerHttpRequest request, @RequestBody GatewayRequestLogListQueryDTO queryDTO, PageDomain pageDomain) {
        reuqestCommonUtil.checkAuth(request);
        startPage(pageDomain);
        return PageResultDTO.success(requestLogService.list(queryDTO, pageDomain),
                requestLogService.listCount(queryDTO), pageDomain);
    }

    @PostMapping("/detail")
    public ResultDTO detail(ServerHttpRequest request, @RequestParam("logId") String logId) {
        reuqestCommonUtil.checkAuth(request);
        return ResultDTO.success(requestLogService.detail(logId));
    }

    @PostMapping("/delete")
    public ResultDTO delete(ServerHttpRequest request, @RequestParam("logIds") String logIds) {
        reuqestCommonUtil.checkAuth(request);
        requestLogService.delete(logIds);
        return ResultDTO.success();
    }

    @PostMapping("/clean")
    public ResultDTO clean(ServerHttpRequest request,
                           @RequestParam(value = "routeType", required = false) @EnumValid(message = "routeType传值不正确", target = RouteTypeEnum.class, allowEmpty = true) String authType,
                           @RequestParam("days") @Min(value = 0, message = "days参数异常") Integer days ) {
        reuqestCommonUtil.checkAuth(request);
        requestLogService.clean(authType, days);
        return ResultDTO.success();
    }

}
