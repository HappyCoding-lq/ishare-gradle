package top.dddpeter.ishare.common.mongo.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * mongodb连接池初始化
 *
 * @author huangshuanbao
 * @date 2020-01-20
 */
@Configuration
@Slf4j
public class MongoConfig {
    @Value("${spring.data.mongodb.authenticationDatabase:#{null}}")
    private String authenticationDatabase;

    @Value("${spring.data.mongodb.address}")
    private List<String>  address;

    @Value("${spring.data.mongodb.username}")
    private String username;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Value("${spring.data.mongodb.minConn}")
    private int minConn;

    @Value("${spring.data.mongodb.maxConn}")
    private int maxConn;

    @Value("${spring.data.mongodb.maxWaitTime}")
    private int maxWaitTime;

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Value("${mongo.replicaSetName:#{null}}")
    private String replicaSetName;

    @Bean
    public MongoDbFactory mongoDbFactory() {
        try {
            // 客户端配置
            MongoClientOptions.Builder builder = MongoClientOptions.builder();
            builder.minConnectionsPerHost(minConn);
            builder.connectionsPerHost(maxConn);
            if (!StringUtils.isEmpty(replicaSetName)) {
                builder.requiredReplicaSetName(replicaSetName);
            }
            builder.maxConnectionIdleTime(maxWaitTime);
            builder.maxWaitTime(maxWaitTime);
            MongoClientOptions options = builder.build();

            // MongoDB地址列表
            List<ServerAddress> serverAddresses = new ArrayList<>();
            for(String everyAddress : address){
                String[] split = everyAddress.split(":");
                String host = split[0];
                Integer port = Integer.parseInt(split[1]);
                ServerAddress serverAddress = new ServerAddress(host, port);
                serverAddresses.add(serverAddress);
            }

            // 连接认证
            MongoCredential credentials = MongoCredential.createScramSha1Credential(username, !StringUtils.isEmpty(authenticationDatabase)?authenticationDatabase:database, password.toCharArray());

            // 创建客户端和Factory
            MongoClient mongoClient = new MongoClient(serverAddresses, credentials, options);
            MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongoClient, database);
            log.info("mongodb 连接池初始化成功！");
            return mongoDbFactory;
        } catch (Exception e) {
            log.error("mongodb 连接池初始化失败,异常信息：", e);
            throw e;
        }
    }
}
