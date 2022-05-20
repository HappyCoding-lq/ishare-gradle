package top.dddpeter.ishare.job.rpc.remoting.invoker.route.impl;

import top.dddpeter.ishare.job.rpc.remoting.invoker.route.RpcLoadBalance;

import java.util.Random;
import java.util.TreeSet;

/**
 * random
 *
 * @author hqins 2019-12-10
 */
public class RpcLoadBalanceRandomStrategy extends RpcLoadBalance {

    private Random random = new Random();

    @Override
    public String route(String serviceKey, TreeSet<String> addressSet) {
        // arr
        String[] addressArr = addressSet.toArray(new String[addressSet.size()]);

        // random
        String finalAddress = addressArr[random.nextInt(addressSet.size())];
        return finalAddress;
    }

}
