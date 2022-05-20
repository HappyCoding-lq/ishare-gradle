package top.dddpeter.ishare.job.executor.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.dddpeter.ishare.job.executor.impl.JobSpringExecutor;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *ishare-job config
 *
 * @author hqins 2019-12-10
 */
@Configuration
public class JobConfig {
    private Logger logger = LoggerFactory.getLogger(JobConfig.class);

    @Value("${ishare.job.admin.addresses:\"\"}")
    private String adminAddresses;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${ishare.job.executor.ip}")
    private String ip;

    @Value("${ishare.job.executor.port}")
    private int port;

    @Value("${ishare.job.accessToken}")
    private String accessToken;

    @Value("${ishare.job.executor.logpath}")
    private String logPath;
    @Value("${ishare.job.admin.enableServiceId:false}")
    private boolean enableServiceId;
    @Value("${ishare.job.admin.serviceId:}")
    private String serviceId;
    @Value("${ishare.job.executor.logretentiondays}")
    private int logRetentionDays;
    @Resource
    private DiscoveryClient discoveryClient;

    @Bean
    public JobSpringExecutor jobExecutor() {
        logger.info(">>>>>>>>>>> job config init.");
        JobSpringExecutor jobSpringExecutor = new JobSpringExecutor();
        if(enableServiceId){
            if(serviceId!=null && serviceId.trim().length()<1){
                logger.error("选择通过服务名注册到服务无法找到");
            }
            else{
                List<String> list = adminAdresses(serviceId);
                if(list!=null && list.size()>0){
                    StringBuilder sb = new StringBuilder();
                    for(String i :list){
                        sb.append(i).append(",");
                    }
                    String temp = sb.toString();
                    adminAddresses = temp.substring(0,temp.length()-1);
                }
                else{
                    logger.error("选择通过服务名注册到服务无法找到");
                }

            }
        }
        logger.info("Job adminServer at:{}",adminAddresses);
        jobSpringExecutor.setAdminAddresses(adminAddresses);
        jobSpringExecutor.setAppName(appName);
        jobSpringExecutor.setServiceId(serviceId);
        jobSpringExecutor.setIp(ip);
        jobSpringExecutor.setPort(port);
        jobSpringExecutor.setAccessToken(accessToken);
        jobSpringExecutor.setLogPath(logPath);
        jobSpringExecutor.setLogRetentionDays(logRetentionDays);
        jobSpringExecutor.setServiceIdEnabled(enableServiceId);
        return jobSpringExecutor;
    }

    private List<String> adminAdresses(String serviceId) {
        // 获取目标微服务的所有实例的请求地址
        return discoveryClient.getInstances(serviceId).stream()
                .map(i -> {
                    Map metaData = i.getMetadata();
                    String contextPath = null;
                    if(metaData.get("servlet.context-path")!=null && !"".equals(metaData.get("servlet.context-path"))){
                        contextPath = ((String)metaData.get("servlet.context-path"));
                    }
                   return  (new StringBuilder())
                            .append("http://")
                            .append(i.getHost())
                            .append(":")
                            .append(i.getPort())
                            .append("/")
                            .append(contextPath!=null?contextPath.substring(1):"")
                            .toString();
                })
                .collect(Collectors.toList());

    }

    /**
     * 针对多网卡、容器内部署等情况，可借助 "spring-cloud-commons" 提供的 "InetUtils" 组件灵活定制注册IP；
     *
     *      1、引入依赖：
     *          <dependency>
     *             <groupId>org.springframework.cloud</groupId>
     *             <artifactId>spring-cloud-commons</artifactId>
     *             <version>${version}</version>
     *         </dependency>
     *
     *      2、配置文件，或者容器启动变量
     *          spring.cloud.inetutils.preferred-networks: 'xxx.xxx.xxx.'
     *
     *      3、获取IP
     *          String ip_ = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
     */


}