package top.dddpeter.ishare.job.rpc.remoting.provider.impl;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import top.dddpeter.ishare.job.rpc.remoting.provider.RpcProviderFactory;
import top.dddpeter.ishare.job.rpc.remoting.provider.annotation.RpcService;
import top.dddpeter.ishare.job.rpc.util.RpcException;

import java.util.Map;

/**
 * ishare-rpc provider (for spring)
 *
 * @author hqins 2019-12-10
 */
public class RpcSpringProviderFactory extends RpcProviderFactory implements ApplicationContextAware, InitializingBean,DisposableBean {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (serviceBeanMap!=null && serviceBeanMap.size()>0) {
            for (Object serviceBean : serviceBeanMap.values()) {
                // valid
                if (serviceBean.getClass().getInterfaces().length ==0) {
                    throw new RpcException("ishare-rpc, service(XxlRpcService) must inherit interface.");
                }
                // add service
                RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);

                String iface = serviceBean.getClass().getInterfaces()[0].getName();
                String version = rpcService.version();

                super.addService(iface, version, serviceBean);
            }
        }

        // TODO，addServices by api + prop

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.start();
    }

    @Override
    public void destroy() throws Exception {
        super.stop();
    }

}
