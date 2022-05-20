package top.dddpeter.ishare.job.biz.client;

import top.dddpeter.ishare.job.biz.AdminBiz;
import top.dddpeter.ishare.job.biz.model.HandleCallbackParam;
import top.dddpeter.ishare.job.biz.model.RegistryParam;
import top.dddpeter.ishare.job.biz.model.ReturnT;
import top.dddpeter.ishare.job.util.JobRemotingUtil;

import java.util.List;

/**
 * admin api test
 *
 * @author hqins 2019-12-10
 */
public class AdminBizClient implements AdminBiz {

    public AdminBizClient() {
    }
    public AdminBizClient(String addressUrl, String accessToken) {
        this.addressUrl = addressUrl;
        this.accessToken = accessToken;

        // valid
        if (!this.addressUrl.endsWith("/")) {
            this.addressUrl = this.addressUrl + "/";
        }
    }

    private String addressUrl ;
    private String accessToken;


    @Override
    public ReturnT<String> callback(List<HandleCallbackParam> callbackParamList) {
        return JobRemotingUtil.postBody(addressUrl+"api/callback", accessToken, callbackParamList, 3);
    }

    @Override
    public ReturnT<String> registry(RegistryParam registryParam) {
        return JobRemotingUtil.postBody(addressUrl + "api/registry", accessToken, registryParam, 3);
    }

    @Override
    public ReturnT<String> registryRemove(RegistryParam registryParam) {
        return JobRemotingUtil.postBody(addressUrl + "api/registryRemove", accessToken, registryParam, 3);
    }
}
