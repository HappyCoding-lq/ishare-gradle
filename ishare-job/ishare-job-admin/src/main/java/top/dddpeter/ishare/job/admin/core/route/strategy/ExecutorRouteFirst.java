package top.dddpeter.ishare.job.admin.core.route.strategy;

import top.dddpeter.ishare.job.admin.core.route.ExecutorRouter;
import top.dddpeter.ishare.job.biz.model.ReturnT;
import top.dddpeter.ishare.job.biz.model.TriggerParam;

import java.util.List;

/**
 *  @Author hqins 2019-12-10
 */
public class ExecutorRouteFirst extends ExecutorRouter {

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList){
        return new ReturnT<String>(addressList.get(0));
    }

}
