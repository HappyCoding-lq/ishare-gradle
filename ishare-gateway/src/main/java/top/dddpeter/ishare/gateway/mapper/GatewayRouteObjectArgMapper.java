package top.dddpeter.ishare.gateway.mapper;

import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.Mapper;
import top.dddpeter.ishare.gateway.domain.GatewayRouteObjectArg;

/**
 * 网关路由对象参数定义mapper
 */
public interface GatewayRouteObjectArgMapper extends Mapper<GatewayRouteObjectArg>, InsertListMapper<GatewayRouteObjectArg>, ConditionMapper<GatewayRouteObjectArg> {

}
