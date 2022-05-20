package top.dddpeter.ishare.system.message;

import top.dddpeter.ishare.system.domain.MsgChannel;
import top.dddpeter.ishare.system.domain.MsgTemplateInfo;

import java.util.Map;

public interface IMessageSender {

    void send(MsgChannel channel, MsgTemplateInfo templateInfo, String receiver, Map<String, Object> params);

}
