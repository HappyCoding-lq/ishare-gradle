package top.dddpeter.ishare.gateway.domain;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 网关路由对象定义
 */
@Data
public class GatewayRouteObject implements Serializable {

    private static final long serialVersionUID = 4956766826074247452L;

    /** 关键字 */
    @Id
    private String objectName;

    /** 描述 */
    private String objectDesc;

    /** 对象类型 predicate: 断言 filter: 过滤器 */
    private String objectType;

    /** 子类型 inset:内置 develop:自定义开发 */
    private String objectSubType;

    /** className */
    private String objectClassName;

    /** 说明/提示信息 */
    private String objectTips;

    /** 获取参数方式 Map、List */
    private String objectArgType;

    /** 显示顺序 */
    private Integer sortNum;

    /** 参数信息 */
    @Transient
    private List<GatewayRouteObjectArg> objectArgsDefine;

    /** 创建时间 */
    private Date createTime;

    /** 修改时间 */
    private Date updateTime;

}
