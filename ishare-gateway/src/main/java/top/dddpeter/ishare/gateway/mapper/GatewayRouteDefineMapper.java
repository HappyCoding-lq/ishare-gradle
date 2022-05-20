package top.dddpeter.ishare.gateway.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.dddpeter.ishare.common.core.dao.BaseMapper;
import top.dddpeter.ishare.gateway.domain.GatewayRouteDefine;

/**
 * 网关路由定义mapper
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/3/10 11:24 上午
 */
@Mapper
public interface GatewayRouteDefineMapper extends BaseMapper<GatewayRouteDefine> {

    @Select("select count(1) from gateway_route_define where JSON_CONTAINS(predicates, JSON_OBJECT('args', JSON_OBJECT('pattern', #{apiPath})))")
    int countApiPath(@Param("apiPath") String apiPath);

}
