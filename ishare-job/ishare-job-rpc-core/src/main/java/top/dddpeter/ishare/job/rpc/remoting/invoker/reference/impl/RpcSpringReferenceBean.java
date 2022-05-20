package top.dddpeter.ishare.job.rpc.remoting.invoker.reference.impl;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import top.dddpeter.ishare.job.rpc.remoting.invoker.reference.RpcReferenceBean;

/**
 * rpc reference bean, use by spring xml and annotation (for spring)
 *
 * @author hqins 2019-12-10
 */
public class RpcSpringReferenceBean implements FactoryBean<Object>, InitializingBean {


    // ---------------------- util ----------------------

    private RpcReferenceBean rpcReferenceBean;

    @Override
    public void afterPropertiesSet() {

        // init config
        this.rpcReferenceBean = new RpcReferenceBean();
    }


    @Override
    public Object getObject() throws Exception {
        return rpcReferenceBean.getObject();
    }

    @Override
    public Class<?> getObjectType() {
        return rpcReferenceBean.getIface();
    }

    @Override
    public boolean isSingleton() {
        return false;
    }


    /**
     *	public static <T> ClientProxy ClientProxy<T> getFuture(Class<T> type) {
     *		<T> ClientProxy proxy = (<T>) new ClientProxy();
     *		return proxy;
     *	}
     */


}
