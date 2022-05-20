package com.xxl.job.adminbiz;

import org.junit.Assert;
import org.junit.Test;
import top.dddpeter.ishare.job.biz.AdminBiz;
import top.dddpeter.ishare.job.biz.client.AdminBizClient;
import top.dddpeter.ishare.job.biz.model.HandleCallbackParam;
import top.dddpeter.ishare.job.biz.model.RegistryParam;
import top.dddpeter.ishare.job.biz.model.ReturnT;
import top.dddpeter.ishare.job.enums.RegistryConfig;

import java.util.Arrays;
import java.util.List;

/**
 * admin api test
 *
 * @author hqins 2019-12-10
 */
public class AdminBizTest {

    // admin-client
    private static String addressUrl = "http://127.0.0.1:8080/ishare-job-admin/";
    private static String accessToken = null;


    @Test
    public void callback() throws Exception {
        AdminBiz adminBiz = new AdminBizClient(addressUrl, accessToken);

        HandleCallbackParam param = new HandleCallbackParam();
        param.setLogId(1);
        param.setExecuteResult(ReturnT.SUCCESS);

        List<HandleCallbackParam> callbackParamList = Arrays.asList(param);

        ReturnT<String> returnT = adminBiz.callback(callbackParamList);

        Assert.assertTrue(returnT.getCode() == ReturnT.SUCCESS_CODE);
    }

    /**
     * registry executor
     *
     * @throws Exception
     */
    @Test
    public void registry() throws Exception {
        AdminBiz adminBiz = new AdminBizClient(addressUrl, accessToken);

        RegistryParam registryParam = new RegistryParam(RegistryConfig.RegistType.EXECUTOR.name(), "ishare-job-executor-example", "127.0.0.1:9999");
        ReturnT<String> returnT = adminBiz.registry(registryParam);

        Assert.assertTrue(returnT.getCode() == ReturnT.SUCCESS_CODE);
    }

    /**
     * registry executor remove
     *
     * @throws Exception
     */
    @Test
    public void registryRemove() throws Exception {
        AdminBiz adminBiz = new AdminBizClient(addressUrl, accessToken);

        RegistryParam registryParam = new RegistryParam(RegistryConfig.RegistType.EXECUTOR.name(), "ishare-job-executor-example", "127.0.0.1:9999");
        ReturnT<String> returnT = adminBiz.registryRemove(registryParam);

        Assert.assertTrue(returnT.getCode() == ReturnT.SUCCESS_CODE);

    }

}
