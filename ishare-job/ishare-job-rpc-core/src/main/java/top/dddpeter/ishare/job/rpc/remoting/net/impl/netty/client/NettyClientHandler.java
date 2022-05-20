package top.dddpeter.ishare.job.rpc.remoting.net.impl.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.dddpeter.ishare.job.rpc.remoting.invoker.RpcInvokerFactory;
import top.dddpeter.ishare.job.rpc.remoting.net.params.Beat;
import top.dddpeter.ishare.job.rpc.remoting.net.params.RpcResponse;

/**
 * rpc netty client handler
 *
 * @author hqins 2019-12-10
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
	private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);


	private RpcInvokerFactory rpcInvokerFactory;
	private NettyConnectClient nettyConnectClient;
	public NettyClientHandler(final RpcInvokerFactory rpcInvokerFactory, NettyConnectClient nettyConnectClient) {
		this.rpcInvokerFactory = rpcInvokerFactory;
		this.nettyConnectClient = nettyConnectClient;
	}


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcResponse rpcResponse) throws Exception {

		// notify response
		rpcInvokerFactory.notifyInvokerFuture(rpcResponse.getRequestId(), rpcResponse);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error(">>>>>>>>>>> ishare-rpc netty client caught exception", cause);
		ctx.close();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent){
			/*ctx.channel().close();      // close idle channel
			logger.debug(">>>>>>>>>>> ishare-rpc netty client close an idle channel.");*/

			nettyConnectClient.send(Beat.BEAT_PING);	// beat N, close if fail(may throw error)
			logger.debug(">>>>>>>>>>> ishare-rpc netty client send beat-ping.");

		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

}
