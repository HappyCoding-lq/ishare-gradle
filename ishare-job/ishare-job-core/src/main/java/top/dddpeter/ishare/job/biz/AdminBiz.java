package top.dddpeter.ishare.job.biz;

import top.dddpeter.ishare.job.biz.model.HandleCallbackParam;
import top.dddpeter.ishare.job.biz.model.RegistryParam;
import top.dddpeter.ishare.job.biz.model.ReturnT;

import java.util.List;

/**
 * @author hqins 2019-12-10
 */
public interface AdminBiz {


    // ---------------------- callback ----------------------

    /**
     * callback
     *
     * @param callbackParamList
     * @return
     */
    public ReturnT<String> callback(List<HandleCallbackParam> callbackParamList);


    // ---------------------- registry ----------------------

    /**
     * registry
     *
     * @param registryParam
     * @return
     */
    public ReturnT<String> registry(RegistryParam registryParam);

    /**
     * registry remove
     *
     * @param registryParam
     * @return
     */
    public ReturnT<String> registryRemove(RegistryParam registryParam);

}
