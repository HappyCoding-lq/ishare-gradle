package top.dddpeter.ishare.system.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 消息发送表(SmsInfo)实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MsgInfo implements Serializable {
    private static final long serialVersionUID = -37098681201049472L;
    /**
    * 自增主键
    */
    @Id
    private Integer id;
    /**
    * 手机号/邮箱号
    */
    private String code;
    /**
    * 消息内容
    */
    private String templateNo;
    /**
    * 状态
    */
    private Integer state;
    /**
     * 状态结果描述
     */
    private String stateMsg;
    /**
    * 创建时间
    */
    private Date createDate;

}