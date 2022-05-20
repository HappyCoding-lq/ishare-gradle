package top.dddpeter.ishare.gateway.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ClientInfoVO implements Serializable {
    private String appId;
    private String appSecret;
    private String appName;
    private String description;

}
