package top.dddpeter.ishare.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.dddpeter.ishare.gateway.bank.mapper.BankInfoMapper;
import top.dddpeter.ishare.gateway.bank.po.BankInfo;
import top.dddpeter.ishare.gateway.netty.NettyServerThread;
import top.dddpeter.ishare.gateway.netty.NettyServerWithMeta;
import top.dddpeter.ishare.gateway.netty.initializer.ServerChannelInitializer;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lijinde
 */
@Configuration
public class BankServerConfig {
    @Resource
    BankInfoMapper bankInfoMapper;
    @Bean
    public Map<String,NettyServerThread> nettyServerThread() {
        //  从数据库取到环境的配置，初始化服务端口
        List<BankInfo> bankList =  bankInfoMapper.selectAll();
        Map<String,NettyServerThread>  serverThreadMap  = new ConcurrentHashMap<>(32);

        bankList.forEach(bankInfo -> {
            ServerChannelInitializer initializer = new ServerChannelInitializer(bankInfo);
            NettyServerWithMeta nettyServerWithMeta = new NettyServerWithMeta(bankInfo.getBankCode(),
                    bankInfo.getBankName(),bankInfo.getServicePort(),initializer);
            NettyServerThread thread = new NettyServerThread(nettyServerWithMeta);
            thread.start();
            serverThreadMap.put(bankInfo.getBankCode(),thread);
        });

        return serverThreadMap;
    }
}
