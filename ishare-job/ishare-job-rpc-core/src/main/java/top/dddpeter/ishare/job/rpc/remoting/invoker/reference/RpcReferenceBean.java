package top.dddpeter.ishare.job.rpc.remoting.invoker.reference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.dddpeter.ishare.job.rpc.remoting.invoker.RpcInvokerFactory;
import top.dddpeter.ishare.job.rpc.remoting.invoker.call.CallType;
import top.dddpeter.ishare.job.rpc.remoting.invoker.call.RpcInvokeCallback;
import top.dddpeter.ishare.job.rpc.remoting.invoker.call.RpcInvokeFuture;
import top.dddpeter.ishare.job.rpc.remoting.invoker.generic.RpcGenericService;
import top.dddpeter.ishare.job.rpc.remoting.invoker.route.LoadBalance;
import top.dddpeter.ishare.job.rpc.remoting.net.Client;
import top.dddpeter.ishare.job.rpc.remoting.net.impl.netty.client.NettyClient;
import top.dddpeter.ishare.job.rpc.remoting.net.params.RpcFutureResponse;
import top.dddpeter.ishare.job.rpc.remoting.net.params.RpcRequest;
import top.dddpeter.ishare.job.rpc.remoting.net.params.RpcResponse;
import top.dddpeter.ishare.job.rpc.remoting.provider.RpcProviderFactory;
import top.dddpeter.ishare.job.rpc.serialize.Serializer;
import top.dddpeter.ishare.job.rpc.serialize.impl.HessianSerializer;
import top.dddpeter.ishare.job.rpc.util.ClassUtil;
import top.dddpeter.ishare.job.rpc.util.RpcException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * rpc reference bean, use by api
 *
 * @author hqins 2019-12-10
 */
public class RpcReferenceBean {
	private static final Logger logger = LoggerFactory.getLogger(RpcReferenceBean.class);
	// [tips01: save 30ms/100invoke. why why why??? with this logger, it can save lots of time.]


	// ---------------------- config ----------------------

	private Class<? extends Client> client = NettyClient.class;
	private Class<? extends Serializer> serializer = HessianSerializer.class;
	private CallType callType = CallType.SYNC;
	private LoadBalance loadBalance = LoadBalance.ROUND;

	private Class<?> iface = null;
	private String version = null;

	private long timeout = 1000;

	private String address = null;
	private String accessToken = null;

	private RpcInvokeCallback invokeCallback = null;

	private RpcInvokerFactory invokerFactory = null;


	// set
	public void setClient(Class<? extends Client> client) {
		this.client = client;
	}
	public void setSerializer(Class<? extends Serializer> serializer) {
		this.serializer = serializer;
	}
	public void setCallType(CallType callType) {
		this.callType = callType;
	}
	public void setLoadBalance(LoadBalance loadBalance) {
		this.loadBalance = loadBalance;
	}
	public void setIface(Class<?> iface) {
		this.iface = iface;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public void setInvokeCallback(RpcInvokeCallback invokeCallback) {
		this.invokeCallback = invokeCallback;
	}
	public void setInvokerFactory(RpcInvokerFactory invokerFactory) {
		this.invokerFactory = invokerFactory;
	}


	// get
	public Serializer getSerializerInstance() {
		return serializerInstance;
	}
	public long getTimeout() {
		return timeout;
	}

	public RpcInvokerFactory getInvokerFactory() {
		return invokerFactory;
	}
	public Class<?> getIface() {
		return iface;
	}


	// ---------------------- initClient ----------------------

	private Client clientInstance = null;
	private Serializer serializerInstance = null;

	public RpcReferenceBean initClient() throws Exception {

		// valid
		if (this.client == null) {
			throw new RpcException("ishare-rpc reference client missing.");
		}
		if (this.serializer == null) {
			throw new RpcException("ishare-rpc reference serializer missing.");
		}
		if (this.callType==null) {
			throw new RpcException("ishare-rpc reference callType missing.");
		}
		if (this.loadBalance==null) {
			throw new RpcException("ishare-rpc reference loadBalance missing.");
		}
		if (this.iface==null) {
			throw new RpcException("ishare-rpc reference iface missing.");
		}
		if (this.timeout < 0) {
			this.timeout = 0;
		}
		if (this.invokerFactory == null) {
			this.invokerFactory = RpcInvokerFactory.getInstance();
		}

		// init serializerInstance
		this.serializerInstance = serializer.newInstance();

		// init Client
		clientInstance = client.newInstance();
		clientInstance.init(this);

		return this;
	}


	// ---------------------- util ----------------------

	public Object getObject() throws Exception {

		// initClient
		initClient();

		// newProxyInstance
		return Proxy.newProxyInstance(Thread.currentThread()
				.getContextClassLoader(), new Class[] { iface },
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

						// method param
						String className = method.getDeclaringClass().getName();	// iface.getName()
						String varsion_ = version;
						String methodName = method.getName();
						Class<?>[] parameterTypes = method.getParameterTypes();
						Object[] parameters = args;

						// filter for generic
						if (className.equals(RpcGenericService.class.getName()) && methodName.equals("invoke")) {

							Class<?>[] paramTypes = null;
							if (args[3]!=null) {
								String[] paramTypes_str = (String[]) args[3];
								if (paramTypes_str.length > 0) {
									paramTypes = new Class[paramTypes_str.length];
									for (int i = 0; i < paramTypes_str.length; i++) {
										paramTypes[i] = ClassUtil.resolveClass(paramTypes_str[i]);
									}
								}
							}

							className = (String) args[0];
							varsion_ = (String) args[1];
							methodName = (String) args[2];
							parameterTypes = paramTypes;
							parameters = (Object[]) args[4];
						}

						// filter method like "Object.toString()"
						if (className.equals(Object.class.getName())) {
							logger.info(">>>>>>>>>>> ishare-rpc proxy class-method not support [{}#{}]", className, methodName);
							throw new RpcException("ishare-rpc proxy class-method not support");
						}

						// address
						String finalAddress = address;
						if (finalAddress==null || finalAddress.trim().length()==0) {
							if (invokerFactory!=null && invokerFactory.getServiceRegistry()!=null) {
								// discovery
								String serviceKey = RpcProviderFactory.makeServiceKey(className, varsion_);
								TreeSet<String> addressSet = invokerFactory.getServiceRegistry().discovery(serviceKey);
								// load balance
								if (addressSet==null || addressSet.size()==0) {
									// pass
								} else if (addressSet.size()==1) {
									finalAddress = addressSet.first();
								} else {
									finalAddress = loadBalance.xxlRpcInvokerRouter.route(serviceKey, addressSet);
								}

							}
						}
						if (finalAddress==null || finalAddress.trim().length()==0) {
							throw new RpcException("ishare-rpc reference bean["+ className +"] address empty");
						}

						// request
						RpcRequest rpcRequest = new RpcRequest();
	                    rpcRequest.setRequestId(UUID.randomUUID().toString());
	                    rpcRequest.setCreateMillisTime(System.currentTimeMillis());
	                    rpcRequest.setAccessToken(accessToken);
	                    rpcRequest.setClassName(className);
	                    rpcRequest.setMethodName(methodName);
	                    rpcRequest.setParameterTypes(parameterTypes);
	                    rpcRequest.setParameters(parameters);
	                    rpcRequest.setVersion(version);
	                    
	                    // send
						if (CallType.SYNC == callType) {
							// future-response set
							RpcFutureResponse futureResponse = new RpcFutureResponse(invokerFactory, rpcRequest, null);
							try {
								// do invoke
								clientInstance.asyncSend(finalAddress, rpcRequest);

								// future get
								RpcResponse rpcResponse = futureResponse.get(timeout, TimeUnit.MILLISECONDS);
								if (rpcResponse.getErrorMsg() != null) {
									throw new RpcException(rpcResponse.getErrorMsg());
								}
								return rpcResponse.getResult();
							} catch (Exception e) {
								logger.info(">>>>>>>>>>> ishare-rpc, invoke error, address:{}, XxlRpcRequest{}", finalAddress, rpcRequest);

								throw (e instanceof RpcException)?e:new RpcException(e);
							} finally{
								// future-response remove
								futureResponse.removeInvokerFuture();
							}
						} else if (CallType.FUTURE == callType) {
							// future-response set
							RpcFutureResponse futureResponse = new RpcFutureResponse(invokerFactory, rpcRequest, null);
                            try {
								// invoke future set
								RpcInvokeFuture invokeFuture = new RpcInvokeFuture(futureResponse);
								RpcInvokeFuture.setFuture(invokeFuture);

                                // do invoke
								clientInstance.asyncSend(finalAddress, rpcRequest);

                                return null;
                            } catch (Exception e) {
								logger.info(">>>>>>>>>>> ishare-rpc, invoke error, address:{}, XxlRpcRequest{}", finalAddress, rpcRequest);

								// future-response remove
								futureResponse.removeInvokerFuture();

								throw (e instanceof RpcException)?e:new RpcException(e);
                            }

						} else if (CallType.CALLBACK == callType) {

							// get callback
							RpcInvokeCallback finalInvokeCallback = invokeCallback;
							RpcInvokeCallback threadInvokeCallback = RpcInvokeCallback.getCallback();
							if (threadInvokeCallback != null) {
								finalInvokeCallback = threadInvokeCallback;
							}
							if (finalInvokeCallback == null) {
								throw new RpcException("ishare-rpc XxlRpcInvokeCallback（CallType="+ CallType.CALLBACK.name() +"） cannot be null.");
							}

							// future-response set
							RpcFutureResponse futureResponse = new RpcFutureResponse(invokerFactory, rpcRequest, finalInvokeCallback);
							try {
								clientInstance.asyncSend(finalAddress, rpcRequest);
							} catch (Exception e) {
								logger.info(">>>>>>>>>>> ishare-rpc, invoke error, address:{}, XxlRpcRequest{}", finalAddress, rpcRequest);

								// future-response remove
								futureResponse.removeInvokerFuture();

								throw (e instanceof RpcException)?e:new RpcException(e);
							}

							return null;
						} else if (CallType.ONEWAY == callType) {
							clientInstance.asyncSend(finalAddress, rpcRequest);
                            return null;
                        } else {
							throw new RpcException("ishare-rpc callType["+ callType +"] invalid");
						}

					}
				});
	}

}
