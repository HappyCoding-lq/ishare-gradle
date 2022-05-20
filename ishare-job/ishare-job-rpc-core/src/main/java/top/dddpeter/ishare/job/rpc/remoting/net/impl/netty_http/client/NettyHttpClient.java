package top.dddpeter.ishare.job.rpc.remoting.net.impl.netty_http.client;

import top.dddpeter.ishare.job.rpc.remoting.net.Client;
import top.dddpeter.ishare.job.rpc.remoting.net.common.ConnectClient;
import top.dddpeter.ishare.job.rpc.remoting.net.params.RpcRequest;

/**
 * netty_http client
 *
 * @author hqins 2019-12-10
 */
public class NettyHttpClient extends Client {

    private Class<? extends ConnectClient> connectClientImpl = NettyHttpConnectClient.class;

    @Override
    public void asyncSend(String address, RpcRequest rpcRequest) throws Exception {
        ConnectClient.asyncSend(rpcRequest, address, connectClientImpl, rpcReferenceBean);
    }

}
