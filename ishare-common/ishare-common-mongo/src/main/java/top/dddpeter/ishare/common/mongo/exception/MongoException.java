package top.dddpeter.ishare.common.mongo.exception;

/**
 * mongo操作通用异常类
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/28 2:23 上午
 */
public class MongoException extends Exception{

    private static final long serialVersionUID = -1150483905614684983L;

    public MongoException(String message) {
        super(message);
    }

    public MongoException(String message, Throwable cause) {
        super(message, cause);
    }

    public MongoException(Throwable cause) {
        super(cause);
    }

    public MongoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
