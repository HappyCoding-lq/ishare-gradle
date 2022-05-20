package top.dddpeter.ishare.job.rpc.remoting.net.params;

/**
 * beat for keep-alive
 *
 * @author hqins 2019-12-10
 */
public final class Beat {

    public static final int BEAT_INTERVAL = 30;
    public static final String BEAT_ID = "BEAT_PING_PONG";

    public static RpcRequest BEAT_PING;

    static {
        BEAT_PING = new RpcRequest(){};
        BEAT_PING.setRequestId(BEAT_ID);
    }

}
