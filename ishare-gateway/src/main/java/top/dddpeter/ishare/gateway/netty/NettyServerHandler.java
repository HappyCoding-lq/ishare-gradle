package top.dddpeter.ishare.gateway.netty;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import top.dddpeter.ishare.gateway.bank.po.BankInfo;

/**
 * @author ChinaPost-life
 *
 * netty服务端处理器
 **/
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private BankInfo bankInfo;
    public NettyServerHandler(BankInfo bankInfo){
        log.info("银行初始化信息{}",bankInfo);
        this.bankInfo = bankInfo;
    }
    /**
     * 客户端连接会触发
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("银行初始化成功");
    }

    /**
     * 客户端发消息会触发
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("{}-{}服务器收到消息: {}",bankInfo.getBankCode(), bankInfo.getBankName(),msg.toString());
        //todo： 报文处理调用后端服务
        Thread.sleep(600);
        ChannelFuture future = ctx.writeAndFlush("hello");
        future.addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 发生异常触发
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
