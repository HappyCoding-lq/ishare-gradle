package top.dddpeter.ishare.job.admin.core.conf;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.dddpeter.ishare.job.admin.core.scheduler.JobScheduler;
import top.dddpeter.ishare.job.admin.dao.*;
import top.dddpeter.ishare.system.feign.message.RemoteMessageService;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *ishare-job config
 *
 * @author hqins 2019-12-10
 */

@Component
public class JobAdminConfig implements InitializingBean, DisposableBean {

    private static JobAdminConfig adminConfig = null;
    public static JobAdminConfig getAdminConfig() {
        return adminConfig;
    }


    // ---------------------- XxlJobScheduler ----------------------

    private JobScheduler jobScheduler;

    @Override
    public void afterPropertiesSet() throws Exception {
        adminConfig = this;

        jobScheduler = new JobScheduler();
        jobScheduler.init();
    }

    @Override
    public void destroy() throws Exception {
        jobScheduler.destroy();
    }


    // ---------------------- JobScheduler ----------------------

    // conf
    @Value("${ishare.job.i18n}")
    private String i18n;

    @Value("${ishare.job.accessToken}")
    private String accessToken;

    @Value("${spring.mail.username}")
    private String emailUserName;

    @Value("${ishare.job.triggerpool.fast.max}")
    private int triggerPoolFastMax;

    @Value("${ishare.job.triggerpool.slow.max}")
    private int triggerPoolSlowMax;

    @Value("${ishare.job.logretentiondays}")
    private int logretentiondays;

    // dao, service

    @Resource
    private JobLogDao jobLogDao;
    @Resource
    private JobInfoDao jobInfoDao;
    @Resource
    private JobRegistryDao jobRegistryDao;
    @Resource
    private JobGroupDao jobGroupDao;
    @Resource
    private JobLogReportDao jobLogReportDao;
    @Resource
    private RemoteMessageService messageService;
    @Resource
    private DataSource dataSource;


    public String getI18n() {
        return i18n;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getEmailUserName() {
        return emailUserName;
    }

    public int getTriggerPoolFastMax() {
        if (triggerPoolFastMax < 200) {
            return 200;
        }
        return triggerPoolFastMax;
    }

    public int getTriggerPoolSlowMax() {
        if (triggerPoolSlowMax < 100) {
            return 100;
        }
        return triggerPoolSlowMax;
    }

    public int getLogretentiondays() {
        if (logretentiondays < 7) {
            return -1;  // Limit greater than or equal to 7, otherwise close
        }
        return logretentiondays;
    }

    public JobLogDao getJobLogDao() {
        return jobLogDao;
    }

    public JobInfoDao getJobInfoDao() {
        return jobInfoDao;
    }

    public JobRegistryDao getJobRegistryDao() {
        return jobRegistryDao;
    }

    public JobGroupDao getJobGroupDao() {
        return jobGroupDao;
    }

    public JobLogReportDao getJobLogReportDao() {
        return jobLogReportDao;
    }

    public RemoteMessageService getMessageService() {
        return messageService;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

}
