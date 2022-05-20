package top.dddpeter.ishare.common.mongo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import top.dddpeter.ishare.common.mongo.dao.AbstractMongoDao;
import top.dddpeter.ishare.common.mongo.exception.MongoException;


/**
 * 接口调用工具(保存接口调用通用Mo日志)
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/27 7:57 下午
 */
@Slf4j
public class CallWithMongoLogUtil {

    private AbstractMongoDao mongoDao;

    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 构造函数
     *
     * @author : huangshuanbao
     * @date : 2020/2/27 7:40 下午
     * @param mongoDao mongodb collection的操作实现
     * @param threadPoolTaskExecutor 执行异步保存日志的线程池
     */
    public CallWithMongoLogUtil(AbstractMongoDao mongoDao, ThreadPoolTaskExecutor threadPoolTaskExecutor){
        this.mongoDao = mongoDao;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    /**
     * http json post方式调用
     *
     * @author : huangshuanbao
     * @date : 2020/2/27 7:40 下午
     * @param httpUrl 接口地址
     * @param funcFlag 接口标识
     * @param jsonParam json报文
     * @return String – 调用结果
     */
    public String httpPost(String httpUrl, String funcFlag, String jsonParam) throws MongoException {
        //mongodb保存请求报文
        String uuid = MongoLogUtil.saveCallRequest(mongoDao, jsonParam, funcFlag);
        try{
            String result = HttpUtil.httpPost(httpUrl, jsonParam);
            //mongodb异步保存响应报文
            MongoLogUtil.saveCallResponse(mongoDao, result, uuid);
            if (StringUtils.isBlank(result)) {
                throw new MongoException("发送请求失败，返回报文为空");
            }
            return result;
        } catch (MongoException ex) {
            log.error("请求出错",ex);
            threadPoolTaskExecutor.execute(() ->
                    //mongodb异步保存响应报文
                    MongoLogUtil.saveCallResponse(mongoDao, "请求出错", uuid)
            );
            throw ex;
        }
    }

}
