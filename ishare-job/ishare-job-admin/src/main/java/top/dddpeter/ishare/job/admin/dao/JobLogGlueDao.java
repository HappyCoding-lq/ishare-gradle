package top.dddpeter.ishare.job.admin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.dddpeter.ishare.job.admin.core.model.JobLogGlue;

import java.util.List;

/**
 * job log for glue
 * @author hqins 2019-12-10
 */
@Mapper
public interface JobLogGlueDao {
	
	public int save(JobLogGlue jobLogGlue);
	
	public List<JobLogGlue> findByJobId(@Param("jobId") int jobId);

	public int removeOld(@Param("jobId") int jobId, @Param("limit") int limit);

	public int deleteByJobId(@Param("jobId") int jobId);
	
}
