package top.dddpeter.ishare.job.rpc.remoting.invoker.call;

/**
 * rpc call type
 *
 * @author hqins 2019-12-10
 */
public enum CallType {


    SYNC,

    FUTURE,

    CALLBACK,

    ONEWAY;


    public static CallType match(String name, CallType defaultCallType){
        for (CallType item : CallType.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return defaultCallType;
    }

}
