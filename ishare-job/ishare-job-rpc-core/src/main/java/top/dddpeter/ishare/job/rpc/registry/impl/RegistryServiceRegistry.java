package top.dddpeter.ishare.job.rpc.registry.impl;

import top.dddpeter.ishare.job.client.RegistryClient;
import top.dddpeter.ishare.job.client.model.RegistryDataParamVO;
import top.dddpeter.ishare.job.rpc.registry.ServiceRegistry;

import java.util.*;

/**
 * service registry for "ishare-registry v1.0.1"
 *
 * @author hqins 2019-12-10
 */
public class RegistryServiceRegistry extends ServiceRegistry {

    public static final String XXL_REGISTRY_ADDRESS = "XXL_REGISTRY_ADDRESS";
    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String BIZ = "BIZ";
    public static final String ENV = "ENV";

    private RegistryClient registryClient;
    public RegistryClient getRegistryClient() {
        return registryClient;
    }

    @Override
    public void start(Map<String, String> param) {
        String xxlRegistryAddress = param.get(XXL_REGISTRY_ADDRESS);
        String accessToken = param.get(ACCESS_TOKEN);
        String biz = param.get(BIZ);
        String env = param.get(ENV);

        // fill
        biz = (biz!=null&&biz.trim().length()>0)?biz:"default";
        env = (env!=null&&env.trim().length()>0)?env:"default";

        registryClient = new RegistryClient(xxlRegistryAddress, accessToken, biz, env);
    }

    @Override
    public void stop() {
        if (registryClient != null) {
            registryClient.stop();
        }
    }

    @Override
    public boolean registry(Set<String> keys, String value) {
        if (keys==null || keys.size() == 0 || value == null) {
            return false;
        }

        // init
        List<RegistryDataParamVO> registryDataList = new ArrayList<>();
        for (String key:keys) {
            registryDataList.add(new RegistryDataParamVO(key, value));
        }

        return registryClient.registry(registryDataList);
    }

    @Override
    public boolean remove(Set<String> keys, String value) {
        if (keys==null || keys.size() == 0 || value == null) {
            return false;
        }

        // init
        List<RegistryDataParamVO> registryDataList = new ArrayList<>();
        for (String key:keys) {
            registryDataList.add(new RegistryDataParamVO(key, value));
        }

        return registryClient.remove(registryDataList);
    }

    @Override
    public Map<String, TreeSet<String>> discovery(Set<String> keys) {
        return registryClient.discovery(keys);
    }

    @Override
    public TreeSet<String> discovery(String key) {
        return registryClient.discovery(key);
    }

}
