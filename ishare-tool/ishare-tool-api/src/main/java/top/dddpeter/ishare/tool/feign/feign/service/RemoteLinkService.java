package top.dddpeter.ishare.tool.feign.feign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import top.dddpeter.ishare.common.constant.ServiceNameConstants;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.tool.feign.domain.CommonLink;
import top.dddpeter.ishare.tool.feign.feign.factory.RemoteLinkFallbackFactory;

/**
 * @auther Yoko
 * @date 2021/2/20
 */
@FeignClient(name = ServiceNameConstants.TOOL_SERVICE, fallbackFactory = RemoteLinkFallbackFactory.class)
public interface RemoteLinkService {

    @PostMapping("/link/common/saveLink")
    ResultDTO saveLinkDTO(CommonLink link);
}
