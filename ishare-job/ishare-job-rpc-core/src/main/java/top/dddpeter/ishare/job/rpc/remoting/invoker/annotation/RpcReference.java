package top.dddpeter.ishare.job.rpc.remoting.invoker.annotation;

import top.dddpeter.ishare.job.rpc.remoting.invoker.call.CallType;
import top.dddpeter.ishare.job.rpc.remoting.invoker.route.LoadBalance;
import top.dddpeter.ishare.job.rpc.remoting.net.Client;
import top.dddpeter.ishare.job.rpc.remoting.net.impl.netty.client.NettyClient;
import top.dddpeter.ishare.job.rpc.serialize.Serializer;
import top.dddpeter.ishare.job.rpc.serialize.impl.HessianSerializer;

import java.lang.annotation.*;

/**
 * rpc service annotation, skeleton of stub ("@Inherited" allow service use "Transactional")
 *
 * @author 2015-10-29 19:44:33
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RpcReference {

    Class<? extends Client> client() default NettyClient.class;
    Class<? extends Serializer> serializer() default HessianSerializer.class;
    CallType callType() default CallType.SYNC;
    LoadBalance loadBalance() default LoadBalance.ROUND;

    //Class<?> iface;
    String version() default "";

    long timeout() default 1000;

    String address() default "";
    String accessToken() default "";

    //XxlRpcInvokeCallback invokeCallback() ;

}
