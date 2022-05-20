package top.dddpeter.ishare.gateway.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.net.InetSocketAddress;

/**
 * @author hqins
 */
public interface NettyServer {
    void start(InetSocketAddress socketAddress, ChannelInitializer<SocketChannel> initializer);
}
