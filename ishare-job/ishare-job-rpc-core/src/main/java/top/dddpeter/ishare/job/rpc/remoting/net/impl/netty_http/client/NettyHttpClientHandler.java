package top.dddpeter.ishare.job.rpc.remoting.net.impl.netty_http.client;

import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.dddpeter.ishare.job.rpc.remoting.invoker.RpcInvokerFactory;
import top.dddpeter.ishare.job.rpc.remoting.net.params.Beat;
import top.dddpeter.ishare.job.rpc.remoting.net.params.RpcResponse;
import top.dddpeter.ishare.job.rpc.serialize.Serializer;
import top.dddpeter.ishare.job.rpc.util.RpcException;

/**
 * netty_http
 *
 * @author hqins 2019-12-10
 */
public class NettyHttpClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
    private static final Logger logger = LoggerFactory.getLogger(NettyHttpClientHandler.class);


    private RpcInvokerFactory rpcInvokerFactory;
    private Serializer serializer;
    private NettyHttpConnectClient nettyHttpConnectClient;
    public NettyHttpClientHandler(final RpcInvokerFactory rpcInvokerFactory, Serializer serializer, final NettyHttpConnectClient nettyHttpConnectClient) {
        this.rpcInvokerFactory = rpcInvokerFactory;
        this.serializer = serializer;
        this.nettyHttpConnectClient = nettyHttpConnectClient;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {

        // valid status
        if (!HttpResponseStatus.OK.equals(msg.status())) {
            throw new RpcException("ishare-rpc response status invalid.");
        }

        // response parse
        byte[] responseBytes = ByteBufUtil.getBytes(msg.content());

        // valid length
        if (responseBytes.length == 0) {
            throw new RpcException("ishare-rpc response data empty.");
        }

        // response deserialize
        RpcResponse rpcResponse = (RpcResponse) serializer.deserialize(responseBytes, RpcResponse.class);

        // notify response
        rpcInvokerFactory.notifyInvokerFuture(rpcResponse.getRequestId(), rpcResponse);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        logger.error(">>>>>>>>>>> ishare-rpc netty_http client caught exception", cause);
        ctx.close();
    }

    /*@Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // retry
        super.channelInactive(ctx);
    }*/

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            /*ctx.channel().close();      // close idle channel
            logger.debug(">>>>>>>>>>> ishare-rpc netty_http client close an idle channel.");*/

            nettyHttpConnectClient.send(Beat.BEAT_PING);    // beat N, close if fail(may throw error)
            logger.debug(">>>>>>>>>>> ishare-rpc netty_http client send beat-ping.");
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
