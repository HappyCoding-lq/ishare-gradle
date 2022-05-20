package top.dddpeter.ishare.gateway.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.dddpeter.ishare.common.core.dao.BaseMapper;
import top.dddpeter.ishare.gateway.domain.GatewayRouteObject;

/**
 * 网关路由对象定义mapper
 */
@Mapper
public interface GatewayRouteObjectMapper extends BaseMapper<GatewayRouteObject> {

}
