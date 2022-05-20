package top.dddpeter.ishare.gateway.domain;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 网关路由对象参数定义
 */
@Data
public class GatewayRouteObjectArg implements Serializable {

    private static final long serialVersionUID = 6890177834535789578L;

    /** 从属对象关键字 */
    @Id
    private String objectName;

    /** 关键字 */
    @Id
    private String argName;

    /** 参数描述 */
    private String argDesc;

    /** 是否必须 true-是 fasle-否 */
    private Boolean required;

    /** 说明/提示信息 */
    private String argTips;

    /** 参数值控件类型  input-输入   select-选择   fixed-固定值 */
    private String argValueControlType;

    /** 参数值范围 json arry 例如["xxx","bbbb"]  input类型不配置或正则 input和fixed只支持取第一个数据 */
    private String argValueScope;

    /** 显示顺序 */
    private Integer sortNum;

    /** 创建时间 */
    private Date createTime;

    /** 修改时间 */
    private Date updateTime;

}
