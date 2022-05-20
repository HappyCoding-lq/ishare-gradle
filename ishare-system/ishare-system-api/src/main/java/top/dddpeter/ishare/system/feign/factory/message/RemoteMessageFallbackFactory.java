package top.dddpeter.ishare.system.feign.factory.message;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.system.domain.EmailInfoDTO;
import top.dddpeter.ishare.system.domain.message.CommonSendEmail;
import top.dddpeter.ishare.system.domain.message.CommonSendSms;
import top.dddpeter.ishare.system.feign.message.RemoteMessageService;

@Slf4j
@Component
public class RemoteMessageFallbackFactory implements FallbackFactory<RemoteMessageService> {

    private static final String COMMON_ERROR_MSG = "remote system message service call exception";

    @Override
    public RemoteMessageService create(Throwable throwable) {
        log.error(throwable.getMessage());
        return new RemoteMessageService() {
            @Override
            public ResultDTO sendSms(CommonSendSms commonSendSms) {
                return ResultDTO.failure(COMMON_ERROR_MSG);
            }

            @Override
            public ResultDTO sendEmail(CommonSendEmail commonSendEmail) {
                return ResultDTO.failure(COMMON_ERROR_MSG);
            }

            @Override
            public ResultDTO sendVerifyEmail(EmailInfoDTO emailInfoDTO) {
                return ResultDTO.failure(COMMON_ERROR_MSG);
            }
        };
    }

}
