package top.dddpeter.ishare.gateway.config;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CachePenetrationProtect;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.dddpeter.ishare.gateway.mapper.ClientInfoMapper;
import top.dddpeter.ishare.gateway.vo.ClientInfoVO;

import javax.annotation.Resource;
import java.util.List;

/**
 * druid 配置多数据源
 *
 * @author ishare
 */
@Configuration
@Slf4j
public class ClientConfig {
    @CreateCache(name = "clientInfoCache", expire = 1000 * 30 * 60,cacheType = CacheType.BOTH)
    @CachePenetrationProtect
    private Cache<String, ClientInfoVO> clientInfoCache;
    @Resource
    ClientInfoMapper clientInfoMapper;


    @Bean(name = "clientInfoCache")
    public Cache<String, ClientInfoVO> clientInfoCache(){
        List<ClientInfoVO> clientInfoVOList = clientInfoMapper.getAllClient();
        for(ClientInfoVO clientInfo:clientInfoVOList) {
            log.info("缓存客户端信息:{}", clientInfo.getAppId());
            clientInfoCache.put(clientInfo.getAppId(), clientInfo);
        }
        return clientInfoCache;
    }

}
