package top.dddpeter.ishare.common.encryption.exception;

/**
 * 加解密异常类
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/20 11:02 上午
 */
public class EncryptedException extends RuntimeException {

    private static final long serialVersionUID = -6935979354607399217L;

    public EncryptedException() {}

    public EncryptedException(String message) {
        super(message);
    }

    public EncryptedException(String message, Throwable cause) {
        super(message, cause);
    }

    public EncryptedException(Throwable cause) {
        super(cause);
    }

    public EncryptedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
