package top.dddpeter.ishare.job.handler.impl;

import top.dddpeter.ishare.job.biz.model.ReturnT;
import top.dddpeter.ishare.job.handler.IJobHandler;
import top.dddpeter.ishare.job.log.JobLogger;

/**
 * glue job handler
 * @author hqins 2019-12-10
 */
public class GlueJobHandler extends IJobHandler {

	private long glueUpdatetime;
	private IJobHandler jobHandler;
	public GlueJobHandler(IJobHandler jobHandler, long glueUpdatetime) {
		this.jobHandler = jobHandler;
		this.glueUpdatetime = glueUpdatetime;
	}
	public long getGlueUpdatetime() {
		return glueUpdatetime;
	}

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		JobLogger.log("----------- glue.version:"+ glueUpdatetime +" -----------");
		return jobHandler.execute(param);
	}

}
