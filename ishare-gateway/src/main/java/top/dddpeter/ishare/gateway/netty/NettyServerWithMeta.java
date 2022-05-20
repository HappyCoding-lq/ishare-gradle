package top.dddpeter.ishare.gateway.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
public class NettyServerWithMeta {
    public String getChannelCode() {
        return channelCode;
    }

    public String getChannelName() {
        return channelName;
    }


    public NettyServer getNettyServer() {
        return nettyServer;
    }

    private String channelCode;
    private String channelName;
    private NettyServer nettyServer;
    private ChannelInitializer<SocketChannel> initializer;

    public ChannelInitializer<SocketChannel> getInitializer() {
        return initializer;
    }

    public void setInitializer(ChannelInitializer<SocketChannel> initializer) {
        this.initializer = initializer;
    }



    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private int port;

    /**
     *
     * @param channelCode
     * @param channelName
     */
    public NettyServerWithMeta(String channelCode,String channelName,int port,ChannelInitializer<SocketChannel> initializer){
        this.channelCode = channelCode;
        this.channelName = channelName;
        this.port = port;
        this.initializer = initializer;
        log.info("渠道代码:{},渠道名称:{},服务端口:{}",channelCode,channelName,port);
        this.nettyServer  =new NettyServerImpl();
    }
}
