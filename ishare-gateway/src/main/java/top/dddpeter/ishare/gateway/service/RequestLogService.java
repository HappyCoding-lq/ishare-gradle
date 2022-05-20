package top.dddpeter.ishare.gateway.service;

import top.dddpeter.ishare.common.core.page.PageDomain;
import top.dddpeter.ishare.gateway.domain.GatewayRequestLog;
import top.dddpeter.ishare.gateway.dto.GatewayRequestLogListQueryDTO;
import top.dddpeter.ishare.gateway.vo.GatewayRequestLogListQueryVO;

import java.util.List;

/**
 * 网关请求日志处理service层
 */
public interface RequestLogService {

    List<GatewayRequestLogListQueryVO> list(GatewayRequestLogListQueryDTO queryDTO, PageDomain pageDomain);

    int listCount(GatewayRequestLogListQueryDTO queryDTO);

    GatewayRequestLog detail(String logId);

    void delete(String logIds);

    void clean(String routeType, Integer days);

}
