package top.dddpeter.ishare.common.constant;

/**
 * 通用常量信息
 * 
 * @author ishare
 */
public final class Constants
{

    private Constants(){}

    /**
     * UTF-8 字符集
     */
    public static final String UTF8             = "UTF-8";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS          = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL             = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS    = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT           = "Logout";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL       = "Error";

    /**
     * 自动去除表前缀
     */
    public static final String AUTO_REOMVE_PRE  = "true";

    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM         = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE        = "pageSize";

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN  = "sortField";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC           = "sortOrder";

    /**
     * 排序串 排序列+排序的方向 "xxCloumn desc ,..." 或者 "xxCloumn asc ,...".
     */
    public static final String ORDER_BY         = "orderBy";

    public static final String CURRENT_ID       = "current_id";

    public static final String CURRENT_USERNAME = "current_username";

    public static final String TOKEN            = "token";

    public static final String DEFAULT_CODE_KEY = "random_code_";

    public static final String ACCESS_TOKEN     = "access_token_";

    public static final String ACCESS_USERID    = "access_userid_";

    public static final String USER_ID = "userId";

    public static final String LOGIN_NAME  = "loginName";

    public static final String RESOURCE_PREFIX  = "/profile";

    public static final  String APP_ID = "app_id";
    public static final  String APP_SECRET = "app_secret";

    /**
     * 通用出现异常标识
     */
    public static final String EXCEPTION = "exception";

    /**
     * 表记录逻辑删除状态 - Y
     */
    public static final String DB_IS_DELETED_Y = "Y";

    /**
     * 表记录逻辑删除状态 - N
     */
    public static final String DB_IS_DELETED_N = "N";

    public static final String NETWORK_UNSTABLE_COMMON_MSG = "网络不稳定，请稍后重试";

    public static final String ORDER_CENTER_SYSTEM_EXCEPTION = "订单中心系统异常";

    public static final String PAY_CENTER_SYSTEM_EXCEPTION = "支付中心系统异常";

    public static final String USER_CENTER_SYSTEM_EXCEPTION = "用户中心系统异常";

    public static final String INTELLIGENCE_CENTER_SYSTEM_EXCEPTION = "商务智能中心系统异常";

    /**
     * rdeis 微信签证
     */
    public static final String WECHAT = ":WECHAT:";
}
