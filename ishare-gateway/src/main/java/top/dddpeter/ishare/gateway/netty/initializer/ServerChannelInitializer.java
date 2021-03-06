package top.dddpeter.ishare.gateway.netty.initializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import top.dddpeter.ishare.gateway.bank.po.BankInfo;
import top.dddpeter.ishare.gateway.netty.NettyServerHandler;

/**
 * @author ChinaPost-life
 *
 * netty服务初始化器
 **/
@Slf4j
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    private BankInfo bankInfo;
    public ServerChannelInitializer(BankInfo bankInfo){
        this.bankInfo  = bankInfo;
    }
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //添加编解码
        socketChannel.pipeline().addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
        socketChannel.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
        socketChannel.pipeline().addLast(new NettyServerHandler(this.bankInfo));
    }
}
