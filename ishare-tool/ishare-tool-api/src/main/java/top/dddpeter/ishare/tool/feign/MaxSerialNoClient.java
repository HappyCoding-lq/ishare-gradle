package top.dddpeter.ishare.tool.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.dddpeter.ishare.common.constant.ServiceNameConstants;
import top.dddpeter.ishare.tool.feign.factory.MaxSerialNoClientFallbackFactory;

/**
 * @author hqins 2020-01-03
 * 获取序列号当前最大号
 */

@FeignClient(name = ServiceNameConstants.TOOL_SERVICE, fallbackFactory = MaxSerialNoClientFallbackFactory.class)
public interface MaxSerialNoClient {
    @GetMapping("/maxNo/createMaxNo/{noType}/{noLength}")
    String get(@PathVariable("noType") String noType, @PathVariable("noLength") int noLength);
}