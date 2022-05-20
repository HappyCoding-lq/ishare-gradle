package top.dddpeter.ishare.system.domain;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 消息模板实体
 */
@Data
public class MsgTemplateInfo implements Serializable {

    private static final long serialVersionUID = -7372422774909605488L;

    @Id
    private String templateNo;

    private String templateName;

    private String templateType;

    private String templateInfo;

    private String channelId;

    private String channelTemplateNo;

    private String channelTemplateInfo;

    private String remark;

}
