package top.dddpeter.ishare.system.feign.message;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.dddpeter.ishare.common.constant.ServiceNameConstants;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.system.domain.EmailInfoDTO;
import top.dddpeter.ishare.system.domain.message.CommonSendEmail;
import top.dddpeter.ishare.system.domain.message.CommonSendSms;
import top.dddpeter.ishare.system.feign.factory.message.RemoteMessageFallbackFactory;

/**
 * 消息相关 Feign服务层
 */
@FeignClient(name = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteMessageFallbackFactory.class)
public interface RemoteMessageService {

    @PostMapping("/message/common/sendSms")
    ResultDTO sendSms(@RequestBody CommonSendSms commonSendSms);

    @PostMapping("/message/common/sendEmail")
    ResultDTO sendEmail(@RequestBody CommonSendEmail commonSendEmail);

    @PostMapping("/message/common/sendVerifyEmail")
    ResultDTO sendVerifyEmail(@RequestBody EmailInfoDTO emailInfoDTO);

}
