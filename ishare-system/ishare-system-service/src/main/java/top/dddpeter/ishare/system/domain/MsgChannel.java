package top.dddpeter.ishare.system.domain;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 消息通道实体
 */
@Data
public class MsgChannel implements Serializable {

    private static final long serialVersionUID = 5595571077699303480L;

    @Id
    private String channelId;

    private String channelName;

    private String url;

    private String appId;

    private String appSecret;

    private String topic;

    private String serviceClass;

    private Boolean realSend;

}
