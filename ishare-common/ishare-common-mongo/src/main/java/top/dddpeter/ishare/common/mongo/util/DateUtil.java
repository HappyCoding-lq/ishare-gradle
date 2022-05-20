package top.dddpeter.ishare.common.mongo.util;

import java.text.SimpleDateFormat;
import java.util.Date;


class DateUtil {

    private DateUtil() {}

    static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * 日期(时间)转化为字符串.
     *
     * @param formater 日期或时间的格式.
     * @param aDate    java.util.Date类的实例.
     * @return 日期转化后的字符串.
     */
    static String dateToString(String formater, Date aDate) {
        if (formater == null || "".equals(formater)) {
            return null;
        }
        if (aDate == null) {
            return null;
        }
        return (new SimpleDateFormat(formater)).format(aDate);
    }

    /**
     * 当前日期(时间)转化为字符串.
     *
     * @param formater 日期或时间的格式.
     * @return 日期转化后的字符串.
     */
    static String dateToString(String formater) {
        return dateToString(formater, new Date());
    }

}
