package top.dddpeter.ishare.auth.exception;

/**
 * 角色锁定异常类
 * 
 * @author ishare
 */
public class RoleBlockedException extends UserException
{
    private static final long serialVersionUID = 1L;

    public RoleBlockedException()
    {
        super("role.blocked", null);
    }
}
