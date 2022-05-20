package top.dddpeter.ishare.job.admin.core.route.strategy;

import top.dddpeter.ishare.job.admin.core.route.ExecutorRouter;
import top.dddpeter.ishare.job.admin.core.scheduler.JobScheduler;
import top.dddpeter.ishare.job.admin.core.util.I18nUtil;
import top.dddpeter.ishare.job.biz.ExecutorBiz;
import top.dddpeter.ishare.job.biz.model.ReturnT;
import top.dddpeter.ishare.job.biz.model.TriggerParam;

import java.util.List;

/**
 *  @Author hqins 2019-12-10
 */
public class ExecutorRouteBusyover extends ExecutorRouter {

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        StringBuffer idleBeatResultSB = new StringBuffer();
        for (String address : addressList) {
            // beat
            ReturnT<String> idleBeatResult = null;
            try {
                ExecutorBiz executorBiz = JobScheduler.getExecutorBiz(address);
                idleBeatResult = executorBiz.idleBeat(triggerParam.getJobId());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                idleBeatResult = new ReturnT<String>(ReturnT.FAIL_CODE, ""+e );
            }
            idleBeatResultSB.append( (idleBeatResultSB.length()>0)?"<br><br>":"")
                    .append(I18nUtil.getString("jobconf_idleBeat") + "：")
                    .append("<br>address：").append(address)
                    .append("<br>code：").append(idleBeatResult.getCode())
                    .append("<br>msg：").append(idleBeatResult.getMsg());

            // beat success
            if (idleBeatResult.getCode() == ReturnT.SUCCESS_CODE) {
                idleBeatResult.setMsg(idleBeatResultSB.toString());
                idleBeatResult.setContent(address);
                return idleBeatResult;
            }
        }

        return new ReturnT<String>(ReturnT.FAIL_CODE, idleBeatResultSB.toString());
    }

}
