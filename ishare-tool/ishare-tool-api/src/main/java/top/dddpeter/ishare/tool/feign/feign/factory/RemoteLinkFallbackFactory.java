package top.dddpeter.ishare.tool.feign.feign.factory;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.tool.feign.feign.service.RemoteLinkService;

/**
 * @auther Yoko
 * @date 2021/2/20
 */
@Slf4j
@Component
public class RemoteLinkFallbackFactory implements FallbackFactory<RemoteLinkService> {

    private static final String COMMON_ERROR_MSG = "远程系统消息服务调用异常!";

    @Override
    public RemoteLinkService create(Throwable throwable) {
        log.error(throwable.getMessage());
        return link -> ResultDTO.failure(COMMON_ERROR_MSG);

    }
}
