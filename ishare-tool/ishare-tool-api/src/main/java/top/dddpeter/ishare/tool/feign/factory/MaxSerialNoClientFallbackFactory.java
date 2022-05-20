package top.dddpeter.ishare.tool.feign.factory;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.dddpeter.ishare.common.constant.Constants;
import top.dddpeter.ishare.tool.feign.MaxSerialNoClient;

@Component
@Slf4j
public class MaxSerialNoClientFallbackFactory implements FallbackFactory<MaxSerialNoClient> {

    @Override
    public MaxSerialNoClient create(Throwable throwable) {
        log.error("feign异常：{}", throwable.getMessage());
        return (noType, noLength) -> Constants.EXCEPTION;
    }
}
