package top.dddpeter.ishare.job.rpc.remoting.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.dddpeter.ishare.job.rpc.remoting.invoker.reference.RpcReferenceBean;
import top.dddpeter.ishare.job.rpc.remoting.net.params.RpcRequest;

/**
 * i client
 * @author hqins 2019-12-10
 */
public abstract class Client {
	protected static final Logger logger = LoggerFactory.getLogger(Client.class);


	// ---------------------- init ----------------------

	protected volatile RpcReferenceBean rpcReferenceBean;

	public void init(RpcReferenceBean rpcReferenceBean) {
		this.rpcReferenceBean = rpcReferenceBean;
	}


    // ---------------------- send ----------------------

	/**
	 * async send, bind requestId and future-response
	 *
	 * @param address
	 * @param rpcRequest
	 * @return
	 * @throws Exception
	 */
	public abstract void asyncSend(String address, RpcRequest rpcRequest) throws Exception;

}
