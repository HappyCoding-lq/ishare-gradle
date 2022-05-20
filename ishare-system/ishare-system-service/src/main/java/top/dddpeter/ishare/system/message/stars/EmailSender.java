package top.dddpeter.ishare.system.message.stars;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import top.dddpeter.ishare.common.exception.IShareException;
import top.dddpeter.ishare.common.utils.StringUtils;
import top.dddpeter.ishare.common.utils.spring.SpringUtils;
import top.dddpeter.ishare.system.domain.MsgChannel;
import top.dddpeter.ishare.system.domain.MsgTemplateInfo;
import top.dddpeter.ishare.system.message.IMessageSender;
import top.dddpeter.ishare.system.message.stars.domain.StarsCommonReqDTO;
import top.dddpeter.ishare.system.message.stars.domain.StarsCommonResDTO;
import top.dddpeter.ishare.system.message.stars.util.StarsCallUtil;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 满天星邮件发送
 */
@Slf4j
public class EmailSender implements IMessageSender {

    @Override
    public void send(MsgChannel channel, MsgTemplateInfo templateInfo, String receiver, Map<String, Object> params) {
        this.check(channel, templateInfo, receiver, params);
        Configuration configuration = new Configuration();
        StringWriter out = new StringWriter();
        try {
            Template template = new Template("template", new StringReader(templateInfo.getTemplateInfo()), configuration);
            template.process(params, out);
            String content = out.toString();
            StarsCommonReqDTO reqDTO = new StarsCommonReqDTO();
            reqDTO.setProduct(channel.getTopic());
            reqDTO.setApplication(channel.getAppId());
            reqDTO.setAppKey(channel.getAppSecret());
            reqDTO.setTemplateNo(templateInfo.getChannelTemplateNo());
            Map<String, Object> map = new HashMap<>();
            map.put("toMails", receiver);
            map.put("title", params.get("title"));
            map.put("fileList", params.get("fileList"));
            reqDTO.setContent(map);
            reqDTO.setParam(Collections.singletonMap("content", content));
            String envStr = SpringUtils.getActiveProfile();
            if(StringUtils.isNotEmpty(envStr) && !"prd".equalsIgnoreCase(envStr) && !Boolean.TRUE.equals(channel.getRealSend())){
                log.debug("调用满天星邮件发送[未真实发送]：mails: {}, params: {}", reqDTO.getContent(), reqDTO.getParam());
                return;
            }
            StarsCommonResDTO callResult = StarsCallUtil.toHttpPost(channel.getUrl(), reqDTO);
            if(callResult.getSuccess()==null || !callResult.getSuccess()){
                String errorMsg = "";
                if(callResult.getValue()!=null && StringUtils.isNotEmpty(callResult.getValue().toString())){
                    errorMsg = String.format("【%s】", callResult.getValue().toString());
                }
                throw new IShareException("邮件发送失败" + errorMsg);
            }
        } catch (Exception e) {
            log.error("满天星邮件发送失败，原因：", e);
            if(e instanceof IShareException){
                throw (IShareException) e;
            }else {
                throw new IShareException("邮件发送失败");
            }
        }
    }

    private void check(MsgChannel channel, MsgTemplateInfo templateInfo, String receiver, Map<String, Object> params){
        if(StringUtils.isEmpty(templateInfo.getChannelTemplateNo())
                || StringUtils.isEmpty(templateInfo.getChannelTemplateInfo())){
            log.error("邮件模板号:{}配置异常", templateInfo.getTemplateNo());
            throw new IShareException("邮件发送失败，原因：【邮件模板配置异常】");
        }
        if(StringUtils.isEmpty(channel.getAppId())
                || StringUtils.isEmpty(channel.getAppSecret())
                || StringUtils.isEmpty(channel.getTopic())){
            log.error("邮件发送通道:{}配置异常", templateInfo.getChannelId());
            throw new IShareException("邮件发送失败，原因：【邮件发送通道配置异常】");
        }
        Object title = params.get("title");
        if(title == null || title.toString().length()<=0){
            throw new IShareException("邮件发送失败: 邮件标题不能为空");
        }
    }

}
