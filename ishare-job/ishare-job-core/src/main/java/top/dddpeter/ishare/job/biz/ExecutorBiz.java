package top.dddpeter.ishare.job.biz;

import top.dddpeter.ishare.job.biz.model.LogResult;
import top.dddpeter.ishare.job.biz.model.ReturnT;
import top.dddpeter.ishare.job.biz.model.TriggerParam;

/**
 *  @Author hqins 2019-12-10
 */
public interface ExecutorBiz {

    /**
     * beat
     * @return
     */
    public ReturnT<String> beat();

    /**
     * idle beat
     *
     * @param jobId
     * @return
     */
    public ReturnT<String> idleBeat(int jobId);

    /**
     * kill
     * @param jobId
     * @return
     */
    public ReturnT<String> kill(int jobId);

    /**
     * log
     * @param logDateTim
     * @param logId
     * @param fromLineNum
     * @return
     */
    public ReturnT<LogResult> log(long logDateTim, long logId, int fromLineNum);

    /**
     * run
     * @param triggerParam
     * @return
     */
    public ReturnT<String> run(TriggerParam triggerParam);

}
