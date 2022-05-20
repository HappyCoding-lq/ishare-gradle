package top.dddpeter.ishare.job.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.dddpeter.ishare.job.biz.AdminBiz;
import top.dddpeter.ishare.job.biz.model.RegistryParam;
import top.dddpeter.ishare.job.biz.model.ReturnT;
import top.dddpeter.ishare.job.enums.RegistryConfig;
import top.dddpeter.ishare.job.executor.JobExecutor;
import top.dddpeter.ishare.job.executor.impl.JobSpringExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *  @Author hqins 2019-12-10
 */
public class ExecutorRegistryThread {
    private static Logger logger = LoggerFactory.getLogger(ExecutorRegistryThread.class);

    private static ExecutorRegistryThread instance = new ExecutorRegistryThread();
    public static ExecutorRegistryThread getInstance(){
        return instance;
    }

    private Thread registryThread;
    private volatile boolean toStop = false;
    public void start(final String appName, final String address){

        // valid
        if (appName==null || appName.trim().length()==0) {
            logger.warn(">>>>>>>>>>>ishare-job, executor registry config fail, appName is null.");
            return;
        }
        if (JobExecutor.getAdminBizList() == null) {
            logger.warn(">>>>>>>>>>>ishare-job, executor registry config fail, adminAddresses is null.");
            return;
        }

        registryThread = new Thread(new Runnable() {
            @Override
            public void run() {

                // registry
                while (!toStop) {
                    try {
                        RegistryParam registryParam = new RegistryParam(RegistryConfig.RegistType.EXECUTOR.name(), appName, address);
                        List<AdminBiz> adminBizList = new ArrayList<>();
                        if(JobExecutor.srvIdenable){
                            adminBizList = JobSpringExecutor.getAdminBizList();
                        }
                        else{
                            adminBizList = JobExecutor.getAdminBizList();
                        }
                        for (AdminBiz adminBiz: adminBizList) {
                            try {
                                ReturnT<String> registryResult = adminBiz.registry(registryParam);
                                if (registryResult!=null && ReturnT.SUCCESS_CODE == registryResult.getCode()) {
                                    registryResult = ReturnT.SUCCESS;
                                    logger.debug(">>>>>>>>>>>ishare-job registry success, registryParam:{}, registryResult:{}", new Object[]{registryParam, registryResult});
                                    break;
                                } else {
                                    logger.info(">>>>>>>>>>>ishare-job registry fail, registryParam:{}, registryResult:{}", new Object[]{registryParam, registryResult});
                                }
                            } catch (Exception e) {
                                logger.info(">>>>>>>>>>>ishare-job registry error, registryParam:{}", registryParam, e);
                            }

                        }
                    } catch (Exception e) {
                        if (!toStop) {
                            logger.error(e.getMessage(), e);
                        }

                    }

                    try {
                        if (!toStop) {
                            TimeUnit.SECONDS.sleep(RegistryConfig.BEAT_TIMEOUT);
                        }
                    } catch (InterruptedException e) {
                        if (!toStop) {
                            logger.warn(">>>>>>>>>>>ishare-job, executor registry thread interrupted, error msg:{}", e.getMessage());
                        }
                    }
                }

                // registry remove
                try {
                    RegistryParam registryParam = new RegistryParam(RegistryConfig.RegistType.EXECUTOR.name(), appName, address);
                    for (AdminBiz adminBiz: JobExecutor.getAdminBizList()) {
                        try {
                            ReturnT<String> registryResult = adminBiz.registryRemove(registryParam);
                            if (registryResult!=null && ReturnT.SUCCESS_CODE == registryResult.getCode()) {
                                registryResult = ReturnT.SUCCESS;
                                logger.info(">>>>>>>>>>>ishare-job registry-remove success, registryParam:{}, registryResult:{}", new Object[]{registryParam, registryResult});
                                break;
                            } else {
                                logger.info(">>>>>>>>>>>ishare-job registry-remove fail, registryParam:{}, registryResult:{}", new Object[]{registryParam, registryResult});
                            }
                        } catch (Exception e) {
                            if (!toStop) {
                                logger.info(">>>>>>>>>>>ishare-job registry-remove error, registryParam:{}", registryParam, e);
                            }

                        }

                    }
                } catch (Exception e) {
                    if (!toStop) {
                        logger.error(e.getMessage(), e);
                    }
                }
                logger.info(">>>>>>>>>>>ishare-job, executor registry thread destory.");

            }
        });
        registryThread.setDaemon(true);
        registryThread.setName("job, executor ExecutorRegistryThread");
        registryThread.start();
    }

    public void toStop() {
        toStop = true;
        // interrupt and wait
        registryThread.interrupt();
        try {
            registryThread.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
