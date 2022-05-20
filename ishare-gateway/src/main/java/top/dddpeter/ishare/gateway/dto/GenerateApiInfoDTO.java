package top.dddpeter.ishare.gateway.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class GenerateApiInfoDTO implements Serializable {

    private static final long serialVersionUID = 7006723020778765423L;

    /** 微服务名 */
    @NotBlank(message = "微服务名不能为空")
    private String serviceId;

    /** 微服务接口path */
    @NotBlank(message = "微服务接口路径不能为空")
    @Pattern(message = "微服务接口路径不合法", regexp = "^(/[A-Za-z0-9\\-_]+)+")
    private String servicePath;

}
