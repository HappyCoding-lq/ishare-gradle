package top.dddpeter.ishare.system.message.stars.domain;

import lombok.Data;

import java.util.Map;

/**
 * 满天星短信/邮件发送请求体定义
 */
@Data
public class StarsCommonReqDTO {

    private String product;

    private String application;

    private String appKey;

    private String templateNo;

    private Map<String, String> param;

    private Map<String, Object> content;

}
