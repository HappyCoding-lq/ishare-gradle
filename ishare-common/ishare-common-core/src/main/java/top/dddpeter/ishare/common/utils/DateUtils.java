package top.dddpeter.ishare.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

/**
 * 时间工具类
 *
 * @author ishare
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static final  String yyyy = "yyyy";

    public static final String yyyy_MM = "yyyy-MM";

    public static final String yyyy_MM_dd = "yyyy-MM-dd";

    public static final String HH_mm_ss = "HH:mm:ss";

    public static final String yyyyMMdd = "yyyyMMdd";

    public static final String HHmmss = "HHmmss";

    public static final String yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";

    public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";

    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    protected static final String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(yyyy_MM_dd);
    }

    public static final String getTime() {
        return dateTimeNow(yyyy_MM_dd_HH_mm_ss);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(yyyyMMddHHmmss);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(yyyy_MM_dd, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(String text, String... parsePatterns) throws ParseException {
        if (text == null || text.length()<=0) {
            return null;
        }
        return org.apache.commons.lang3.time.DateUtils.parseDate(text, parsePatterns);
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    // 获得某天最大时间 2017-10-15 23:59:59
    public static Date getEndOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    // 获得某天最小时间 2017-10-15 00:00:00
    public static Date getStartOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }
    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000L * 24 * 60 * 60;
        long nh = 1000L * 60 * 60;
        long nm = 1000L * 60;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 计算两个时间差多少天
     */
    public static long getDateDifferenceDay(Date endDate, Date nowDate) {
        long nd = 1000L * 24 * 60 * 60;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        return diff / nd;
    }

    /**
     * dayStart
     *
     * @param date date
     * @return Date
     */
    public static Date dayStart(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * dayEnd
     *
     * @param date date
     * @return Date
     */
    public static Date dayEnd(Date date) {
        final int num23 = 23;
        final int num59 = 59;
        final int num999 = 999;
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, num23);
        calendar.set(Calendar.MINUTE, num59);
        calendar.set(Calendar.SECOND, num59);
        calendar.set(Calendar.MILLISECOND, num999);
        return calendar.getTime();
    }

    /**
     * 对日期(时间)中的日进行加减计算. <br>
     * 例子: <br>
     * 如果Date类型的d为 2005年8月20日,那么 <br>
     * calculateByDate(d,-10)的值为2005年8月10日 <br>
     * 而calculateByDate(d,+10)的值为2005年8月30日 <br>
     *
     * @param d      日期(时间).
     * @param amount 加减计算的幅度.+n=加n天;-n=减n天.
     * @return 计算后的日期(时间).
     */
    public static Date calculateByDate(Date d, int amount) {
        return calculate(d, GregorianCalendar.DATE, amount);
    }

    /**
     * <p>
     * Description: 对日期(分钟)中的日进行加减计算.
     * </p>
     *
     * @param d      日期(时间).
     * @param amount 加减计算的幅度
     * @return 计算后的日期(时间)
     */
    public static Date calculateByMinute(Date d, int amount) {
        return calculate(d, GregorianCalendar.MINUTE, amount);
    }

    /**
     * <p>
     * Description: 对日期(年)中的日进行加减计算.
     * </p>
     *
     * @param d      日期(时间).
     * @param amount 加减计算的幅度
     * @return 计算后的日期(时间)
     */
    public static Date calculateByYear(Date d, int amount) {
        return calculate(d, GregorianCalendar.YEAR, amount);
    }

    /**
     * 对日期(时间)中由field参数指定的日期成员进行加减计算. <br>
     * 例子: <br>
     * 如果Date类型的d为 2005年8月20日,那么 <br>
     * calculate(d,GregorianCalendar.YEAR,-10)的值为1995年8月20日 <br>
     * 而calculate(d,GregorianCalendar.YEAR,+10)的值为2015年8月20日 <br>
     *
     * @param d      日期(时间).
     * @param field  日期成员. <br>
     *               日期成员主要有: <br>
     *               年:GregorianCalendar.YEAR <br>
     *               月:GregorianCalendar.MONTH <br>
     *               日:GregorianCalendar.DATE <br>
     *               时:GregorianCalendar.HOUR <br>
     *               分:GregorianCalendar.MINUTE <br>
     *               秒:GregorianCalendar.SECOND <br>
     *               毫秒:GregorianCalendar.MILLISECOND <br>
     * @param amount 加减计算的幅度.+n=加n个由参数field指定的日期成员值;-n=减n个由参数field代表的日期成员值.
     * @return 计算后的日期(时间).
     */
    private static Date calculate(Date d, int field, int amount) {
        if (d == null) {
            return null;
        }
        GregorianCalendar g = new GregorianCalendar();
        g.setGregorianChange(d);
        g.add(field, amount);
        return g.getTime();
    }

    /**
     * 日期(时间)转化为字符串.
     *
     * @param formater 日期或时间的格式.
     * @param aDate    java.util.Date类的实例.
     * @return 日期转化后的字符串.
     */
    public static String dateToString(String formater, Date aDate) {
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
    public static String dateToString(String formater) {
        return dateToString(formater, new Date());
    }

    /**
     * 获取当前日期对应的星期数. <br>
     * 1=星期天,2=星期一,3=星期二,4=星期三,5=星期四,6=星期五,7=星期六
     *
     * @return 当前日期对应的星期数
     */
    public static int dayOfWeek() {
        GregorianCalendar g = new GregorianCalendar();
        return g.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取所有的时区编号. <br>
     * 排序规则:按照ASCII字符的正序进行排序. <br>
     * 排序时候忽略字符大小写.
     *
     * @return 所有的时区编号(时区编号已经按照字符[忽略大小写]排序).
     */
    public static List<String> fecthAllTimeZoneIds() {
        List<String> v = new ArrayList<>();
        String[] ids = TimeZone.getAvailableIDs();
        Arrays.stream(ids).forEach(id -> { //NOSONAR
            v.add(id);
        });
        Collections.sort(v, String.CASE_INSENSITIVE_ORDER);
        return v;
    }

    /**
     * 将日期时间字符串根据转换为指定时区的日期时间.
     *
     * @param srcFormater   待转化的日期时间的格式.
     * @param srcDateTime   待转化的日期时间.
     * @param dstFormater   目标的日期时间的格式.
     * @param dstTimeZoneId 目标的时区编号.
     * @return 转化后的日期时间.
     * @throws ParseException 异常
     */
    public static String stringToTimezone(String srcFormater, String srcDateTime, String dstFormater, String dstTimeZoneId) throws ParseException { //NOSONAR
        if (srcFormater == null || "".equals(srcFormater)) {
            return null;
        }
        if (srcDateTime == null || "".equals(srcDateTime)) {
            return null;
        }
        if (dstFormater == null || "".equals(dstFormater)) {
            return null;
        }
        if (dstTimeZoneId == null || "".equals(dstTimeZoneId)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(srcFormater);
        int diffTime = getDiffTimeZoneRawOffset(dstTimeZoneId);
        Date d = sdf.parse(srcDateTime);
        long nowTime = d.getTime();
        long newNowTime = nowTime - diffTime;
        d = new Date(newNowTime);
        return dateToString(dstFormater, d);
    }

    /**
     * 获取系统当前默认时区与UTC的时间差.(单位:毫秒)
     *
     * @return 系统当前默认时区与UTC的时间差.(单 = 毫秒)
     */
    public static int getDefaultTimeZoneRawOffset() {
        return TimeZone.getDefault().getRawOffset();
    }

    /**
     * <p>
     * Description: 返回UTC与指定时区的时间差
     * </p>
     *
     * @param timeZoneId 时区ID
     * @return 时差
     */
    public static String getUtcTimeZoneRawOffset(String timeZoneId) {
        return new SimpleDateFormat("HH:mm").format(getTimeZoneRawOffset(timeZoneId));
    }

    /**
     * 获取指定时区与UTC的时间差.(单位:毫秒)
     *
     * @param timeZoneId 时区Id
     * @return 指定时区与UTC的时间差.(单位 = 毫秒)
     */
    public static int getTimeZoneRawOffset(String timeZoneId) {
        return TimeZone.getTimeZone(timeZoneId).getRawOffset();
    }

    /**
     * 获取系统当前默认时区与指定时区的时间差.(单位:毫秒)
     *
     * @param timeZoneId 时区Id
     * @return 系统当前默认时区与指定时区的时间差.(单位 = 毫秒)
     */
    private static int getDiffTimeZoneRawOffset(String timeZoneId) {
        return TimeZone.getDefault().getRawOffset() - TimeZone.getTimeZone(timeZoneId).getRawOffset();
    }

    /**
     * 将日期时间字符串根据转换为指定时区的日期时间.
     *
     * @param srcDateTime   待转化的日期时间.
     * @param dstTimeZoneId 目标的时区编号.
     * @return 转化后的日期时间.
     * @throws ParseException 异常
     */
    public static String stringToTimezoneDefault(String srcDateTime, String dstTimeZoneId) throws ParseException {
        return stringToTimezone("yyyy-MM-dd HH:mm:ss", srcDateTime, "yyyy-MM-dd HH:mm:ss", dstTimeZoneId);
    }

    /**
     * 判断传入日期是否为休息日（周六日）
     * @author: wangmaolin
     * @date: 2020/6/5 11:08
     */
    public static boolean checkDayOff(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return w == 6 || w == 0  ;
    }
}
