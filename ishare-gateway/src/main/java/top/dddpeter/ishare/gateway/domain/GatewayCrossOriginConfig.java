package top.dddpeter.ishare.gateway.domain;

import lombok.Data;
import top.dddpeter.ishare.common.annotation.UriStrValid;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 网关跨域配置
 *
 * @author : huangshuanbao
 */
@Data
public class GatewayCrossOriginConfig implements Serializable {

    private static final long serialVersionUID = 8943555766537852343L;

    /** 主键ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 请求路径 */
    @NotBlank(message = "请求路径不能为空")
    private String path;

    /** 允许的域 */
    @UriStrValid(message = "允许的域传值不正确")
    private String allowOrigin;

    /** 描述 */
    @NotBlank(message = "描述不能为空")
    private String description;

    /** 创建时间 */
    private Date createTime;

    /** 修改时间 */
    private Date updateTime;

}
