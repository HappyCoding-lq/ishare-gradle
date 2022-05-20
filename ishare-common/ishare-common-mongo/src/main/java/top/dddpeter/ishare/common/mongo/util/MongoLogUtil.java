package top.dddpeter.ishare.common.mongo.util;

import lombok.extern.slf4j.Slf4j;
import top.dddpeter.ishare.common.mongo.dao.AbstractMongoDao;
import top.dddpeter.ishare.common.mongo.domain.CallCommonMongoLog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * mongodb日志存储工具
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/27 3:50 下午
 */
@Slf4j
public class MongoLogUtil {

    private MongoLogUtil() {}

    /**
     * 使用mongodb存储日志
     *
     * <p>日志对象外部传入</p>
     *
     * @author : huangshuanbao
     * @date : 2020/2/27 3:50 下午
     * @param mongoDao mongodb collection的操作实现
     * @param serializableLog 请求日志（对象类型需要实现Serializable接口）
     */
    public static void saveLog(AbstractMongoDao mongoDao, Serializable serializableLog) {
        try {
            mongoDao.insertOne(serializableLog);
            log.info("mongodb存储日志信息成功!");
        } catch (Exception e) {
            //异常不影响后续操作
            log.error("mongodb存储日志信息异常, e={}", e.getMessage());
        }
    }

    /**
     * 使用mongodb存储接口请求日志
     *
     * <p>日志对象外部传入</p>
     * <p>与MongoOperationUtil.saveCallResponse方法一同使用</p>
     *
     * @author : huangshuanbao
     * @date : 2020/2/27 3:50 下午
     * @param mongoDao mongodb collection的操作实现
     * @param callLog 请求日志（对象类型需要实现Serializable接口）
     */
    public static void saveCallRequest(AbstractMongoDao mongoDao, Serializable callLog) {
        try {
            mongoDao.insertOne(callLog);
            log.info("mongodb存储请求报文信息成功!");
        } catch (Exception e) {
            //异常不影响后续操作
            log.error("mongodb存储请求报文信息异常, e={}", e.getMessage());
        }
    }

    /**
     * 使用mongodb存储接口响应日志
     *
     * <p>日志对象外部传入</p>
     * <p>依赖MongoOperationUtil.saveCallRequest方法的使用</p>
     *
     * @author : huangshuanbao
     * @date : 2020/2/27 3:50 下午
     * @param mongoDao mongodb collection的操作实现
     * @param callLog 响应日志（对象类型需要实现Serializable接口）
     * @param keyName 更新日志key的名字
     * @param keyValue 更新日志key的值
     */
    public static void saveCallResponse(AbstractMongoDao mongoDao, Serializable callLog, String keyName, String keyValue) {
        try {
            mongoDao.updateOne(generateUpdateQuery(keyName, keyValue), callLog);
            log.info("mongodb存储响应报文信息成功!");
        } catch (Exception e) {
            //mongodb异常不影响后续操作
            log.error("mongodb存储响应报文信息异常, e={}", e.getMessage());
        }
    }

    /**
     * 使用mongodb存储接口请求日志
     *
     * <p>日志对象采用：top.dddpeter.ishare.common.mongo.domain.CallCommonMongoLog</p>
     * <p>与MongoOperationUtil.saveCallResponse方法一同使用</p>
     *
     * @author : huangshuanbao
     * @date : 2020/2/27 3:50 下午
     * @param mongoDao mongodb collection的操作实现
     * @param request 请求报文
     * @param funcFlag 接口标识
     */
    public static String saveCallRequest(AbstractMongoDao mongoDao, String request, String funcFlag) {
        String uuid = "";
        try {
            CallCommonMongoLog callMongoLog = new CallCommonMongoLog();
            callMongoLog.setFuncFlag(funcFlag);
            callMongoLog.setTransBeginTime(DateUtil.dateToString(DateUtil.YYYY_MM_DD_HH_MM_SS));
            callMongoLog.setRequest(request);
            callMongoLog.setUuid(UUID.randomUUID().toString());
            mongoDao.insertOne(callMongoLog);
            uuid = callMongoLog.getUuid();
            log.info("mongodb存储请求报文信息成功!");
        } catch (Exception e) {
            //mongodb异常不影响后续操作
            log.error("mongodb存储请求报文信息异常, e={}", e.getMessage());
        }
        return uuid;
    }

    /**
     * 使用mongodb存储接口响应日志
     *
     * <p>日志对象采用top.dddpeter.ishare.common.mongo.domain.CallCommonMongoLog</p>
     * <p>依赖MongoOperationUtil.saveCallRequest方法的使用</p>
     *
     * @author : huangshuanbao
     * @date : 2020/2/27 3:50 下午
     * @param mongoDao mongodb collection的操作实现
     * @param response 响应报文
     * @param uuid saveCallRequest返回的uuid
     */
    public static void saveCallResponse(AbstractMongoDao mongoDao, String response, String uuid) {
        try {
            CallCommonMongoLog callMongoLog = new CallCommonMongoLog();
            callMongoLog.setResponse(response);
            callMongoLog.setTransEndTime(DateUtil.dateToString(DateUtil.YYYY_MM_DD_HH_MM_SS));
            mongoDao.updateOne(generateUpdateQuery("uuid", uuid), callMongoLog);
            log.debug("mongodb存储响应报文信息成功!");
        } catch (Exception e) {
            //mongodb异常不影响后续操作
            log.error("mongodb存储响应报文信息异常", e);
        }
    }

    /**
     * 构造mongodb更新操作更新条件
     *
     * @author : huangshuanbao
     * @date : 2020/2/27 3:50 下午
     * @param keyName 更新条件key的名字
     * @param keyValue 更新条件key的值
     */
    private static List<String> generateUpdateQuery(String keyName, String keyValue){
        List<String> query = new ArrayList<>();
        query.add("and");
        query.add(keyName);
        query.add("=");
        query.add(keyValue);
        return query;
    }

}
