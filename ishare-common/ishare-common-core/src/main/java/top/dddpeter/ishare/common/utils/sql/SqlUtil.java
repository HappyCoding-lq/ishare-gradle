package top.dddpeter.ishare.common.utils.sql;

import org.apache.commons.collections.CollectionUtils;
import top.dddpeter.ishare.common.utils.StringUtils;

import java.util.List;

/**
 * sql操作工具类
 *
 * @author ishare
 */
public class SqlUtil {
    /**
     * 仅支持字母、数字、下划线、空格、逗号（支持多个字段排序）
     */
    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,]+";

    /**
     * 检查字符，防止注入绕过
     */
    public static String escapeOrderBySql(String value) {
        if (StringUtils.isNotEmpty(value) && !isValidOrderBySql(value)) {
            return StringUtils.EMPTY;
        }
        return value;
    }

    /**
     * 验证 order by 语法是否符合规范
     */
    public static boolean isValidOrderBySql(String value) {
        return value.matches(SQL_PATTERN);
    }

    /**
     * 将逗号分隔的串转换成in语句的字符串 比如 1,2,3 转换成 '1','2','3'
     */
    public static String toInParamByJoinStr(String str) {
        if (str == null || "".equals(str)) {
            return "('')";
        }
        return "'" + str.replace(",", "','") + "'";
    }

    /**
     * 按照IDs 查询的列表转换
     * @param elements ID列表
     * @return 字符
     */
    public static String toInParamByIdList(List<Long> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            return "('')";
        }
        return StringUtils.join(elements).replace("[", "").replace("]", "");
    }
}
