package top.dddpeter.ishare.gateway.domain;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import top.dddpeter.ishare.common.annotation.EnumValid;
import top.dddpeter.ishare.common.annotation.JsonStrValid;
import top.dddpeter.ishare.common.annotation.UriStrValid;
import top.dddpeter.ishare.common.enums.JsonTypeEnum;
import top.dddpeter.ishare.common.utils.StringUtils;
import top.dddpeter.ishare.gateway.constant.CommonConstants;
import top.dddpeter.ishare.gateway.enums.CommonYesAndNoEnum;
import top.dddpeter.ishare.gateway.enums.RouteTypeEnum;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 网关路由定义
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/3/10 11:25 上午
 */
@Data
public class GatewayRouteDefine implements Serializable {

    private static final long serialVersionUID = 2383230354474128747L;

    /** 路由ID */
    @Id
    private String routeId;

    /** 路由转发uri */
    @UriStrValid(message = "路由转发目标传值不正确")
    private String routeUri;

    /** 路由信息描述 */
    @NotBlank(message = "路由信息描述不能为空")
    private String routeDesc;

    /** 路由类型 RouteTypeEnum */
    @EnumValid(message = "路由类型传值不正确", target = RouteTypeEnum.class)
    private String routeType;

    /** 登录/授权可用 YES-是 NO-否 */
    @EnumValid(message = "是否登录/授权可用传值不正确", target = CommonYesAndNoEnum.class, allowEmpty = true)
    private String needCheckAuth;

    /** 授权客户端，多个逗号分隔 */
    @NotBlank(message = "授权客户端传值不能为空", groups = {AuthClientsValidatorGroup.class})
    private String authClients;

    /** 是否启用IP白名单 YES-是 NO-否 */
    @EnumValid(message = "是否启用IP白名单传值不正确", target = CommonYesAndNoEnum.class, allowEmpty = true)
    private String trustedIpsEnable;

    /** 受信ip集合，支持Pattern正则表达 */
    @NotBlank(message = "受信ip不能为空", groups = {TrustedIpsValidatorGroup.class})
    private String trustedIps;

    /** 路由匹配优先级 */
    @NotNull(message = "路由匹配优先级不能为空")
    private Integer routeOrder;

    /** 断言字符串 */
    @JsonStrValid(message = "断言字符串传值不正确", jsonType = JsonTypeEnum.ARRAY, targetClass = PredicateDefinition.class)
    private String predicates;

    /** 过滤器字符串 */
    @JsonStrValid(message = "过滤器字符串传值不正确", jsonType = JsonTypeEnum.ARRAY, targetClass = FilterDefinition.class, allowEmpty = true)
    private String filters;

    /** 是否启用  YES-是 NO-否 */
    @EnumValid(message = "是否启用传值不正确", target = CommonYesAndNoEnum.class, allowEmpty = true)
    private String isEnable;

    /** 是否记录请求日志  YES-是 NO-否 */
    @EnumValid(message = "是否记录请求日志传值不正确", target = CommonYesAndNoEnum.class, allowEmpty = true)
    private String isLogAble;

    /** 对外api接口交易编码 */
    @NotBlank(message = "对外api接口交易编码不能为空", groups = {OpenApiValidatorGroup.class})
    private String apiTransCode;

    /** 是否API接口要素验证  YES-是 NO-否 */
    @EnumValid(message = "是否API接口要素验证传值不正确", target = CommonYesAndNoEnum.class, allowEmpty = true, groups = {OpenApiValidatorGroup.class})
    private String apiFactorsVerify;

    /** 是否能在页面进行修改  YES-是 NO-否 */
    @EnumValid(message = "pageManageOperateAble传值不正确", target = CommonYesAndNoEnum.class, allowEmpty = true)
    private String pageManageOperateAble;

    /** 创建时间 */
    private Date createTime;

    /** 修改时间 */
    private Date updateTime;

    public List<PredicateDefinition> getPredicateDefinition() {
        if (StringUtils.isNotEmpty(this.predicates)) {
            return JSON.parseArray(this.predicates, PredicateDefinition.class);
        } else {
            return new ArrayList<>();
        }
    }

    public List<FilterDefinition> getFilterDefinition() {
        if (StringUtils.isNotEmpty(this.filters)) {
            return JSON.parseArray(this.filters, FilterDefinition.class);
        } else {
            return new ArrayList<>();
        }
    }

    public void setDefaultFieldsValue(){
        if(StringUtils.isEmpty(needCheckAuth)){
            needCheckAuth = CommonYesAndNoEnum.YES.getCode();
            if(StringUtils.isEmpty(authClients)){
                authClients = CommonConstants.DEFAULT_CLIENT_APP_ID;
            }
        }
        if(StringUtils.isEmpty(trustedIpsEnable)){
            trustedIpsEnable = CommonYesAndNoEnum.NO.getCode();
        }
        if(StringUtils.isEmpty(isEnable)){
            isEnable = CommonYesAndNoEnum.NO.getCode();
        }
        if(StringUtils.isEmpty(isLogAble)){
            isLogAble = CommonYesAndNoEnum.NO.getCode();
        }
        if(StringUtils.isEmpty(apiFactorsVerify)){
            apiFactorsVerify = CommonYesAndNoEnum.NO.getCode();
        }
        if(StringUtils.isEmpty(pageManageOperateAble)){
            pageManageOperateAble = CommonYesAndNoEnum.YES.getCode();
        }
    }

}
