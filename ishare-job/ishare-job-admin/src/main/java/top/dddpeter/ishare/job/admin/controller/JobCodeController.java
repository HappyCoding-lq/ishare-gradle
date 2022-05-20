package top.dddpeter.ishare.job.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.dddpeter.ishare.job.admin.core.model.JobInfo;
import top.dddpeter.ishare.job.admin.core.model.JobLogGlue;
import top.dddpeter.ishare.job.admin.core.util.I18nUtil;
import top.dddpeter.ishare.job.admin.dao.JobInfoDao;
import top.dddpeter.ishare.job.admin.dao.JobLogGlueDao;
import top.dddpeter.ishare.job.biz.model.ReturnT;
import top.dddpeter.ishare.job.glue.GlueTypeEnum;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * job code controller
 * @author hqins 2019-12-10
 */
@Controller
@RequestMapping("/jobcode")
public class JobCodeController {
	
	@Resource
	private JobInfoDao jobInfoDao;
	@Resource
	private JobLogGlueDao jobLogGlueDao;

	@RequestMapping
	public String index(HttpServletRequest request, Model model, int jobId) {
		JobInfo jobInfo = jobInfoDao.loadById(jobId);
		List<JobLogGlue> jobLogGlues = jobLogGlueDao.findByJobId(jobId);

		if (jobInfo == null) {
			throw new RuntimeException(I18nUtil.getString("jobinfo_glue_jobid_unvalid"));
		}
		if (GlueTypeEnum.BEAN == GlueTypeEnum.match(jobInfo.getGlueType())) {
			throw new RuntimeException(I18nUtil.getString("jobinfo_glue_gluetype_unvalid"));
		}

		// valid permission
		JobInfoController.validPermission(request, jobInfo.getJobGroup());

		// Glue类型-字典
		model.addAttribute("GlueTypeEnum", GlueTypeEnum.values());

		model.addAttribute("jobInfo", jobInfo);
		model.addAttribute("jobLogGlues", jobLogGlues);
		return "jobcode/jobcode.index";
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public ReturnT<String> save(Model model, int id, String glueSource, String glueRemark) {
		// valid
		if (glueRemark==null) {
			return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_glue_remark")) );
		}
		if (glueRemark.length()<4 || glueRemark.length()>100) {
			return new ReturnT<String>(500, I18nUtil.getString("jobinfo_glue_remark_limit"));
		}
		JobInfo exists_jobInfo = jobInfoDao.loadById(id);
		if (exists_jobInfo == null) {
			return new ReturnT<String>(500, I18nUtil.getString("jobinfo_glue_jobid_unvalid"));
		}
		
		// update new code
		exists_jobInfo.setGlueSource(glueSource);
		exists_jobInfo.setGlueRemark(glueRemark);
		exists_jobInfo.setGlueUpdatetime(new Date());

		exists_jobInfo.setUpdateTime(new Date());
		jobInfoDao.update(exists_jobInfo);

		// log old code
		JobLogGlue jobLogGlue = new JobLogGlue();
		jobLogGlue.setJobId(exists_jobInfo.getId());
		jobLogGlue.setGlueType(exists_jobInfo.getGlueType());
		jobLogGlue.setGlueSource(glueSource);
		jobLogGlue.setGlueRemark(glueRemark);

		jobLogGlue.setAddTime(new Date());
		jobLogGlue.setUpdateTime(new Date());
		jobLogGlueDao.save(jobLogGlue);

		// remove code backup more than 30
		jobLogGlueDao.removeOld(exists_jobInfo.getId(), 30);

		return ReturnT.SUCCESS;
	}
	
}
