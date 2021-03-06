package top.dddpeter.ishare.job.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.dddpeter.ishare.job.biz.AdminBiz;
import top.dddpeter.ishare.job.biz.ExecutorBiz;
import top.dddpeter.ishare.job.biz.client.AdminBizClient;
import top.dddpeter.ishare.job.biz.impl.ExecutorBizImpl;
import top.dddpeter.ishare.job.handler.IJobHandler;
import top.dddpeter.ishare.job.log.JobFileAppender;
import top.dddpeter.ishare.job.rpc.registry.ServiceRegistry;
import top.dddpeter.ishare.job.rpc.remoting.net.impl.netty_http.server.NettyHttpServer;
import top.dddpeter.ishare.job.rpc.remoting.provider.RpcProviderFactory;
import top.dddpeter.ishare.job.rpc.serialize.Serializer;
import top.dddpeter.ishare.job.rpc.serialize.impl.HessianSerializer;
import top.dddpeter.ishare.job.rpc.util.IpUtil;
import top.dddpeter.ishare.job.rpc.util.NetUtil;
import top.dddpeter.ishare.job.thread.ExecutorRegistryThread;
import top.dddpeter.ishare.job.thread.JobLogFileCleanThread;
import top.dddpeter.ishare.job.thread.JobThread;
import top.dddpeter.ishare.job.thread.TriggerCallbackThread;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *  @Author hqins 2019-12-10
 */
public class JobExecutor {
    private static final Logger logger = LoggerFactory.getLogger(JobExecutor.class);

    // ---------------------- param ----------------------
    private String adminAddresses;
    private String appName;
    private String ip;
    private int port;
    private boolean serviceIdEnabled;
    public void setServiceIdEnabled(boolean serviceIdEnabled) {
        this.serviceIdEnabled = serviceIdEnabled;
    }


    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    private String serviceId;
    private String accessToken;
    private String logPath;
    private int logRetentionDays;

    public void setAdminAddresses(String adminAddresses) {
        this.adminAddresses = adminAddresses;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }
    public void setLogRetentionDays(int logRetentionDays) {
        this.logRetentionDays = logRetentionDays;
    }


    // ---------------------- start + stop ----------------------
    public void start() throws Exception {

        // init logpath
        JobFileAppender.initLogPath(logPath);

        // init invoker, admin-client
        initAdminBizList(adminAddresses, accessToken,serviceId,serviceIdEnabled);


        // init JobLogFileCleanThread
        JobLogFileCleanThread.getInstance().start(logRetentionDays);

        // init TriggerCallbackThread
        TriggerCallbackThread.getInstance().start();

        // init executor-server
        port = port>0?port: NetUtil.findAvailablePort(9999);
        ip = (ip!=null&&ip.trim().length()>0)?ip: IpUtil.getIp();
        initRpcProvider(ip, port, appName, accessToken);
    }
    public void destroy(){
        // destory executor-server
        stopRpcProvider();

        // destory jobThreadRepository
        if (jobThreadRepository.size() > 0) {
            for (Map.Entry<Integer, JobThread> item: jobThreadRepository.entrySet()) {
                removeJobThread(item.getKey(), "web container destroy and kill the job.");
            }
            jobThreadRepository.clear();
        }
        jobHandlerRepository.clear();


        // destory JobLogFileCleanThread
        JobLogFileCleanThread.getInstance().toStop();

        // destory TriggerCallbackThread
        TriggerCallbackThread.getInstance().toStop();

    }


    // ---------------------- admin-client (rpc invoker) ----------------------
    public static List<AdminBiz> adminBizList;
    public static Serializer serializer = new HessianSerializer();
    public static String srvId;
    public static boolean srvIdenable;
    public static String srvIdAddress;
    public static String acsToken;
    private void initAdminBizList(String adminAddresses, String accessToken,String serviceId,boolean serviceIdEnable) throws Exception {
        srvId = serviceId;
        srvIdenable = serviceIdEnable;
        acsToken = accessToken;
        srvIdAddress = adminAddresses;
        if (adminAddresses!=null && adminAddresses.trim().length()>0) {
            for (String address: adminAddresses.trim().split(",")) {
                if (address!=null && address.trim().length()>0) {

                    AdminBiz adminBiz = new AdminBizClient(address.trim(), accessToken);

                    if (adminBizList == null) {
                        adminBizList = new ArrayList<AdminBiz>();
                    }
                    adminBizList.add(adminBiz);
                }
            }
        }
    }
    public static List<AdminBiz> getAdminBizList(){
        return adminBizList;
    }
    public static Serializer getSerializer() {
        return serializer;
    }


    // ---------------------- executor-server (rpc provider) ----------------------
    private RpcProviderFactory rpcProviderFactory = null;

    private void initRpcProvider(String ip, int port, String appName, String accessToken) throws Exception {

        // init, provider factory
        String address = IpUtil.getIpPort(ip, port);
        Map<String, String> serviceRegistryParam = new HashMap<String, String>();
        serviceRegistryParam.put("appName", appName);
        serviceRegistryParam.put("address", address);

        rpcProviderFactory = new RpcProviderFactory();

        rpcProviderFactory.setServer(NettyHttpServer.class);
        rpcProviderFactory.setSerializer(HessianSerializer.class);
        rpcProviderFactory.setCorePoolSize(20);
        rpcProviderFactory.setMaxPoolSize(200);
        rpcProviderFactory.setIp(ip);
        rpcProviderFactory.setPort(port);
        rpcProviderFactory.setAccessToken(accessToken);
        rpcProviderFactory.setServiceRegistry(ExecutorServiceRegistry.class);
        rpcProviderFactory.setServiceRegistryParam(serviceRegistryParam);

        // add services
        rpcProviderFactory.addService(ExecutorBiz.class.getName(), null, new ExecutorBizImpl());

        // start
        rpcProviderFactory.start();

    }

    public static class ExecutorServiceRegistry extends ServiceRegistry {

        @Override
        public void start(Map<String, String> param) {
            // start registry
            ExecutorRegistryThread.getInstance().start(param.get("appName"), param.get("address"));
        }
        @Override
        public void stop() {
            // stop registry
            ExecutorRegistryThread.getInstance().toStop();
        }

        @Override
        public boolean registry(Set<String> keys, String value) {
            return false;
        }
        @Override
        public boolean remove(Set<String> keys, String value) {
            return false;
        }
        @Override
        public Map<String, TreeSet<String>> discovery(Set<String> keys) {
            return null;
        }
        @Override
        public TreeSet<String> discovery(String key) {
            return null;
        }

    }

    private void stopRpcProvider() {
        // stop provider factory
        try {
            rpcProviderFactory.stop();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }


    // ---------------------- job handler repository ----------------------
    private static ConcurrentMap<String, IJobHandler> jobHandlerRepository = new ConcurrentHashMap<String, IJobHandler>();
    public static IJobHandler registJobHandler(String name, IJobHandler jobHandler){
        logger.info(">>>>>>>>>>>ishare-job register jobhandler success, name:{}, jobHandler:{}", name, jobHandler);
        return jobHandlerRepository.put(name, jobHandler);
    }
    public static IJobHandler loadJobHandler(String name){
        return jobHandlerRepository.get(name);
    }


    // ---------------------- job thread repository ----------------------
    private static ConcurrentMap<Integer, JobThread> jobThreadRepository = new ConcurrentHashMap<Integer, JobThread>();
    public static JobThread registJobThread(int jobId, IJobHandler handler, String removeOldReason){
        JobThread newJobThread = new JobThread(jobId, handler);
        newJobThread.start();
        logger.info(">>>>>>>>>>>ishare-job regist JobThread success, jobId:{}, handler:{}", new Object[]{jobId, handler});

        JobThread oldJobThread = jobThreadRepository.put(jobId, newJobThread);	// putIfAbsent | oh my god, map's put method return the old value!!!
        if (oldJobThread != null) {
            oldJobThread.toStop(removeOldReason);
            oldJobThread.interrupt();
        }

        return newJobThread;
    }
    public static void removeJobThread(int jobId, String removeOldReason){
        JobThread oldJobThread = jobThreadRepository.remove(jobId);
        if (oldJobThread != null) {
            oldJobThread.toStop(removeOldReason);
            oldJobThread.interrupt();
        }
    }
    public static JobThread loadJobThread(int jobId){
        JobThread jobThread = jobThreadRepository.get(jobId);
        return jobThread;
    }

}
