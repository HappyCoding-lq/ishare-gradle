package top.dddpeter.ishare.job.admin.service;


import top.dddpeter.ishare.job.admin.core.model.JobInfo;
import top.dddpeter.ishare.job.biz.model.ReturnT;

import java.util.Date;
import java.util.Map;

/**
 * core job action forishare-job
 * 
 * @author hqins 2019-12-10
 */
public interface JobService {

	/**
	 * page list
	 *
	 * @param start
	 * @param length
	 * @param jobGroup
	 * @param jobDesc
	 * @param executorHandler
	 * @param author
	 * @return
	 */
	public Map<String, Object> pageList(int start, int length, int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author);

	/**
	 * add job
	 *
	 * @param jobInfo
	 * @return
	 */
	public ReturnT<String> add(JobInfo jobInfo);

	/**
	 * update job
	 *
	 * @param jobInfo
	 * @return
	 */
	public ReturnT<String> update(JobInfo jobInfo);

	/**
	 * remove job
	 * 	 *
	 * @param id
	 * @return
	 */
	public ReturnT<String> remove(int id);

	/**
	 * start job
	 *
	 * @param id
	 * @return
	 */
	public ReturnT<String> start(int id);

	/**
	 * stop job
	 *
	 * @param id
	 * @return
	 */
	public ReturnT<String> stop(int id);

	/**
	 * dashboard info
	 *
	 * @return
	 */
	public Map<String,Object> dashboardInfo();

	/**
	 * chart info
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public ReturnT<Map<String,Object>> chartInfo(Date startDate, Date endDate);

}
