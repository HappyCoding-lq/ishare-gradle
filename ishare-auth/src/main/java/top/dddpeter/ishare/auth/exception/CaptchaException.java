package top.dddpeter.ishare.auth.exception;

/**
 * 验证码错误异常类
 * 
 * @author ishare
 */
public class CaptchaException extends UserException
{
    private static final long serialVersionUID = 1L;

    public CaptchaException()
    {
        super("user.jcaptcha.error", null);
    }
}
