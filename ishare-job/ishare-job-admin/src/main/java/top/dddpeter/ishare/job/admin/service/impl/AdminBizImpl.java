package top.dddpeter.ishare.job.admin.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.dddpeter.ishare.job.admin.core.model.JobInfo;
import top.dddpeter.ishare.job.admin.core.model.JobLog;
import top.dddpeter.ishare.job.admin.core.thread.JobTriggerPoolHelper;
import top.dddpeter.ishare.job.admin.core.trigger.TriggerTypeEnum;
import top.dddpeter.ishare.job.admin.core.util.I18nUtil;
import top.dddpeter.ishare.job.admin.dao.JobGroupDao;
import top.dddpeter.ishare.job.admin.dao.JobInfoDao;
import top.dddpeter.ishare.job.admin.dao.JobLogDao;
import top.dddpeter.ishare.job.admin.dao.JobRegistryDao;
import top.dddpeter.ishare.job.biz.AdminBiz;
import top.dddpeter.ishare.job.biz.model.HandleCallbackParam;
import top.dddpeter.ishare.job.biz.model.RegistryParam;
import top.dddpeter.ishare.job.biz.model.ReturnT;
import top.dddpeter.ishare.job.handler.IJobHandler;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

/**
 * @author hqins 2019-12-10
 */
@Service
public class AdminBizImpl implements AdminBiz {
    private static Logger logger = LoggerFactory.getLogger(AdminBizImpl.class);

    @Resource
    public JobLogDao jobLogDao;
    @Resource
    private JobInfoDao jobInfoDao;
    @Resource
    private JobRegistryDao jobRegistryDao;
    @Resource
    private JobGroupDao jobGroupDao;


    @Override
    public ReturnT<String> callback(List<HandleCallbackParam> callbackParamList) {
        for (HandleCallbackParam handleCallbackParam: callbackParamList) {
            ReturnT<String> callbackResult = callback(handleCallbackParam);
            logger.debug(">>>>>>>>> JobApiController.callback {}, handleCallbackParam={}, callbackResult={}",
                    (callbackResult.getCode()==IJobHandler.SUCCESS.getCode()?"success":"fail"), handleCallbackParam, callbackResult);
        }

        return ReturnT.SUCCESS;
    }

    private ReturnT<String> callback(HandleCallbackParam handleCallbackParam) {
        // valid log item
        JobLog log = jobLogDao.load(handleCallbackParam.getLogId());
        if (log == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "log item not found.");
        }
        if (log.getHandleCode() > 0) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "log repeate callback.");     // avoid repeat callback, trigger child job etc
        }

        // trigger success, to trigger child job
        String callbackMsg = null;
        if (IJobHandler.SUCCESS.getCode() == handleCallbackParam.getExecuteResult().getCode()) {
            JobInfo jobInfo = jobInfoDao.loadById(log.getJobId());
            if (jobInfo !=null && jobInfo.getChildJobId()!=null && jobInfo.getChildJobId().trim().length()>0) {
                callbackMsg = "<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>"+ I18nUtil.getString("jobconf_trigger_child_run") +"<<<<<<<<<<< </span><br>";

                String[] childJobIds = jobInfo.getChildJobId().split(",");
                for (int i = 0; i < childJobIds.length; i++) {
                    int childJobId = (childJobIds[i]!=null && childJobIds[i].trim().length()>0 && isNumeric(childJobIds[i]))?Integer.valueOf(childJobIds[i]):-1;
                    if (childJobId > 0) {

                        JobTriggerPoolHelper.trigger(childJobId, TriggerTypeEnum.PARENT, -1, null, null);
                        ReturnT<String> triggerChildResult = ReturnT.SUCCESS;

                        // add msg
                        callbackMsg += MessageFormat.format(I18nUtil.getString("jobconf_callback_child_msg1"),
                                (i+1),
                                childJobIds.length,
                                childJobIds[i],
                                (triggerChildResult.getCode()==ReturnT.SUCCESS_CODE?I18nUtil.getString("system_success"):I18nUtil.getString("system_fail")),
                                triggerChildResult.getMsg());
                    } else {
                        callbackMsg += MessageFormat.format(I18nUtil.getString("jobconf_callback_child_msg2"),
                                (i+1),
                                childJobIds.length,
                                childJobIds[i]);
                    }
                }

            }
        }

        // handle msg
        StringBuffer handleMsg = new StringBuffer();
        if (log.getHandleMsg()!=null) {
            handleMsg.append(log.getHandleMsg()).append("<br>");
        }
        if (handleCallbackParam.getExecuteResult().getMsg() != null) {
            handleMsg.append(handleCallbackParam.getExecuteResult().getMsg());
        }
        if (callbackMsg != null) {
            handleMsg.append(callbackMsg);
        }

        // success, save log
        log.setHandleTime(new Date());
        log.setHandleCode(handleCallbackParam.getExecuteResult().getCode());
        log.setHandleMsg(handleMsg.toString());
        jobLogDao.updateHandleInfo(log);

        return ReturnT.SUCCESS;
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
    public ReturnT<String> registry(RegistryParam registryParam) {

        // valid
        if (!StringUtils.hasText(registryParam.getRegistryGroup())
                || !StringUtils.hasText(registryParam.getRegistryKey())
                || !StringUtils.hasText(registryParam.getRegistryValue())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "Illegal Argument.");
        }

        int ret = jobRegistryDao.registryUpdate(registryParam.getRegistryGroup(), registryParam.getRegistryKey(), registryParam.getRegistryValue(), new Date());
        if (ret < 1) {
            jobRegistryDao.registrySave(registryParam.getRegistryGroup(), registryParam.getRegistryKey(), registryParam.getRegistryValue(), new Date());

            // fresh
            freshGroupRegistryInfo(registryParam);
        }
        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<String> registryRemove(RegistryParam registryParam) {

        // valid
        if (!StringUtils.hasText(registryParam.getRegistryGroup())
                || !StringUtils.hasText(registryParam.getRegistryKey())
                || !StringUtils.hasText(registryParam.getRegistryValue())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "Illegal Argument.");
        }

        int ret = jobRegistryDao.registryDelete(registryParam.getRegistryGroup(), registryParam.getRegistryKey(), registryParam.getRegistryValue());
        if (ret > 0) {

            // fresh
            freshGroupRegistryInfo(registryParam);
        }
        return ReturnT.SUCCESS;
    }

    private void freshGroupRegistryInfo(RegistryParam registryParam){
        // Under consideration, prevent affecting core tables
    }

}
