package top.dddpeter.ishare.job.admin.core.route.strategy;

import top.dddpeter.ishare.job.admin.core.route.ExecutorRouter;
import top.dddpeter.ishare.job.biz.model.ReturnT;
import top.dddpeter.ishare.job.biz.model.TriggerParam;

import java.util.List;
import java.util.Random;

/**
 *  @Author hqins 2019-12-10
 */
public class ExecutorRouteRandom extends ExecutorRouter {

    private static Random localRandom = new Random();

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        String address = addressList.get(localRandom.nextInt(addressList.size()));
        return new ReturnT<String>(address);
    }

}
