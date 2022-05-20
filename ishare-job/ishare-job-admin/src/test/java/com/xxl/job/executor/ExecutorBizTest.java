package com.xxl.job.executor;

import top.dddpeter.ishare.job.biz.ExecutorBiz;
import top.dddpeter.ishare.job.biz.model.ReturnT;
import top.dddpeter.ishare.job.biz.model.TriggerParam;
import top.dddpeter.ishare.job.enums.ExecutorBlockStrategyEnum;
import top.dddpeter.ishare.job.glue.GlueTypeEnum;
import top.dddpeter.ishare.job.rpc.remoting.invoker.RpcInvokerFactory;
import top.dddpeter.ishare.job.rpc.remoting.invoker.call.CallType;
import top.dddpeter.ishare.job.rpc.remoting.invoker.reference.RpcReferenceBean;
import top.dddpeter.ishare.job.rpc.remoting.invoker.route.LoadBalance;
import top.dddpeter.ishare.job.rpc.remoting.net.impl.netty_http.client.NettyHttpClient;
import top.dddpeter.ishare.job.rpc.serialize.impl.HessianSerializer;

/**
 * executor-api client, test
 *
 *  @Author hqins 2019-12-10
 */
public class ExecutorBizTest {

    public static void main(String[] args) throws Exception {

        // param
        String jobHandler = "demoJobHandler";
        String params = "";

        runTest(jobHandler, params);
    }

    /**
     * run jobhandler
     *
     * @param jobHandler
     * @param params
     */
    private static void runTest(String jobHandler, String params) throws Exception {
        // trigger data
        TriggerParam triggerParam = new TriggerParam();
        triggerParam.setJobId(1);
        triggerParam.setExecutorHandler(jobHandler);
        triggerParam.setExecutorParams(params);
        triggerParam.setExecutorBlockStrategy(ExecutorBlockStrategyEnum.COVER_EARLY.name());
        triggerParam.setGlueType(GlueTypeEnum.BEAN.name());
        triggerParam.setGlueSource(null);
        triggerParam.setGlueUpdatetime(System.currentTimeMillis());
        triggerParam.setLogId(1);
        triggerParam.setLogDateTime(System.currentTimeMillis());

        // do remote trigger
        String accessToken = null;

        RpcReferenceBean referenceBean = new RpcReferenceBean();
        referenceBean.setClient(NettyHttpClient.class);
        referenceBean.setSerializer(HessianSerializer.class);
        referenceBean.setCallType(CallType.SYNC);
        referenceBean.setLoadBalance(LoadBalance.ROUND);
        referenceBean.setIface(ExecutorBiz.class);
        referenceBean.setVersion(null);
        referenceBean.setTimeout(3000);
        referenceBean.setAddress("127.0.0.1:9999");
        referenceBean.setAccessToken(accessToken);
        referenceBean.setInvokeCallback(null);
        referenceBean.setInvokerFactory(null);

        ExecutorBiz executorBiz = (ExecutorBiz) referenceBean.getObject();

        ReturnT<String> runResult = executorBiz.run(triggerParam);

        System.out.println(runResult);
        RpcInvokerFactory.getInstance().stop();
    }

}
