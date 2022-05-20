package top.dddpeter.ishare.job.admin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.dddpeter.ishare.job.admin.core.model.JobLogReport;

import java.util.Date;
import java.util.List;

/**
 * job log
 * @author hqins 2019-12-10
 */
@Mapper
public interface JobLogReportDao {

	public int save(JobLogReport jobLogReport);

	public int update(JobLogReport jobLogReport);

	public List<JobLogReport> queryLogReport(@Param("triggerDayFrom") Date triggerDayFrom,
                                             @Param("triggerDayTo") Date triggerDayTo);

	public JobLogReport queryLogReportTotal();

}
