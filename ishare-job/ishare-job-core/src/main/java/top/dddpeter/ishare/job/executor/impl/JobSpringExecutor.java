package top.dddpeter.ishare.job.executor.impl;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import top.dddpeter.ishare.job.biz.AdminBiz;
import top.dddpeter.ishare.job.biz.client.AdminBizClient;
import top.dddpeter.ishare.job.biz.model.ReturnT;
import top.dddpeter.ishare.job.executor.JobExecutor;
import top.dddpeter.ishare.job.glue.GlueFactory;
import top.dddpeter.ishare.job.handler.IJobHandler;
import top.dddpeter.ishare.job.handler.annotation.ClassJobHandler;
import top.dddpeter.ishare.job.handler.annotation.MethodJobHandler;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *ishare-job executor (for spring)
 *
 * @author hqins 2019-12-10
 */
public class JobSpringExecutor extends JobExecutor implements ApplicationContextAware, InitializingBean, DisposableBean {


    // start
    @Override
    public void afterPropertiesSet() throws Exception {

        // init JobHandler Repository（for class）
        initClassJobHandlerRepository(applicationContext);

        // init JobHandler Repository (for method)
        initJobHandlerMethodRepository(applicationContext);

        // refresh GlueFactory
        GlueFactory.refreshInstance(1);


        // super start
        super.start();
    }

    // destroy
    @Override
    public void destroy() {
        super.destroy();
    }
    private void initClassJobHandlerRepository(ApplicationContext applicationContext){
        if (applicationContext == null) {
            return;
        }

        // init job handler action
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(ClassJobHandler.class);

        if (serviceBeanMap!=null && serviceBeanMap.size()>0) {
            for (Object serviceBean : serviceBeanMap.values()) {
                if (serviceBean instanceof IJobHandler){
                    String name = serviceBean.getClass().getAnnotation(ClassJobHandler.class).value();
                    IJobHandler handler = (IJobHandler) serviceBean;
                    if (loadJobHandler(name) != null) {
                        throw new RuntimeException("job jobhandler["+ name +"] naming conflicts.");
                    }
                    registJobHandler(name, handler);
                }
            }
        }
    }

    private void initJobHandlerMethodRepository(ApplicationContext applicationContext) {
        if (applicationContext == null) {
            return;
        }

        // init job handler from method
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanDefinitionName);
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method: methods) {
                MethodJobHandler xxlJob = AnnotationUtils.findAnnotation(method, MethodJobHandler.class);
                if (xxlJob != null) {

                    // name
                    String name = xxlJob.value();
                    if (name.trim().length() == 0) {
                        throw new RuntimeException("xxl-job method-jobhandler name invalid, for[" + bean.getClass() + "#"+ method.getName() +"] .");
                    }
                    if (loadJobHandler(name) != null) {
                        throw new RuntimeException("xxl-job jobhandler[" + name + "] naming conflicts.");
                    }

                    // execute method
                    if (!(method.getParameterTypes()!=null && method.getParameterTypes().length==1 && method.getParameterTypes()[0].isAssignableFrom(String.class))) {
                        throw new RuntimeException("xxl-job method-jobhandler param-classtype invalid, for[" + bean.getClass() + "#"+ method.getName() +"] , " +
                                "The correct method format like \" public ReturnT<String> execute(String param) \" .");
                    }
                    if (!method.getReturnType().isAssignableFrom(ReturnT.class)) {
                        throw new RuntimeException("xxl-job method-jobhandler return-classtype invalid, for[" + bean.getClass() + "#"+ method.getName() +"] , " +
                                "The correct method format like \" public ReturnT<String> execute(String param) \" .");
                    }
                    method.setAccessible(true);

                    // init and destory
                    Method initMethod = null;
                    Method destroyMethod = null;

                    if(xxlJob.init().trim().length() > 0) {
                        try {
                            initMethod = bean.getClass().getDeclaredMethod(xxlJob.init());
                            initMethod.setAccessible(true);
                        } catch (NoSuchMethodException e) {
                            throw new RuntimeException("xxl-job method-jobhandler initMethod invalid, for[" + bean.getClass() + "#"+ method.getName() +"] .");
                        }
                    }
                    if(xxlJob.destroy().trim().length() > 0) {
                        try {
                            destroyMethod = bean.getClass().getDeclaredMethod(xxlJob.destroy());
                            destroyMethod.setAccessible(true);
                        } catch (NoSuchMethodException e) {
                            throw new RuntimeException("xxl-job method-jobhandler destroyMethod invalid, for[" + bean.getClass() + "#"+ method.getName() +"] .");
                        }
                    }

                    // registry jobhandler
                    registJobHandler(name, new top.dddpeter.ishare.job.handler.impl.MethodJobHandler(bean, method, initMethod, destroyMethod));
                }
            }
        }
    }

    // ---------------------- applicationContext ----------------------
    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        JobSpringExecutor.applicationContext = applicationContext;
    }
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static List<AdminBiz> getAdminBizList(){
        if(srvIdenable){
             List<AdminBiz> adminBizs= Arrays.stream(srvIdAddress.split(","))
                    .map(i ->{
                        return new AdminBizClient(i, acsToken);
                    })
                    .collect(Collectors.toList());
             if(adminBizs != null && adminBizs.size() >0 ){
                adminBizList = adminBizs;
             }

        }
        return adminBizList;
    }
}
