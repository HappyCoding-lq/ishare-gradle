package top.dddpeter.ishare.job.rpc.remoting.net.impl.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.dddpeter.ishare.job.rpc.remoting.net.params.Beat;
import top.dddpeter.ishare.job.rpc.remoting.net.params.RpcRequest;
import top.dddpeter.ishare.job.rpc.remoting.net.params.RpcResponse;
import top.dddpeter.ishare.job.rpc.remoting.provider.RpcProviderFactory;
import top.dddpeter.ishare.job.rpc.util.ThrowableUtil;

import java.util.concurrent.ThreadPoolExecutor;


/**
 * netty server handler
 *
 * @author hqins 2019-12-10
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

    private RpcProviderFactory rpcProviderFactory;
    private ThreadPoolExecutor serverHandlerPool;

    public NettyServerHandler(final RpcProviderFactory rpcProviderFactory, final ThreadPoolExecutor serverHandlerPool) {
        this.rpcProviderFactory = rpcProviderFactory;
        this.serverHandlerPool = serverHandlerPool;
    }


    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final RpcRequest rpcRequest) throws Exception {

        // filter beat
        if (Beat.BEAT_ID.equalsIgnoreCase(rpcRequest.getRequestId())){
            logger.debug(">>>>>>>>>>> ishare-rpc provider netty server read beat-ping.");
            return;
        }

        // do invoke
        try {
            serverHandlerPool.execute(new Runnable() {
                @Override
                public void run() {
                    // invoke + response
                    RpcResponse rpcResponse = rpcProviderFactory.invokeService(rpcRequest);

                    ctx.writeAndFlush(rpcResponse);
                }
            });
        } catch (Exception e) {
            // catch error
            RpcResponse rpcResponse = new RpcResponse();
            rpcResponse.setRequestId(rpcRequest.getRequestId());
            rpcResponse.setErrorMsg(ThrowableUtil.toString(e));

            ctx.writeAndFlush(rpcResponse);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	logger.error(">>>>>>>>>>> ishare-rpc provider netty server caught exception", cause);
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            ctx.channel().close();      // beat 3N, close if idle
            logger.debug(">>>>>>>>>>> ishare-rpc provider netty server close an idle channel.");
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
