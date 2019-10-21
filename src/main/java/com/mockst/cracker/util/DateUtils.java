package com.mockst.cracker.util;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class DateUtils {

    public static final String DATETIME_PATTERN_19 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATETIME_PATTERN_14 = "yyyyMMddHHmmss";
    public final static String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public final static String FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public final static String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public final static String FORMAT_YYYY_MM_DD_HH_MM_SS_CHINESE = "yyyy年MM月dd日 HH时mm分ss秒";

    /**
     * 字符串转换为指定格式的日期
     */
    public static Date strDateToDateWithFormat(String strDate, String format) throws Exception {
        if (StringUtils.isNotBlank(strDate)) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date parseDate = sdf.parse(strDate);
            return parseDate;
        } else {
            return null;
        }
    }

    /**
     * 日期转换为指定格式的字符串
     */
    public static String dateToStrDateWithFormat(Date date, String format) {
        if (null != date) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            String strDate = sdf.format(date);
            return strDate;
        } else {
            return "";
        }
    }

    /**
     * 验证当前时间格式
     *
     * @param strTime 当前时间字符串
     * @return 验证后是否匹配
     */
    public static boolean verifyCurrentTimeFormat(String strTime, String DateFormat) {
        boolean convertSuccess = true;
        SimpleDateFormat format = new SimpleDateFormat(DateFormat);
        try {
            format.setLenient(false);
            format.parse(strTime);
        } catch (Exception e) {
            convertSuccess = false;
        }
        return convertSuccess;
    }

    /**
     * 取当前时间明天的日期 -----> 返回日期类型的日期
     */
    public static Date acquireTomorrowDate(Date today) {
        try {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(today);
            calendar.add(calendar.DATE, 1); // 把日期往后增加一天, 整数往后推, 负数往前移动
            return calendar.getTime();     // 这个日期就是日期往后推一天的结果
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前月的第一天
     * 以字符串的形式返回
     */
    public static String acquireFirstDayOfMonth() {
        SimpleDateFormat format = new SimpleDateFormat(DateUtils.FORMAT_YYYY_MM_DD);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1); // 设置为1号, 当前日期既为本月第一天
        String firstDay = format.format(calendar.getTime());
        return firstDay;
    }

    /**
     * 获取当前月的最后一天
     * 以字符串的形式返回
     */
    public static String acquireLastDayOfMonth() {
        SimpleDateFormat format = new SimpleDateFormat(DateUtils.FORMAT_YYYY_MM_DD);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String lastDay = format.format(calendar.getTime());
        return lastDay;
    }

    /**
     * 根据日期, 获取该日期所在的星期的所有日期
     * (1 - 周七, 2 - 周一, 3 - 周二, ·····, 7 - 周六)
     * <p>
     * 譬如: 给我 2016-04-12, 还你 2016-04-10、2016-04-11、2016-04-12、2016-04-13、2016-04-14、2016-04-15、2016-04-16
     */
    public static List<Date> acquireDatesByWeek(Date queryDate) {
        try {
            Integer weekDay = DateUtils.dateToWeekDay(queryDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(queryDate);

            List<Date> dateWeekList = new ArrayList<Date>();
            Date dateWeek = null;

            if (1 == weekDay) {
                calendar.add(Calendar.DAY_OF_YEAR, 0);
            } else {
                calendar.add(Calendar.DAY_OF_YEAR, -(weekDay - 1));
            }
            dateWeek = calendar.getTime();
            dateWeekList.add(dateWeek);

            for (int i = 0; i <= 5; i++) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                dateWeek = calendar.getTime();
                dateWeekList.add(dateWeek);
            }
            return dateWeekList;
        } catch (Exception e) {
            throw new RuntimeException("Paramter Exception!");
        }
    }

    /**
     * 将Date类型的日期转换为对应的周几数 -----> 2016-03-15 对应周二
     * (1 - 周七, 2 - 周一, 3 - 周二, ·····, 7 - 周六)
     */
    public static Integer dateToWeekDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (null != date) {
            calendar.setTime(date);
        }
        Integer weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        return weekDay;
    }

    /**
     * 两日期相减, 得到相差的分钟数
     */
    public static Integer dateSub(Date startTime, Date endTime) {
        Long subTime = endTime.getTime() - startTime.getTime();
        Long subMinutes = subTime / (1000 * 60); // 得到的是相差的毫秒数, 转为分钟数
        return subMinutes.intValue();
    }

    /**
     * 两日期相减, 得到相差的天数
     */
    public static Integer dateSubWithDay(Date startTime, Date endTime) {
        Long subTime = endTime.getTime() - startTime.getTime();
        Long subDays = subTime / (1000 * 60 * 60 * 24); // 得到的是相差的毫秒数, 转为天数
        return subDays.intValue();
    }


    /**
     * 两日期相减, 得到相差的年数(精确到天)
     */
    public static Integer dateSubWithYear(Date startTime, Date endTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        int startYear = calendar.get(Calendar.YEAR);
        int startMonth = calendar.get(Calendar.MONTH);
        int startDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(endTime);
        int endYear = calendar.get(Calendar.YEAR);
        int endMonth = calendar.get(Calendar.MONTH);
        int endDay = calendar.get(Calendar.DAY_OF_MONTH);

        if (endMonth - startMonth > 0) {
            return endYear - startYear;
        } else if (endMonth - startMonth < 0) {
            return endYear - startYear - 1;
        } else {
            if (endDay - startDay >= 0) {
                return endYear - startYear;
            } else {
                return endYear - startYear - 1;
            }
        }
    }

    /**
     * 将Date类型的"年 - 月 - 日"和字符串类型的"时:分钟:秒"拼接在一起, 精确到"年-月-日 时:分钟:秒"返回
     *
     * @param date         年-月-日 时:分钟:秒 -----> 2016-03-13
     * @param splicingTime 时:分钟:秒                  -----> 15:52
     * @return 年-月-日 时:分钟:秒 -----> 2016-03-13 15:52
     */
    public static Date splicingDayWithTime(Date date, String splicingTime) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat(DateUtils.FORMAT_YYYY_MM_DD);
        String ocDay = format.format(date);           // Sun Mar 13 00:00:00 CST 2016 -----> 2016-03-13
        StringBuilder stringBuilder = new StringBuilder();
        String preciseTime = stringBuilder.append(ocDay).append(" ").append(splicingTime).toString();

        format = new SimpleDateFormat(DateUtils.FORMAT_YYYY_MM_DD_HH_MM);
        Date splicingDay = format.parse(preciseTime); // 2016-03-13 15:52 -----> Sun Mar 13 15:52:00 CST 2016
        return splicingDay;
    }

    /**
     * 对Date类型的"年-月-日 时:分钟:秒"与整型的"分钟数"进行相加减(Sun Mar 13 15:52:00 CST 2016 + 30 = Sun Mar 13 16:22:00 CST 2016)
     *
     * @param date            年-月-日 时:分钟:秒 -----> 2016-03-13 15:52:00
     * @param splicingMinutes 分钟数                           -----> 30
     * @return 年-月-日 时:分钟:秒 -----> 2016-03-13 16:22:00
     */
    public static Date addSplicingDayCalculate(Date date, Integer splicingMinutes) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat(DateUtils.FORMAT_YYYY_MM_DD_HH_MM);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, splicingMinutes); // 日期  + splicingMinutes 分钟
        Date resultDate = calendar.getTime();
        return resultDate;
    }

    /**
     * 对Date类型的"年-月-日 时:分钟:秒"与整型的"分钟数"进行相加减(Sun Mar 13 15:52:00 CST 2016 - 30 = Sun Mar 13 14:22:00 CST 2016)
     *
     * @param date            年-月-日 时:分钟:秒
     * @param splicingMinutes 分钟数
     * @return 年-月-日 时:分钟:秒
     */
    public static Date subSplicingDayCalculate(Date date, Integer splicingMinutes) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("-").append(splicingMinutes);
        return DateUtils.addSplicingDayCalculate(date, Integer.parseInt(stringBuilder.toString()));
    }

    /**
     * 日期格式的字符串转为日期对象
     *
     * @param dateStr 日期格式的字符串
     * @return
     */
    public static Date str2Date(String dateStr) {
        return str2Date(dateStr, null);
    }

    /**
     * 日期格式的字符串转为日期对象
     *
     * @param dateStr 日期格式的字符串
     * @param pattern 格式化格式
     * @return
     */
    public static Date str2Date(String dateStr, String pattern) {
        if (pattern == null || "".equals(pattern)) {
            pattern = DATETIME_PATTERN_19;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, dateTimeFormatter);
        Date date = localDateTime2Date(localDateTime);
        return date;

    }

    /**
     * 日期转化为字符串
     *
     * @param date 日期对象
     * @return
     */
    public static String date2Str(Date date) {
        return date2Str(date, null);
    }

    /**
     * 日期转化为字符串
     *
     * @param date    日期对象
     * @param pattern 格式化格式
     * @return
     */
    public static String date2Str(Date date, String pattern) {
        if (pattern == null || "".equals(pattern)) {
            pattern = DATETIME_PATTERN_19;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        String result = dateTimeFormatter.format(date2LocalDateTime(date));

        return result;
    }

    /**
     * 日期转化为本地时间对象
     *
     * @param date
     * @return
     */
    public static LocalDateTime date2LocalDateTime(Date date) {
        Instant instant = date.toInstant();//An instantaneous point on the time-line.(时间线上的一个瞬时点。)
        ZoneId zoneId = ZoneId.systemDefault();//A time-zone ID, such as {@code Europe/Paris}.(时区)
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();

        return localDateTime;
    }

    /**
     * 本地时间转化为日期对象
     *
     * @param localDateTime
     * @return
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);

        Date date = Date.from(zdt.toInstant());

        return date;

    }

    public static String toString(Date date, String pattern) {
        if (date == null) {
            return "";
        } else {
            if (pattern == null) {
                pattern = "yyyy-MM-dd";
            }

            String dateString = "";
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);

            try {
                dateString = sdf.format(date);
            } catch (Exception var5) {
                var5.printStackTrace();
            }

            return dateString;
        }
    }

    public static void main(String[] args) {
        String x1 = "2019-04-22 10:02";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date d1 = sdf.parse(x1);
            Integer integer = dateSubWithDay(d1, new Date());
            System.out.println(integer);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
