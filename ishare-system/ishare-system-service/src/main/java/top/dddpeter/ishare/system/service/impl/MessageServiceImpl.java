package top.dddpeter.ishare.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.common.exception.IShareException;
import top.dddpeter.ishare.common.utils.StringUtils;
import top.dddpeter.ishare.system.domain.EmailInfoDTO;
import top.dddpeter.ishare.system.domain.MsgChannel;
import top.dddpeter.ishare.system.domain.MsgInfo;
import top.dddpeter.ishare.system.domain.MsgTemplateInfo;
import top.dddpeter.ishare.system.mapper.MsgChannelMapper;
import top.dddpeter.ishare.system.mapper.MsgInfoMapper;
import top.dddpeter.ishare.system.mapper.MsgTemplateInfoMapper;
import top.dddpeter.ishare.system.message.stars.domain.StarsCommonReqDTO;
import top.dddpeter.ishare.system.message.stars.domain.StarsCommonResDTO;
import top.dddpeter.ishare.system.message.stars.util.StarsCallUtil;
import top.dddpeter.ishare.system.service.MessageService;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 消息发送
 */
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Resource
    private MsgChannelMapper msgChannelMapper;

    @Resource
    private MsgTemplateInfoMapper msgTemplateInfoMapper;

    @Resource
    private MsgInfoMapper msgInfoMapper;

    public void send(String templateNo, String receiver, Map<String, String> params){

        MsgInfo msgInfo = new MsgInfo();
        msgInfo.setCode(receiver);
        msgInfo.setTemplateNo(templateNo);
        msgInfo.setCreateDate(new Date());

        if(templateNo == null){
            msgInfo.setState(0);
            msgInfo.setStateMsg("消息发送失败，原因：【模板号不能为空】");
            this.msgInfoMapper.insert(msgInfo);
            throw new IShareException("消息发送失败，原因：【模板号不能为空】");
        }
        if(receiver == null){
            msgInfo.setState(0);
            msgInfo.setStateMsg("消息发送失败，原因：【接收人信息不能为空】");
            this.msgInfoMapper.insert(msgInfo);
            throw new IShareException("消息发送失败，原因：【接收人信息不能为空】");
        }
        if(params == null){
            msgInfo.setState(0);
            msgInfo.setStateMsg("消息发送失败，原因：【消息参数不能为空】");
            this.msgInfoMapper.insert(msgInfo);
            throw new IShareException("消息发送失败，原因：【消息参数不能为空】");
        }
        MsgTemplateInfo templateInfo = msgTemplateInfoMapper.selectByPrimaryKey(templateNo);
        if(templateInfo == null || StringUtils.isEmpty(templateInfo.getChannelId()) || StringUtils.isEmpty(templateInfo.getTemplateInfo())){
            log.error("消息模板查询异常，模板号:{}配置信息不存在或配置异常", templateNo);
            msgInfo.setState(0);
            msgInfo.setStateMsg("消息模板查询异常，模板号:"+templateNo+"配置信息不存在或配置异常");
            this.msgInfoMapper.insert(msgInfo);
            throw new IShareException("消息发送失败，原因：【消息模板配置异常】");
        }
        MsgChannel msgChannel = msgChannelMapper.selectByPrimaryKey(templateInfo.getChannelId());
        if(msgChannel == null || StringUtils.isEmpty(msgChannel.getUrl()) || StringUtils.isEmpty(msgChannel.getServiceClass())){
            log.error("消息通道查询异常，消息通道:{}配置信息不存在或配置异常", templateInfo.getChannelId());
            msgInfo.setState(0);
            msgInfo.setStateMsg("消息通道查询异常，消息通道:"+templateInfo.getChannelId()+"配置信息不存在或配置异常");
            this.msgInfoMapper.insert(msgInfo);
            throw new IShareException("消息发送失败，原因：【消息通道配置异常】");
        }
        String serviceClassName = msgChannel.getServiceClass();
        try {
            Class serviceClass = Class.forName(serviceClassName);
            Class[] argTypes=new Class[4];
            argTypes[0]=MsgChannel.class;
            argTypes[1]=MsgTemplateInfo.class;
            argTypes[2]=String.class;
            argTypes[3]=Map.class;
            Method method = serviceClass.getDeclaredMethod("send", argTypes);
            method.invoke(serviceClass.newInstance(), msgChannel, templateInfo, receiver, params);
            msgInfo.setState(1);
            msgInfo.setStateMsg("消息发送成功");
            this.msgInfoMapper.insert(msgInfo);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            if(e instanceof InvocationTargetException){
                Throwable targetException = ((InvocationTargetException) e).getTargetException();
                log.error("消息发送失败，原因：", targetException);
                if(targetException instanceof IShareException){
                    msgInfo.setState(0);
                    msgInfo.setStateMsg("消息发送失败");
                    this.msgInfoMapper.insert(msgInfo);
                    throw (IShareException)targetException;
                }
            }else {
                msgInfo.setState(0);
                msgInfo.setStateMsg("消息发送失败");
                this.msgInfoMapper.insert(msgInfo);
                log.error("消息发送失败，原因：", e);
            }
            msgInfo.setState(0);
            msgInfo.setStateMsg("发送失败");
            this.msgInfoMapper.insert(msgInfo);
            throw new IShareException("发送失败");
        }
    }

    @Override
    public ResultDTO sendEmail(EmailInfoDTO emailInfoDTO) {
        log.info("邮件发送开始， 参数信息，param = {}", JSONObject.toJSONString(emailInfoDTO));
        List<String> toEmailList = emailInfoDTO.getToEmailList();
        if (toEmailList.isEmpty()) {
            return ResultDTO.failure("收件人邮件地址不能为空。");
        }
        String templateNo = emailInfoDTO.getTemplateNo();
        if (StringUtils.isEmpty(templateNo)) {
            return ResultDTO.failure("模板编码不能为空。");
        }
        MsgTemplateInfo templateInfo = msgTemplateInfoMapper.selectByPrimaryKey(templateNo);
        if(templateInfo == null || StringUtils.isEmpty(templateInfo.getChannelId()) || StringUtils.isEmpty(templateInfo.getTemplateInfo())){
            log.error("消息模板查询异常，模板号:{}配置信息不存在或配置异常", templateNo);
            return ResultDTO.failure("消息模板查询异常，模板号:" + templateNo + "配置信息不存在或配置异常。");
        }

        String title = String.format(templateInfo.getChannelTemplateInfo(), emailInfoDTO.getBusinessTypeMsg());
        String content = String.format(templateInfo.getTemplateInfo(), emailInfoDTO.getCustomerName(), emailInfoDTO.getBusinessTypeMsg());

        MsgChannel msgChannel = msgChannelMapper.selectByPrimaryKey(templateInfo.getChannelId());
        if(msgChannel == null || StringUtils.isEmpty(msgChannel.getUrl()) || StringUtils.isEmpty(msgChannel.getServiceClass())){
            log.error("消息通道查询异常，消息通道:{}配置信息不存在或配置异常", templateInfo.getChannelId());
            return ResultDTO.failure("消息发送失败，原因：【消息通道配置异常】");
        }

        StarsCommonReqDTO reqDTO = new StarsCommonReqDTO();
        reqDTO.setProduct(msgChannel.getTopic());
        reqDTO.setApplication(msgChannel.getAppId());
        reqDTO.setAppKey(msgChannel.getAppSecret());
        reqDTO.setTemplateNo(emailInfoDTO.getTemplateNo());

        Map<String, String> paramStr = new HashMap<>();
        paramStr.put("all", content);
        reqDTO.setParam(paramStr);

        Map<String, Object> contentStr = new HashMap<>();
        contentStr.put("toMails", String.join(",", emailInfoDTO.getToEmailList()));
        contentStr.put("title", title);
        reqDTO.setContent(contentStr);

        StarsCommonResDTO callResult = StarsCallUtil.toHttpPost(msgChannel.getUrl(), reqDTO);
        if(callResult.getSuccess()==null || !callResult.getSuccess()){
            log.error("邮件发送失败， error = {}", StringUtils.isEmpty(callResult.getMsg()) ? "返回结果为空" : callResult.getMsg());
            return ResultDTO.failure(StringUtils.isEmpty(callResult.getMsg()) ? "邮件发送失败,返回结果为空" : callResult.getMsg());
        }
        return ResultDTO.success(StringUtils.isEmpty(callResult.getMsg()) ? "邮件发送成功" : callResult.getMsg());
    }

}
