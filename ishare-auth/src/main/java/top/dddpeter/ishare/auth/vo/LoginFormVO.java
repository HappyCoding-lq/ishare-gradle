package top.dddpeter.ishare.auth.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "LoginFormVO", description = "登录接口响应体")
public class LoginFormVO implements Serializable  {

    private static final long serialVersionUID = 6034293642945489980L;

    @ApiModelProperty(value = "用户编号", required = true)
    private Long userId;

    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "token有效时间", required = true)
    private Long expire;

}
