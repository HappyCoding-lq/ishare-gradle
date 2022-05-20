package top.dddpeter.ishare.gateway.netty;

import java.net.InetSocketAddress;

/**
 * @author hqins
 */
public class NettyServerThread  extends Thread  {
    private NettyServerWithMeta nettyServerWithMeta;
    public NettyServerThread(NettyServerWithMeta nettyServerWithMeta){
        this.nettyServerWithMeta = nettyServerWithMeta;
    }

    @Override
    public void run() {
        NettyServer server = nettyServerWithMeta.getNettyServer();
        server.start(new InetSocketAddress(nettyServerWithMeta.getPort()),nettyServerWithMeta.getInitializer());
    }
}
