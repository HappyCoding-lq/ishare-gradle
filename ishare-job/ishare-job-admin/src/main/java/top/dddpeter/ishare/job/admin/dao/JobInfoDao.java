package top.dddpeter.ishare.job.admin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.dddpeter.ishare.job.admin.core.model.JobInfo;

import java.util.List;


/**
 * job info
 * @author hqins 2019-12-10
 */
@Mapper
public interface JobInfoDao {

	public List<JobInfo> pageList(@Param("offset") int offset,
                                  @Param("pagesize") int pagesize,
                                  @Param("jobGroup") int jobGroup,
                                  @Param("triggerStatus") int triggerStatus,
                                  @Param("jobDesc") String jobDesc,
                                  @Param("executorHandler") String executorHandler,
                                  @Param("author") String author);
	public int pageListCount(@Param("offset") int offset,
							 @Param("pagesize") int pagesize,
							 @Param("jobGroup") int jobGroup,
							 @Param("triggerStatus") int triggerStatus,
							 @Param("jobDesc") String jobDesc,
							 @Param("executorHandler") String executorHandler,
							 @Param("author") String author);
	
	public int save(JobInfo info);

	public JobInfo loadById(@Param("id") int id);
	
	public int update(JobInfo jobInfo);
	
	public int delete(@Param("id") long id);

	public List<JobInfo> getJobsByGroup(@Param("jobGroup") int jobGroup);

	public int findAllCount();

	public List<JobInfo> scheduleJobQuery(@Param("maxNextTime") long maxNextTime, @Param("pagesize") int pagesize );

	public int scheduleUpdate(JobInfo jobInfo);


}
