package top.dddpeter.ishare.job.admin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.dddpeter.ishare.job.admin.core.model.JobLog;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * job log
 * @author hqins 2019-12-10
 */
@Mapper
public interface JobLogDao {

	// exist jobId not use jobGroup, not exist use jobGroup
	public List<JobLog> pageList(@Param("offset") int offset,
                                 @Param("pagesize") int pagesize,
                                 @Param("jobGroup") int jobGroup,
                                 @Param("jobId") int jobId,
                                 @Param("triggerTimeStart") Date triggerTimeStart,
                                 @Param("triggerTimeEnd") Date triggerTimeEnd,
                                 @Param("logStatus") int logStatus);
	public int pageListCount(@Param("offset") int offset,
							 @Param("pagesize") int pagesize,
							 @Param("jobGroup") int jobGroup,
							 @Param("jobId") int jobId,
							 @Param("triggerTimeStart") Date triggerTimeStart,
							 @Param("triggerTimeEnd") Date triggerTimeEnd,
							 @Param("logStatus") int logStatus);
	
	public JobLog load(@Param("id") long id);

	public long save(JobLog jobLog);

	public int updateTriggerInfo(JobLog jobLog);

	public int updateHandleInfo(JobLog jobLog);
	
	public int delete(@Param("jobId") int jobId);

	public Map<String, Object> findLogReport(@Param("from") Date from,
											 @Param("to") Date to);

	public List<Long> findClearLogIds(@Param("jobGroup") int jobGroup,
									  @Param("jobId") int jobId,
									  @Param("clearBeforeTime") Date clearBeforeTime,
									  @Param("clearBeforeNum") int clearBeforeNum,
									  @Param("pagesize") int pagesize);
	public int clearLog(@Param("logIds") List<Long> logIds);

	public List<Long> findFailJobLogIds(@Param("pagesize") int pagesize);

	public int updateAlarmStatus(@Param("logId") long logId,
								 @Param("oldAlarmStatus") int oldAlarmStatus,
								 @Param("newAlarmStatus") int newAlarmStatus);

}
