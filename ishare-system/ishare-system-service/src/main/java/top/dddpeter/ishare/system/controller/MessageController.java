package top.dddpeter.ishare.system.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.system.domain.CommonSendEmailDTO;
import top.dddpeter.ishare.system.domain.CommonSendSmsDTO;
import top.dddpeter.ishare.system.domain.EmailInfoDTO;
import top.dddpeter.ishare.system.service.MessageService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.stream.Collectors;

/**
 * 消息发送controller
 */
@RestController
@RequestMapping("/message")
@Slf4j
public class MessageController {

    @Resource
    private MessageService messageService;

    @PostMapping("/common/sendSms")
    public ResultDTO sendSms(@RequestBody @Valid CommonSendSmsDTO smsDTO) {
        messageService.send(smsDTO.getTemplateNo(), smsDTO.getPhonesList().stream().collect(Collectors.joining(",")), smsDTO.getParam());
        return ResultDTO.success("短信发送成功");
    }

    @PostMapping("/common/sendEmail")
    public ResultDTO sendEmail(@RequestBody @Valid CommonSendEmailDTO emailDTO) {
        messageService.send(emailDTO.getTemplateNo(), emailDTO.getEmailList().stream().collect(Collectors.joining(",")), emailDTO.getParam());
        return ResultDTO.success("邮件发送成功");
    }

    @PostMapping("/common/sendVerifyEmail")
    public ResultDTO sendVerifyEmail(@RequestBody EmailInfoDTO emailInfoDTO) {
        return this.messageService.sendEmail(emailInfoDTO);
    }

}
