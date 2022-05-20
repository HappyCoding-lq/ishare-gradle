package top.dddpeter.ishare.system.service;

import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.system.domain.EmailInfoDTO;

import java.util.Map;

/**
 * 消息处理业务层定义
 */
public interface MessageService {

    /**
     * 消息发送
     */
    void send(String templateNo, String receiver, Map<String, String> params);

    /**
     * @description 发送邮件
     * @author Yoko
     */
    ResultDTO sendEmail(EmailInfoDTO emailInfoDTO);

}
