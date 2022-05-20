package top.dddpeter.ishare.system.message.stars.domain;

import lombok.Data;

/**
 * 满天星短信/邮件发送请求体定义
 */
@Data
public class StarsCommonResDTO {

    private Boolean success;

    private String msg;

    private Object value;

}
