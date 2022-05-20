package top.dddpeter.ishare.common.exception;

import top.dddpeter.ishare.common.enums.ResponseCodeEnum;

/**
 * ishare异常类
 * @author huangshuanbao
 * @date 2020-01-06
 */
public class IShareException extends RuntimeException
{
    private static final long serialVersionUID = 8980741666369083282L;

    private final String errorCode;

    private final String message;

    public IShareException(String message)
    {
        super(message);
        this.errorCode = ResponseCodeEnum.ERROR.getCode();
        this.message = message;
    }

    public IShareException(String message, Throwable e)
    {
        super(message, e);
        this.errorCode = ResponseCodeEnum.ERROR.getCode();
        this.message = message;
    }

    public IShareException(String errorCode, String message)
    {
        super(message);
        this.message = message;
        this.errorCode = errorCode;
    }

    public IShareException(String errorCode, String message, Throwable e)
    {
        super(message, e);
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}