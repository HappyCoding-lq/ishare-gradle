package top.dddpeter.ishare.job.admin.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import top.dddpeter.ishare.job.admin.core.cron.CronExpression;
import top.dddpeter.ishare.job.admin.core.model.JobGroup;
import top.dddpeter.ishare.job.admin.core.model.JobInfo;
import top.dddpeter.ishare.job.admin.core.model.JobLogReport;
import top.dddpeter.ishare.job.admin.core.route.ExecutorRouteStrategyEnum;
import top.dddpeter.ishare.job.admin.core.thread.JobScheduleHelper;
import top.dddpeter.ishare.job.admin.core.util.I18nUtil;
import top.dddpeter.ishare.job.admin.dao.*;
import top.dddpeter.ishare.job.admin.service.JobService;
import top.dddpeter.ishare.job.biz.model.ReturnT;
import top.dddpeter.ishare.job.enums.ExecutorBlockStrategyEnum;
import top.dddpeter.ishare.job.glue.GlueTypeEnum;
import top.dddpeter.ishare.job.util.DateUtil;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;

/**
 * core job action forishare-job
 * @author hqins 2019-12-10
 */
@Service
public class JobServiceImpl implements JobService {
	private static Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

	@Resource
	private JobGroupDao jobGroupDao;
	@Resource
	private JobInfoDao jobInfoDao;
	@Resource
	public JobLogDao jobLogDao;
	@Resource
	private JobLogGlueDao jobLogGlueDao;
	@Resource
	private JobLogReportDao jobLogReportDao;
	
	@Override
	public Map<String, Object> pageList(int start, int length, int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author) {

		// page list
		List<JobInfo> list = jobInfoDao.pageList(start, length, jobGroup, triggerStatus, jobDesc, executorHandler, author);
		int list_count = jobInfoDao.pageListCount(start, length, jobGroup, triggerStatus, jobDesc, executorHandler, author);
		
		// package result
		Map<String, Object> maps = new HashMap<String, Object>();
	    maps.put("recordsTotal", list_count);		// 总记录数
	    maps.put("recordsFiltered", list_count);	// 过滤后的总记录数
	    maps.put("data", list);  					// 分页列表
		return maps;
	}

	@Override
	public ReturnT<String> add(JobInfo jobInfo) {
		// valid
		JobGroup group = jobGroupDao.load(jobInfo.getJobGroup());
		if (group == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("system_please_choose")+I18nUtil.getString("jobinfo_field_jobgroup")) );
		}
		if (!CronExpression.isValidExpression(jobInfo.getJobCron())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("jobinfo_field_cron_unvalid") );
		}
		if (jobInfo.getJobDesc()==null || jobInfo.getJobDesc().trim().length()==0) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("system_please_input")+I18nUtil.getString("jobinfo_field_jobdesc")) );
		}
		if (jobInfo.getAuthor()==null || jobInfo.getAuthor().trim().length()==0) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("system_please_input")+I18nUtil.getString("jobinfo_field_author")) );
		}
		if (ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null) == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_executorRouteStrategy")+I18nUtil.getString("system_unvalid")) );
		}
		if (ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), null) == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_executorBlockStrategy")+I18nUtil.getString("system_unvalid")) );
		}
		if (GlueTypeEnum.match(jobInfo.getGlueType()) == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_gluetype")+I18nUtil.getString("system_unvalid")) );
		}
		if (GlueTypeEnum.BEAN==GlueTypeEnum.match(jobInfo.getGlueType()) && (jobInfo.getExecutorHandler()==null || jobInfo.getExecutorHandler().trim().length()==0) ) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("system_please_input")+"JobHandler") );
		}

		// fix "\r" in shell
		if (GlueTypeEnum.GLUE_SHELL==GlueTypeEnum.match(jobInfo.getGlueType()) && jobInfo.getGlueSource()!=null) {
			jobInfo.setGlueSource(jobInfo.getGlueSource().replaceAll("\r", ""));
		}

		// ChildJobId valid
        if (jobInfo.getChildJobId()!=null && jobInfo.getChildJobId().trim().length()>0) {
			String[] childJobIds = jobInfo.getChildJobId().split(",");
			for (String childJobIdItem: childJobIds) {
				if (childJobIdItem!=null && childJobIdItem.trim().length()>0 && isNumeric(childJobIdItem)) {
					JobInfo childJobInfo = jobInfoDao.loadById(Integer.parseInt(childJobIdItem));
					if (childJobInfo==null) {
						return new ReturnT<String>(ReturnT.FAIL_CODE,
								MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId")+"({0})"+I18nUtil.getString("system_not_found")), childJobIdItem));
					}
				} else {
					return new ReturnT<String>(ReturnT.FAIL_CODE,
							MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId")+"({0})"+I18nUtil.getString("system_unvalid")), childJobIdItem));
				}
			}

			// join , avoid "xxx,,"
			String temp = "";
			for (String item:childJobIds) {
				temp += item + ",";
			}
			temp = temp.substring(0, temp.length()-1);

			jobInfo.setChildJobId(temp);
		}

		// add in db
		jobInfo.setAddTime(new Date());
		jobInfo.setUpdateTime(new Date());
		jobInfo.setGlueUpdatetime(new Date());
		jobInfoDao.save(jobInfo);
		if (jobInfo.getId() < 1) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_add")+I18nUtil.getString("system_fail")) );
		}

		return new ReturnT<String>(String.valueOf(jobInfo.getId()));
	}

	private boolean isNumeric(String str){
		try {
			int result = Integer.valueOf(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	public ReturnT<String> update(JobInfo jobInfo) {

		// valid
		if (!CronExpression.isValidExpression(jobInfo.getJobCron())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("jobinfo_field_cron_unvalid") );
		}
		if (jobInfo.getJobDesc()==null || jobInfo.getJobDesc().trim().length()==0) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("system_please_input")+I18nUtil.getString("jobinfo_field_jobdesc")) );
		}
		if (jobInfo.getAuthor()==null || jobInfo.getAuthor().trim().length()==0) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("system_please_input")+I18nUtil.getString("jobinfo_field_author")) );
		}
		if (ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null) == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_executorRouteStrategy")+I18nUtil.getString("system_unvalid")) );
		}
		if (ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), null) == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_executorBlockStrategy")+I18nUtil.getString("system_unvalid")) );
		}

		// ChildJobId valid
        if (jobInfo.getChildJobId()!=null && jobInfo.getChildJobId().trim().length()>0) {
			String[] childJobIds = jobInfo.getChildJobId().split(",");
			for (String childJobIdItem: childJobIds) {
				if (childJobIdItem!=null && childJobIdItem.trim().length()>0 && isNumeric(childJobIdItem)) {
					JobInfo childJobInfo = jobInfoDao.loadById(Integer.parseInt(childJobIdItem));
					if (childJobInfo==null) {
						return new ReturnT<String>(ReturnT.FAIL_CODE,
								MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId")+"({0})"+I18nUtil.getString("system_not_found")), childJobIdItem));
					}
				} else {
					return new ReturnT<String>(ReturnT.FAIL_CODE,
							MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId")+"({0})"+I18nUtil.getString("system_unvalid")), childJobIdItem));
				}
			}

			// join , avoid "xxx,,"
			String temp = "";
			for (String item:childJobIds) {
				temp += item + ",";
			}
			temp = temp.substring(0, temp.length()-1);

			jobInfo.setChildJobId(temp);
		}

		// group valid
		JobGroup jobGroup = jobGroupDao.load(jobInfo.getJobGroup());
		if (jobGroup == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_jobgroup")+I18nUtil.getString("system_unvalid")) );
		}

		// stage job info
		JobInfo exists_jobInfo = jobInfoDao.loadById(jobInfo.getId());
		if (exists_jobInfo == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_id")+I18nUtil.getString("system_not_found")) );
		}

		// next trigger time (5s后生效，避开预读周期)
		long nextTriggerTime = exists_jobInfo.getTriggerNextTime();
		if (exists_jobInfo.getTriggerStatus() == 1 && !jobInfo.getJobCron().equals(exists_jobInfo.getJobCron()) ) {
			try {
				Date nextValidTime = new CronExpression(jobInfo.getJobCron()).getNextValidTimeAfter(new Date(System.currentTimeMillis() + JobScheduleHelper.PRE_READ_MS));
				if (nextValidTime == null) {
					return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("jobinfo_field_cron_never_fire"));
				}
				nextTriggerTime = nextValidTime.getTime();
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
				return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("jobinfo_field_cron_unvalid")+" | "+ e.getMessage());
			}
		}

		exists_jobInfo.setJobGroup(jobInfo.getJobGroup());
		exists_jobInfo.setJobCron(jobInfo.getJobCron());
		exists_jobInfo.setJobDesc(jobInfo.getJobDesc());
		exists_jobInfo.setAuthor(jobInfo.getAuthor());
		exists_jobInfo.setAlarmEmail(jobInfo.getAlarmEmail());
		exists_jobInfo.setExecutorRouteStrategy(jobInfo.getExecutorRouteStrategy());
		exists_jobInfo.setExecutorHandler(jobInfo.getExecutorHandler());
		exists_jobInfo.setExecutorParam(jobInfo.getExecutorParam());
		exists_jobInfo.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
		exists_jobInfo.setExecutorTimeout(jobInfo.getExecutorTimeout());
		exists_jobInfo.setExecutorFailRetryCount(jobInfo.getExecutorFailRetryCount());
		exists_jobInfo.setChildJobId(jobInfo.getChildJobId());
		exists_jobInfo.setTriggerNextTime(nextTriggerTime);

		exists_jobInfo.setUpdateTime(new Date());
        jobInfoDao.update(exists_jobInfo);


		return ReturnT.SUCCESS;
	}

	@Override
	public ReturnT<String> remove(int id) {
		JobInfo jobInfo = jobInfoDao.loadById(id);
		if (jobInfo == null) {
			return ReturnT.SUCCESS;
		}

		jobInfoDao.delete(id);
		jobLogDao.delete(id);
		jobLogGlueDao.deleteByJobId(id);
		return ReturnT.SUCCESS;
	}

	@Override
	public ReturnT<String> start(int id) {
		JobInfo jobInfo = jobInfoDao.loadById(id);

		// next trigger time (5s后生效，避开预读周期)
		long nextTriggerTime = 0;
		try {
			Date nextValidTime = new CronExpression(jobInfo.getJobCron()).getNextValidTimeAfter(new Date(System.currentTimeMillis() + JobScheduleHelper.PRE_READ_MS));
			if (nextValidTime == null) {
				return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("jobinfo_field_cron_never_fire"));
			}
			nextTriggerTime = nextValidTime.getTime();
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
			return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("jobinfo_field_cron_unvalid")+" | "+ e.getMessage());
		}

		jobInfo.setTriggerStatus(1);
		jobInfo.setTriggerLastTime(0);
		jobInfo.setTriggerNextTime(nextTriggerTime);

		jobInfo.setUpdateTime(new Date());
		jobInfoDao.update(jobInfo);
		return ReturnT.SUCCESS;
	}

	@Override
	public ReturnT<String> stop(int id) {
        JobInfo jobInfo = jobInfoDao.loadById(id);

		jobInfo.setTriggerStatus(0);
		jobInfo.setTriggerLastTime(0);
		jobInfo.setTriggerNextTime(0);

		jobInfo.setUpdateTime(new Date());
		jobInfoDao.update(jobInfo);
		return ReturnT.SUCCESS;
	}

	@Override
	public Map<String, Object> dashboardInfo() {

		int jobInfoCount = jobInfoDao.findAllCount();
		int jobLogCount = 0;
		int jobLogSuccessCount = 0;
		JobLogReport jobLogReport = jobLogReportDao.queryLogReportTotal();
		if (jobLogReport != null) {
			jobLogCount = jobLogReport.getRunningCount() + jobLogReport.getSucCount() + jobLogReport.getFailCount();
			jobLogSuccessCount = jobLogReport.getSucCount();
		}

		// executor count
		Set<String> executorAddressSet = new HashSet<String>();
		List<JobGroup> groupList = jobGroupDao.findAll();

		if (groupList!=null && !groupList.isEmpty()) {
			for (JobGroup group: groupList) {
				if (group.getRegistryList()!=null && !group.getRegistryList().isEmpty()) {
					executorAddressSet.addAll(group.getRegistryList());
				}
			}
		}

		int executorCount = executorAddressSet.size();

		Map<String, Object> dashboardMap = new HashMap<String, Object>();
		dashboardMap.put("jobInfoCount", jobInfoCount);
		dashboardMap.put("jobLogCount", jobLogCount);
		dashboardMap.put("jobLogSuccessCount", jobLogSuccessCount);
		dashboardMap.put("executorCount", executorCount);
		return dashboardMap;
	}

	@Override
	public ReturnT<Map<String, Object>> chartInfo(Date startDate, Date endDate) {

		// process
		List<String> triggerDayList = new ArrayList<String>();
		List<Integer> triggerDayCountRunningList = new ArrayList<Integer>();
		List<Integer> triggerDayCountSucList = new ArrayList<Integer>();
		List<Integer> triggerDayCountFailList = new ArrayList<Integer>();
		int triggerCountRunningTotal = 0;
		int triggerCountSucTotal = 0;
		int triggerCountFailTotal = 0;

		List<JobLogReport> logReportList = jobLogReportDao.queryLogReport(startDate, endDate);

		if (logReportList!=null && logReportList.size()>0) {
			for (JobLogReport item: logReportList) {
				String day = DateUtil.formatDate(item.getTriggerDay());
				int triggerDayCountRunning = item.getRunningCount();
				int triggerDayCountSuc = item.getSucCount();
				int triggerDayCountFail = item.getFailCount();

				triggerDayList.add(day);
				triggerDayCountRunningList.add(triggerDayCountRunning);
				triggerDayCountSucList.add(triggerDayCountSuc);
				triggerDayCountFailList.add(triggerDayCountFail);

				triggerCountRunningTotal += triggerDayCountRunning;
				triggerCountSucTotal += triggerDayCountSuc;
				triggerCountFailTotal += triggerDayCountFail;
			}
		} else {
			for (int i = -6; i <= 0; i++) {
				triggerDayList.add(DateUtil.formatDate(DateUtil.addDays(new Date(), i)));
				triggerDayCountRunningList.add(0);
				triggerDayCountSucList.add(0);
				triggerDayCountFailList.add(0);
			}
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("triggerDayList", triggerDayList);
		result.put("triggerDayCountRunningList", triggerDayCountRunningList);
		result.put("triggerDayCountSucList", triggerDayCountSucList);
		result.put("triggerDayCountFailList", triggerDayCountFailList);

		result.put("triggerCountRunningTotal", triggerCountRunningTotal);
		result.put("triggerCountSucTotal", triggerCountSucTotal);
		result.put("triggerCountFailTotal", triggerCountFailTotal);

		return new ReturnT<Map<String, Object>>(result);
	}

}
