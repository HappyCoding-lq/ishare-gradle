package top.dddpeter.ishare.job.rpc.remoting.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.dddpeter.ishare.job.rpc.remoting.net.params.BaseCallback;
import top.dddpeter.ishare.job.rpc.remoting.provider.RpcProviderFactory;

/**
 * server
 *
 * @author hqins 2019-12-10
 */
public abstract class Server {
	protected static final Logger logger = LoggerFactory.getLogger(Server.class);


	private BaseCallback startedCallback;
	private BaseCallback stopedCallback;

	public void setStartedCallback(BaseCallback startedCallback) {
		this.startedCallback = startedCallback;
	}

	public void setStopedCallback(BaseCallback stopedCallback) {
		this.stopedCallback = stopedCallback;
	}


	/**
	 * start server
	 *
	 * @param rpcProviderFactory
	 * @throws Exception
	 */
	public abstract void start(final RpcProviderFactory rpcProviderFactory) throws Exception;

	/**
	 * callback when started
	 */
	public void onStarted() {
		if (startedCallback != null) {
			try {
				startedCallback.run();
			} catch (Exception e) {
				logger.error(">>>>>>>>>>> ishare-rpc, server startedCallback error.", e);
			}
		}
	}

	/**
	 * stop server
	 *
	 * @throws Exception
	 */
	public abstract void stop() throws Exception;

	/**
	 * callback when stoped
	 */
	public void onStoped() {
		if (stopedCallback != null) {
			try {
				stopedCallback.run();
			} catch (Exception e) {
				logger.error(">>>>>>>>>>> ishare-rpc, server stopedCallback error.", e);
			}
		}
	}

}
