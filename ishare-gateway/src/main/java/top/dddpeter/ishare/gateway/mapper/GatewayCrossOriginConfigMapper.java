package top.dddpeter.ishare.gateway.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.dddpeter.ishare.common.core.dao.BaseMapper;
import top.dddpeter.ishare.gateway.domain.GatewayCrossOriginConfig;

/**
 * 网关跨域配置mapper
 *
 * @author : huangshuanbao
 */
@Mapper
public interface GatewayCrossOriginConfigMapper extends BaseMapper<GatewayCrossOriginConfig> {

}
