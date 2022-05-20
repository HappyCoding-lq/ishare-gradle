package top.dddpeter.ishare.gateway.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class GenerateApiInfoVO implements Serializable {

    private static final long serialVersionUID = -5867363867845404331L;

    /** 接口 api path */
    private String apiPath;

    /** api接口编码 */
    private String apiTransCode;

    /** tips */
    private String tips;

}
