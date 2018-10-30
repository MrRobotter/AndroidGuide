package com.joinyon.androidguide.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * 判断字符串是否为空值
     *
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {

        boolean flag = false;
        if (str == null) {
            flag = true;
        }

        if (str.equals("")) {
            flag = true;
        }

        return flag;
    }

    public static BigDecimal transAmount(String amount) {
        if (isNullOrEmpty(amount)) {
            return new BigDecimal("0");
        } else {
            String str = amount.substring(amount.length() - 12, amount.length());

            BigDecimal b = new BigDecimal(str).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
            return b;
        }

    }


    private static final SimpleDateFormat simpleDateFormatYYYYMMdd = new SimpleDateFormat("yyyy-MM-dd");

    private static final SimpleDateFormat simpleDateFormatYYYYMMddHHmm = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static final SimpleDateFormat getSimpleDateFormatYYYYMMddHHmm = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static final SimpleDateFormat getSimpleDateFormatMMddHHmm = new SimpleDateFormat("MM-dd HH:mm");

    private static final SimpleDateFormat getSimpleDateFormatMMddHHmmss = new SimpleDateFormat("MM-dd HH:mm:ss");

    private static final SimpleDateFormat getSimpleDateFormatYYYYMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final SimpleDateFormat getSimpleDateFormatHHmm = new SimpleDateFormat("HH:mm:ss");


    public static String getTwoNumberFormatText(double number) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String numberText = Math.round(number) - number == 0 ?
                String.valueOf((long) number) : decimalFormat.format(number);
        return numberText;
    }

    public static String getTenThousandFormatText(long number) {
        return getTwoNumberFormatText(number / 10000.0) + "万";
    }

    public static String getPercentText(double percent) {
        return getTwoNumberFormatText(percent) + "%";
    }

    public static String getPercentText(double sum, double part) {
        return getPercentText(part / sum);
    }

    public static boolean isPhoneNumber(String text) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(text);

        return m.matches();
    }

    /**
     * 获取日期 例如 2016-01-01
     *
     * @param times
     * @return
     */
    public static String getSimpleDate(long times) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(times);
        return simpleDateFormatYYYYMMdd.format(calendar.getTime());
    }

    public static int getCurrYear() {
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.YEAR);
    }

    public static int getCurrMonth() {
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getCurrDay() {
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getHour() {
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMin() {
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 获取日期 例如 2016-01-01 12:00
     *
     * @param times
     * @return
     */
    public static String getSimpleTime(long times) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(times);
        return simpleDateFormatYYYYMMddHHmm.format(calendar.getTime());
    }

    public static String getTimeHMS(long times) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(times);
        return getSimpleDateFormatHHmm.format(calendar.getTime());
    }

    public static String getTimeWithoutYear(long times) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(times);
        return getSimpleDateFormatMMddHHmm.format(calendar.getTime());
    }

    public static String getTimeWithSecond(long times) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(times);
        return getSimpleDateFormatMMddHHmmss.format(calendar.getTime());
    }

    public static String getTimeWithYear(long times) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(times);
        return getSimpleDateFormatYYYYMMddHHmm.format(calendar.getTime());
    }

    public static String getTimeWithYearAndSecond(long times) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(times);
        return getSimpleDateFormatYYYYMMddHHmmss.format(calendar.getTime());
    }


    public static String getTimeAutoYear(long times) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(times);
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(Calendar.YEAR) == now.get(Calendar.YEAR) ? getTimeWithSecond(times)
                : getTimeWithYearAndSecond(times);
    }

    public static boolean isIdCardNumber(String text) {
        Pattern p1 = Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{2}\\w$");
        Matcher m1 = p1.matcher(text);

        Pattern p2 = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}\\w$");
        Matcher m2 = p2.matcher(text);
        return m2.matches() || m1.matches();
    }

    public static String getCurrentDay() {
        Calendar now = Calendar.getInstance();
        return simpleDateFormatYYYYMMdd.format(now.getTime());
    }

    public static String convertTimeToFormat(long timeStamp) {
        long curTime = System.currentTimeMillis() / (long) 1000;
        long pastTime = timeStamp / (long) 1000;
        long time = curTime - pastTime;
        if (time < 60 && time >= 0) {
            return "刚刚";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else if (time >= 3600 * 24) {
            return getSimpleDate(timeStamp);
        } else {
            return "刚刚";
        }
    }

    public static String getUiWeek(String s) {
        String[] weeks = s.split(",");

        StringBuilder stringBuilder = new StringBuilder();
        for (String week : weeks) {
            // Log.e(">>>>", week);
            stringBuilder.append(getWeek(week));
        }
        return stringBuilder.toString();
    }

    public static String getWeek(String s) {
        switch (s) {
            case "0":
                return "周日";

            case "1":
                return "周一";

            case "2":
                return "周二";

            case "3":
                return "周三";

            case "4":
                return "周四";

            case "5":
                return "周五";

            case "6":
                return "周六";

            default:
                return "";
        }
    }

    public static double toFixed(double number, int index) {
        double result = 0;
        double temp = Math.pow(10, index);
        result = Math.round(number * temp) / temp;
        return result;
    }
}
