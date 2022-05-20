package top.dddpeter.ishare.common.mongo.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用接口请求mongo日志定义
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/27 5:05 下午
 */
@Data
public class CallCommonMongoLog implements Serializable {

    private static final long serialVersionUID = -7729079984741485604L;

    /**
     * request和resonse里面塞同样的uuid
     */
    private String uuid;

    /**
     * 交易码
     */
    private String funcFlag;

    /**
     * 交易名
     */
    private String funcName;

    /**
     * 交易开始时间
     */
    private String transBeginTime;


    /**
     * 请求报文
     */
    private String request;

    /**
     * 响应报文
     */
    private String response;
    
    /**
     * 交易结束时间
     */
    private String transEndTime;

}

