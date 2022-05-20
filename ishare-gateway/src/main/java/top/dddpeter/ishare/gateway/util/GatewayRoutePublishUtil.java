package top.dddpeter.ishare.gateway.util;

import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.handler.predicate.RoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import top.dddpeter.ishare.common.exception.IShareException;
import top.dddpeter.ishare.common.utils.JsonTrans;
import top.dddpeter.ishare.common.utils.StringUtils;
import top.dddpeter.ishare.common.utils.ValidatorUtils;
import top.dddpeter.ishare.gateway.domain.AuthClientsValidatorGroup;
import top.dddpeter.ishare.gateway.domain.GatewayRouteDefine;
import top.dddpeter.ishare.gateway.domain.OpenApiValidatorGroup;
import top.dddpeter.ishare.gateway.domain.TrustedIpsValidatorGroup;
import top.dddpeter.ishare.gateway.enums.CommonYesAndNoEnum;
import top.dddpeter.ishare.gateway.enums.RouteTypeEnum;

import javax.annotation.Resource;
import javax.validation.ValidationException;
import java.util.List;

/**
 * 自定义路由工具类
 *
 * @author : huangshuanbao
 */
@Component
public class GatewayRoutePublishUtil {

    @Resource
    List<GatewayFilterFactory> filterFactories;

    @Resource
    List<RoutePredicateFactory> routePredicateFactories;

    public void publishCheckDefine(GatewayRouteDefine gatewayRouteDefine){
        if(StringUtils.isEmpty(gatewayRouteDefine.getRouteId())){
            throw new IShareException("路由ID不能为空");
        }
        ValidatorUtils.validateEntity(gatewayRouteDefine);
        if (CommonYesAndNoEnum.YES.getCode().equalsIgnoreCase(gatewayRouteDefine.getTrustedIpsEnable())) {
            ValidatorUtils.validateEntity(gatewayRouteDefine, TrustedIpsValidatorGroup.class);
        }
        if (RouteTypeEnum.OPEN_API.getCode().equalsIgnoreCase(gatewayRouteDefine.getRouteType())) {
            ValidatorUtils.validateEntity(gatewayRouteDefine, OpenApiValidatorGroup.class);
        }
        if (CommonYesAndNoEnum.YES.getCode().equalsIgnoreCase(gatewayRouteDefine.getNeedCheckAuth())) {
            ValidatorUtils.validateEntity(gatewayRouteDefine, AuthClientsValidatorGroup.class);
        }
        //断言校验
        List<PredicateDefinition> predicateDefinitionList = gatewayRouteDefine.getPredicateDefinition();
        if(StringUtils.isEmpty(predicateDefinitionList)){
            throw new IShareException("断言配置不能为空");
        }
        boolean predicatePass = true;
        PredicateDefinition errorPredicateDefinition = null;
        for (PredicateDefinition predicateDefinition : predicateDefinitionList) {
            predicatePass = routePredicateFactories.stream().anyMatch( routePredicateFactory-> routePredicateFactory.name().equals(predicateDefinition.getName()));
            if (!predicatePass) {
                errorPredicateDefinition = predicateDefinition;
                break;
            }
        }
        if (!predicatePass) {
            throw new IShareException("断言" + errorPredicateDefinition.getName() + "不存在");
        }
        //过滤器校验
        List<FilterDefinition> filterDefinitionList = gatewayRouteDefine.getFilterDefinition();
        boolean filterPass = true;
        FilterDefinition errorFilterDefinition = null;
        for (FilterDefinition filterDefinition : filterDefinitionList) {
            filterPass = filterFactories.stream().anyMatch(gatewayFilterFactory -> gatewayFilterFactory.name().equals(filterDefinition.getName()));
            if (!filterPass) {
                errorFilterDefinition = filterDefinition;
                break;
            }
        }
        if (!filterPass) {
            throw new IShareException("过滤器" + errorFilterDefinition.getName() + "不存在");
        }
    }

    public void checkPredicateStr(String predicateStr){
        if(StringUtils.isEmpty(predicateStr)){
            throw new IShareException("断言JSON字符串不能为空");
        }
        PredicateDefinition definition = JsonTrans.fromJson(predicateStr, PredicateDefinition.class);
        if(StringUtils.isEmpty(definition.getName())){
            throw new IShareException("断言器名字不能为空");
        }
        RoutePredicateFactory factory = routePredicateFactories.stream().filter( routePredicateFactory-> routePredicateFactory.name().equals(definition.getName())).findAny().orElse(null);
        if(factory == null){
            throw new IShareException("断言" + definition.getName() + "不存在");
        }
        Class configClass = factory.getConfigClass();
        if (configClass.getAnnotation(Validated.class) != null) {
            Object obj = JsonTrans.fromJson(JsonTrans.objectToJson(definition.getArgs()), configClass);
            try {
                ValidatorUtils.validateEntity(obj);
            }catch (ValidationException ex){
                throw new IShareException("断言参数配置异常，请检查好数据再提交");
            }
        }
    }

    public void checkFilterStr(String filterStr){
        if(StringUtils.isEmpty(filterStr)){
            throw new IShareException("过滤器JSON字符串不能为空");
        }
        FilterDefinition definition = JsonTrans.fromJson(filterStr, FilterDefinition.class);
        if(StringUtils.isEmpty(definition.getName())){
            throw new IShareException("过滤器名字不能为空");
        }
        GatewayFilterFactory factory = filterFactories.stream().filter( filterFactory-> filterFactory.name().equals(definition.getName())).findAny().orElse(null);
        if (factory == null) {
            throw new IShareException("过滤器" + definition.getName() + "不存在");
        }
        Class configClass = factory.getConfigClass();
        if (configClass.getAnnotation(Validated.class) != null) {
            Object obj = JsonTrans.fromJson(JsonTrans.objectToJson(definition.getArgs()), configClass);
            try {
                ValidatorUtils.validateEntity(obj);
            }catch (ValidationException ex){
                throw new IShareException("过滤器参数配置异常，请检查好数据再提交");
            }
        }
    }

}
