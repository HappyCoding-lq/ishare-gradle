package top.dddpeter.ishare.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;
import top.dddpeter.ishare.gateway.domain.GatewayCrossOriginConfig;
import top.dddpeter.ishare.gateway.filter.CrossWebFilter;
import top.dddpeter.ishare.gateway.mapper.GatewayCrossOriginConfigMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ChinaPost-life
 */
@Configuration
public class CrossConfig {

    @Bean
    public WebFilter corsFilter() {
        return new CrossWebFilter();
    }

    @Bean("myCrossOriginMap")
    public Map<String, List<GatewayCrossOriginConfig>> cacheCrossOriginConfig(GatewayCrossOriginConfigMapper crossOriginConfigMapper) {
        Map<String, List<GatewayCrossOriginConfig>> map = new HashMap<>();
        List<GatewayCrossOriginConfig> configList = crossOriginConfigMapper.selectAll();
        configList.forEach(gatewayCrossOriginConfig->{
            map.computeIfAbsent(gatewayCrossOriginConfig.getPath(), k -> new ArrayList<>());
            map.get(gatewayCrossOriginConfig.getPath()).add(gatewayCrossOriginConfig);
        });
        return map;
    }

}
