package top.dddpeter.ishare.job.rpc.remoting.invoker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.dddpeter.ishare.job.rpc.registry.ServiceRegistry;
import top.dddpeter.ishare.job.rpc.registry.impl.LocalServiceRegistry;
import top.dddpeter.ishare.job.rpc.remoting.net.params.BaseCallback;
import top.dddpeter.ishare.job.rpc.remoting.net.params.RpcFutureResponse;
import top.dddpeter.ishare.job.rpc.remoting.net.params.RpcResponse;
import top.dddpeter.ishare.job.rpc.util.RpcException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * ishare-rpc invoker factory, init service-registry
 *
 * @author hqins 2019-12-10
 */
public class RpcInvokerFactory {
    private static final Logger logger = LoggerFactory.getLogger(RpcInvokerFactory.class);

    // ---------------------- default instance ----------------------

    private static final RpcInvokerFactory INSTANCE = new RpcInvokerFactory(LocalServiceRegistry.class, null);
    public static RpcInvokerFactory getInstance() {
        return INSTANCE;
    }


    // ---------------------- config ----------------------

    private Class<? extends ServiceRegistry> serviceRegistryClass;
    private Map<String, String> serviceRegistryParam;


    public RpcInvokerFactory(Class<? extends ServiceRegistry> serviceRegistryClass, Map<String, String> serviceRegistryParam) {
        this.serviceRegistryClass = serviceRegistryClass;
        this.serviceRegistryParam = serviceRegistryParam;
    }


    // ---------------------- start / stop ----------------------

    public void start() throws Exception {
        // start registry
        if (serviceRegistryClass != null) {
            serviceRegistry = serviceRegistryClass.getDeclaredConstructor().newInstance();
            serviceRegistry.start(serviceRegistryParam);
        }
    }

    public void  stop() throws Exception {
        // stop registry
        if (serviceRegistry != null) {
            serviceRegistry.stop();
        }

        // stop callback
        if (stopCallbackList.size() > 0) {
            for (BaseCallback callback: stopCallbackList) {
                try {
                    callback.run();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

        // stop CallbackThreadPool
        stopCallbackThreadPool();
    }


    // ---------------------- service registry ----------------------

    private ServiceRegistry serviceRegistry;
    public ServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }


    // ---------------------- service registry ----------------------

    private List<BaseCallback> stopCallbackList = new ArrayList<BaseCallback>();

    public void addStopCallBack(BaseCallback callback){
        stopCallbackList.add(callback);
    }


    // ---------------------- future-response pool ----------------------

    // XxlRpcFutureResponseFactory

    private ConcurrentMap<String, RpcFutureResponse> futureResponsePool = new ConcurrentHashMap<String, RpcFutureResponse>();
    public void setInvokerFuture(String requestId, RpcFutureResponse futureResponse){
        futureResponsePool.put(requestId, futureResponse);
    }
    public void removeInvokerFuture(String requestId){
        futureResponsePool.remove(requestId);
    }
    public void notifyInvokerFuture(String requestId, final RpcResponse rpcResponse){

        // get
        final RpcFutureResponse futureResponse = futureResponsePool.get(requestId);
        if (futureResponse == null) {
            return;
        }

        // notify
        if (futureResponse.getInvokeCallback()!=null) {

            // callback type
            try {
                executeResponseCallback(new Runnable() {
                    @Override
                    public void run() {
                        if (rpcResponse.getErrorMsg() != null) {
                            futureResponse.getInvokeCallback().onFailure(new RpcException(rpcResponse.getErrorMsg()));
                        } else {
                            futureResponse.getInvokeCallback().onSuccess(rpcResponse.getResult());
                        }
                    }
                });
            }catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        } else {

            // other nomal type
            futureResponse.setResponse(rpcResponse);
        }

        // do remove
        futureResponsePool.remove(requestId);

    }


    // ---------------------- response callback ThreadPool ----------------------

    private ThreadPoolExecutor responseCallbackThreadPool = null;
    public void executeResponseCallback(Runnable runnable){

        if (responseCallbackThreadPool == null) {
            synchronized (this) {
                if (responseCallbackThreadPool == null) {
                    responseCallbackThreadPool = new ThreadPoolExecutor(
                            10,
                            100,
                            60L,
                            TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(1000),
                            r -> new Thread(r, "ishare-rpc, XxlRpcInvokerFactory-responseCallbackThreadPool-" + r.hashCode()),
                            new RejectedExecutionHandler() {
                                @Override
                                public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                                    throw new RpcException("ishare-rpc Invoke Callback Thread pool is EXHAUSTED!");
                                }
                            });		// default maxThreads 300, minThreads 60
                }
            }
        }
        responseCallbackThreadPool.execute(runnable);
    }
    public void stopCallbackThreadPool() {
        if (responseCallbackThreadPool != null) {
            responseCallbackThreadPool.shutdown();
        }
    }

}
