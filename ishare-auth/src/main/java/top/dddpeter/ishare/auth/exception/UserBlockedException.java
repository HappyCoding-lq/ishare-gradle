package top.dddpeter.ishare.auth.exception;

/**
 * 用户锁定异常类
 * 
 * @author ishare
 */
public class UserBlockedException extends UserException
{
    private static final long serialVersionUID = 1L;

    public UserBlockedException()
    {
        super("user.blocked", null);
    }
}
