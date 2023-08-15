package zhishusz.housepresell.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cn.hutool.core.util.StrUtil;

public class MyDatetime {
    private static MyDatetime instance;

    public MyDatetime() {

    }

    public static MyDatetime getInstance() {
        if (instance == null)
            instance = new MyDatetime();

        return instance;
    }

    public Date parse(Object object) {
        if (object == null)
            return null;
        else if (object instanceof String[]) {
            String[] objArr = (String[])object;
            if (objArr.length > 0)
                return parse(objArr[0]);
            else
                return null;
        } else
            return parse(object.toString());
    }

    public Date parse(String strDatetime)// 20160920000000
    {
        if (strDatetime == null) {
            return null;
        } else if (strDatetime.length() == 10) // 判断strDatetime的长度是否等于10，只有年月日，必须补足时分秒
        {
            strDatetime += " 00:00:00";
        } else if (strDatetime.length() != 19) // 判断strDatetime的长度是否不等于19，如果不是，则数据格式非法
        {
            return null;
        }

        Date datetime = null;
        try {
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            datetime = formatDate.parse(strDatetime);
        } catch (ParseException e) {
            datetime = null;
        }
        return datetime;
    }

    public long dateToMs(Date datetime) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String oldDatetime = formatDate.format(datetime);
        Date newDatetime = parse(oldDatetime);
        long ms = newDatetime.getTime();
        return ms;
    }

    public Integer dateToSimple(Date datetime) {
        if (datetime == null)
            return null;
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");
        String oldDatetime = formatDate.format(datetime);
        return MyInteger.getInstance().parse(oldDatetime);
    }

    public String dateToSimpleString(Date datetime) {
        if (datetime == null)
            return null;
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        String oldDatetime = formatDate.format(datetime);
        return MyString.getInstance().parse(oldDatetime);
    }

    public String currentTime() {
        Long datetimeStamp = System.currentTimeMillis();
        Date datetime = new Date(datetimeStamp);
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmss");
        String oldDatetime = formatDate.format(datetime);
        return MyString.getInstance().parse(oldDatetime);
    }

    public String dateToSimpleString(Long datetimeStamp) {
        if (datetimeStamp == null)
            return null;
        Date datetime = new Date(datetimeStamp);
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        String oldDatetime = formatDate.format(datetime);
        return MyString.getInstance().parse(oldDatetime);
    }

    public String dateToString(Long datetimeStamp) {
        return dateToString(datetimeStamp, "yyyy-MM-dd");
    }

    public String dateToString2(Long datetimeStamp) {
        return dateToString(datetimeStamp, "yyyy-MM-dd HH:mm:ss");
    }

    public String dateToStringMinute(Long datetimeStamp) {
        return dateToString(datetimeStamp, "yyyy-MM-dd HH:mm");
    }

    public String dateToString(Long datetimeStamp, String format) {
        if (datetimeStamp == null)
            return null;
        return dateToString(new Date(datetimeStamp), format);
    }

    public String dateToString(Date datetime) {
        return dateToString(datetime, "yyyy-MM-dd HH:mm:ss");
    }

    public String dateToString(Date datetime, String format) {
        if (datetime == null)
            return null;
        SimpleDateFormat formatDate = new SimpleDateFormat(format);
        String oldDatetime = formatDate.format(datetime);
        return MyString.getInstance().parse(oldDatetime);
    }

    public Date simpleToDate(Integer simpleDate) {
        String str = MyString.getInstance().parse(simpleDate);
        String date = str.substring(0, 4) + "-" + str.substring(4, 6) + "-" + str.substring(6, 8);
        return parse(date);
    }

    // 获取今天凌晨0点整的时间
    @SuppressWarnings("deprecation")
    public Date getThisDay0Datetime() {
        Date today = new Date();
        today.setHours(0);
        today.setMinutes(0);
        today.setSeconds(0);

        return today;
    }

    @SuppressWarnings("deprecation")
    public Date getDay0Datetime(Date date) {
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);

        return date;
    }

    @SuppressWarnings("deprecation")
    public Date getDay24Datetime(Date date) {
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);

        return date;
    }

    public Date getDate(String str) {
        if (str == null || (str.length() != 8 && str.length() != 14))
            return null;
        str = str.trim();
        Calendar calendar = Calendar.getInstance();

        Integer theYear = MyInteger.getInstance().parse(str.substring(0, 4));
        Integer theMonth = MyInteger.getInstance().parse(str.substring(4, 6));
        Integer theDay = MyInteger.getInstance().parse(str.substring(6, 8));

        Integer theHour = 0;
        Integer theMinute = 0;
        Integer theSecond = 0;

        if (str.length() > 8) {
            theHour = MyInteger.getInstance().parse(str.substring(8, 10));
            theMinute = MyInteger.getInstance().parse(str.substring(10, 12));
            theSecond = MyInteger.getInstance().parse(str.substring(12, 14));
        }

        calendar.set(Calendar.YEAR, theYear);
        calendar.set(Calendar.MONTH, theMonth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, theDay);
        calendar.set(Calendar.HOUR_OF_DAY, theHour);
        calendar.set(Calendar.MINUTE, theMinute);
        calendar.set(Calendar.SECOND, theSecond);

        return calendar.getTime();
    }

    // 获取本周第一天日凌晨00:00:00点整的时间
    public Date getThisWeekDay0Datetime() {
        Calendar cal = Calendar.getInstance();

        int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        cal.add(Calendar.DATE, -day_of_week + 1);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        return cal.getTime();
    }

    // 获取本月最后一天日凌晨23:59:59点整的时间
    public Date getThisMonthDayEndDatetime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MINUTE, 59);

        return calendar.getTime();
    }

    // 获取本月一日凌晨0点整的时间
    @SuppressWarnings("deprecation")
    public Date getThisMonthDay0Datetime() {
        Date thisMonth = new Date();
        thisMonth.setDate(1);
        thisMonth.setHours(0);
        thisMonth.setMinutes(0);
        thisMonth.setSeconds(0);

        return thisMonth;
    }

    public Integer getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public Integer getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    public char getCurrentYearChar() {
        Calendar calendar = Calendar.getInstance();
        Integer year = calendar.get(Calendar.YEAR);

        return (char)(year - (2016 - (int)'A'));
    }

    public String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        return dateFormat.format(new Date());
    }

    public String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }

    // 计算两个日期相隔天数
    public Integer getIntervalDays(Date startday, Date endday) {
        Calendar startTime = new GregorianCalendar();
        startTime.setTime(startday);
        Calendar endTime = new GregorianCalendar();
        endTime.setTime(endday);
        // 确保startday在endday之前
        if (startTime.after(endTime)) {
            Calendar cal = startTime;
            startTime = endTime;
            endTime = cal;
        }
        // 分别得到两个时间的毫秒数
        long sl = startTime.getTimeInMillis();
        long el = endTime.getTimeInMillis();

        long ei = el - sl;
        // 根据毫秒数计算间隔天数
        return MyInteger.getInstance().parse((ei / (1000 * 60 * 60 * 24)));
    }

    // 两个时间相差N天M小时
    public String getInterval(Date startime, Date endtime) {
        String str = "";
        if (startime == null || endtime == null) {
            return "";
        } else {
            long start = startime.getTime();
            long end = endtime.getTime();
            long diff = end - start;
            double millisDay = 1000 * 24 * 60 * 60;// 一天的毫秒数
            double millisHour = 1000 * 60 * 60;// 一小时的毫秒数
            int day = (int)(diff / millisDay);// 计算差多少天
            int hour = (int)Math.ceil(diff % millisDay / millisHour);// 计算差多少小时
            str = day + "天" + hour + "小时";
        }

        return str;
    }

    // 获取上个月的最后一个星期天
    public Date getLastMonthOfLastSunday(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (year != null && year > 0) {
            calendar.set(Calendar.YEAR, year);
        }
        if (month != null && month > 0) {
            calendar.set(Calendar.MONTH, month - 1);
        }
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.DAY_OF_WEEK, 1);

        return calendar.getTime();
    }

    // 获取下个月的第一个星期六
    public Date getNextMonthOfFirstSaturday(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (year != null && year > 0) {
            calendar.set(Calendar.YEAR, year);
        }
        if (month != null && month > 0) {
            calendar.set(Calendar.MONTH, month);
        } else {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        }
        calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
        calendar.set(Calendar.DAY_OF_WEEK, 7);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MINUTE, 59);

        return calendar.getTime();
    }

    /**
     * String类型日期转换成毫秒数
     * 
     * @param date
     *            毫秒数
     * @return
     */
    public Long stringToLong(String date) {
        return stringToLong(date, "yyyy-MM-dd");

    }

    public Long stringMinuteToLong(String date) {
        return stringToLong(date, "yyyy-MM-dd HH:mm");
    }

    public Long stringDayToLong(String date) {
        return stringToLong(date, "yyyy-MM-dd");
    }

    /**
     * String类型日期转换成毫秒数
     * 
     * @param date
     *            毫秒数
     * @param format
     * @return
     */
    public Long stringToLong(String date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            Date parse = simpleDateFormat.parse(date);
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1L;

    }

    public String stringDateFormat(String datetime) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");
        Date d = null;
        try {
            d = formatDate.parse(datetime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SimpleDateFormat formatDate2 = new SimpleDateFormat("yyyy-MM-dd");
        return formatDate2.format(d);
    }

    public String stringDateFormat2(String datetime) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = null;
        try {
            d = formatDate.parse(datetime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SimpleDateFormat formatDate2 = new SimpleDateFormat("yyyy-MM-dd");
        return formatDate2.format(d);
    }

    public String stringDateFormat3(String datetime) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date d = null;
        try {
            d = formatDate.parse(datetime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SimpleDateFormat formatDate2 = new SimpleDateFormat("yyyy-MM-dd");
        return formatDate2.format(d);
    }

    public String stringDateFormat4(String datetime) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d = null;
        try {
            d = formatDate.parse(datetime);
        } catch (ParseException e) {
            return "false";
        }

        SimpleDateFormat formatDate2 = new SimpleDateFormat("yyyy-MM-dd");
        return formatDate2.format(d);
    }

    public static String getSpecifiedDayBefore(String specifiedDay) {

        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayBefore;
    }

    public static String getSpecifiedDayAfter(String specifiedDay) {

        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayBefore;
    }

    /**
     * 获取日期前1年
     * 
     * @param specifiedDay
     *            当前时间
     * @return
     */
    public String getSpecifiedYearBefore(String specifiedDay) {

        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        c.set(Calendar.YEAR, year - 1);

        String yearBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return yearBefore;
    }

    /**
     * 获取日期前1月
     * 
     * @param specifiedDay
     *            当前时间
     * @return
     */
    public String getSpecifiedMonthBefore(String specifiedDay) {

        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        c.set(Calendar.MONTH, month - 1);

        String monthBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return monthBefore;
    }

    // time："00:00:00"、"23:59:59"
    public Long parseTimestamp(String strDatetime, String time) {
        Date date = parse(strDatetime + " " + time);
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }

    public Long parseTimestampMin(String strDatetime) {
        return parseTimestamp(strDatetime, "00:00:00");
    }

    public Long parseTimestampMax(String strDatetime) {
        return parseTimestamp(strDatetime, "23:59:59");
    }

    public Long getDateTimeStampMin(String rangeTimeStampStr) {
        if (rangeTimeStampStr == null || rangeTimeStampStr.trim().length() == 0)
            return null;

        String[] timeArr = rangeTimeStampStr.split(" - ");
        return parseTimestampMin(timeArr[0]);
    }

    public Long getDateTimeStampMax(String rangeTimeStampStr) {
        if (rangeTimeStampStr == null || rangeTimeStampStr.trim().length() == 0)
            return null;

        String[] timeArr = rangeTimeStampStr.split(" - ");
        return parseTimestampMax(timeArr[1]);
    }

    public Long[] getBeforeMarch(String nowTimeStr) {
        MyInteger myInteger = MyInteger.getInstance();

        if (nowTimeStr == null || nowTimeStr.trim().length() == 0)
            return null;
        Calendar calendarEnd = Calendar.getInstance();
        Calendar calendarStart = Calendar.getInstance();
        String[] timeArr = nowTimeStr.split("-");
        Integer theYear = myInteger.parse(timeArr[0]);
        Integer theMonth = myInteger.parse(timeArr[1]);
        calendarEnd.set(Calendar.YEAR, theYear);
        calendarEnd.set(Calendar.MONTH, theMonth - 1);
        calendarEnd.set(Calendar.DAY_OF_MONTH, 1);
        calendarEnd.set(Calendar.HOUR_OF_DAY, 0);
        calendarEnd.set(Calendar.MINUTE, 0);
        calendarEnd.set(Calendar.SECOND, 0);
        calendarEnd.set(Calendar.MILLISECOND, 0);

        calendarStart.set(Calendar.YEAR, theYear);
        calendarStart.set(Calendar.MONTH, theMonth - 4);
        calendarStart.set(Calendar.DAY_OF_MONTH, 1);
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);
        calendarStart.set(Calendar.MILLISECOND, 0);
        Long endDateStamp = calendarEnd.getTimeInMillis();
        Long startDateStamp = calendarStart.getTimeInMillis();

        Long[] rangeTimeStamp = new Long[2];
        rangeTimeStamp[0] = startDateStamp;
        rangeTimeStamp[1] = endDateStamp;

        return rangeTimeStamp;
    }

    public long theDayBeforeSpecifiedDayTimeStamp(long specifiedDayTimeStamp) {
        String dayTimeString = dateToSimpleString(specifiedDayTimeStamp);
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dayTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day1 = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day1 - 1);
        return c.getTime().getTime();
    }

    public long theDayAfterSpecifiedDayTimeStamp(long specifiedDayTimeStamp) {
        String dayTimeString = dateToSimpleString(specifiedDayTimeStamp);
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dayTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day1 = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day1 + 1);
        return c.getTime().getTime();
    }

    public String theDayBeforeSpecifiedDayString(long specifiedDayTimeStamp) {
        return dateToSimpleString(theDayBeforeSpecifiedDayTimeStamp(specifiedDayTimeStamp));
    }

    /**
     * 日期转星期
     *
     * @param datetime
     * @return
     */
    public static Integer dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Integer[] weekDays = {0, 1, 2, 3, 4, 5, 6};
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public int getHour(Long time) {
        if (time == null) {
            return -1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public int getNowHour() {
        return getHour(System.currentTimeMillis());
    }

    public long getDayTimeStamp(Long time) {
        if (time == null) {
            return -1;
        }
        String dayString = dateToSimpleString(time);
        Long dayTimeStamp = stringDayToLong(dayString);
        return dayTimeStamp;
    }

    public long getDayEndTimeStamp(Long time) {
        long dayTimeStamp = getDayTimeStamp(time);
        return dayTimeStamp + getOneDayMs() - 1000;
    }

    public long calculateHours(int hour) {
        return 1000 * 60 * 60 * hour;
    }

    public long getOneDayMs() {
        return calculateHours(24);
    }

    public long getTwoDayMs() {
        return calculateHours(48);
    }

    /**
     * 当前时间推移天数
     * 
     * @param nowDate
     * @param addDate
     * @return
     */
    public String getDateAddDate(String nowDate, Integer addDate) {
        
        if(StrUtil.isBlank(nowDate)){
            nowDate = getCurrentDate();
        }
        
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(getDate(nowDate.replace("-", "")));
        calendar.add(calendar.DATE, addDate);
        Date time = calendar.getTime();
        return dateToSimpleString(time);
    }


    public static void main(String[] args) {
        System.out.println(MyDatetime.getInstance().stringDateFormat2("2023-03-21 09:06:20"));
    }

}
