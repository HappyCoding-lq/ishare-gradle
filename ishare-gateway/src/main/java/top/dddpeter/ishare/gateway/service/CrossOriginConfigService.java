package top.dddpeter.ishare.gateway.service;

import top.dddpeter.ishare.gateway.domain.GatewayCrossOriginConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 跨域配置service层
 */
public interface CrossOriginConfigService {

    List<GatewayCrossOriginConfig> configList(GatewayCrossOriginConfig crossOriginConfig);

    int configListCount(GatewayCrossOriginConfig crossOriginConfig);

    void refreshConfig();

    Long addConfig(GatewayCrossOriginConfig crossOriginConfig);

    void updateConfig(GatewayCrossOriginConfig crossOriginConfig);

    void deleteConfig(String ids);

    void reloadConfig(String id);

    Set<Map<String, String>> pathList(String path);

    List<GatewayCrossOriginConfig> pathCrossList(String path);

    String deletePathCross(String path);

}
