package com.privatecar.privatecar.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Shamyyoun on 2/22/2015.
 */
public class DateUtil {
    public static Calendar convertToCalendar(String strDate, String strFormat) {
        Calendar calendar = Calendar.getInstance();
        try {
            final DateFormat df = new SimpleDateFormat(strFormat);
            calendar.setTime(df.parse(strDate));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return calendar;
    }

    public static String convertToString(Calendar calendar, String strFormat) {
        String strDate;
        try {
            SimpleDateFormat format = new SimpleDateFormat(strFormat);
            strDate = format.format(calendar.getTime());
        } catch (Exception e) {
            strDate = null;
            e.printStackTrace();
        }

        return strDate;
    }

    public static String formatDate(String strDate, String originalFormat, String desiredFormat) {
        return convertToString(convertToCalendar(strDate, originalFormat), desiredFormat);
    }

    public static String getDayName(String date, String dateFormat) {
        Calendar calendar = convertToCalendar(date, dateFormat);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String dayName = sdf.format(calendar.getTime());

        return dayName;
    }

    public static String getDayName(Calendar date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String dayName = sdf.format(date.getTime());

        return dayName;
    }

    public static String getCurrentTime() {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        int seconds = c.get(Calendar.SECOND);
        int minutes = c.get(Calendar.MINUTE);
        int hours = c.get(Calendar.HOUR_OF_DAY);

        return hours + ":" + minutes + ":" + seconds;
    }

    public static boolean isCurrentDate(Calendar calendar) {
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
        return (calendar.get(Calendar.DAY_OF_MONTH) == currentCalendar.get(Calendar.DAY_OF_MONTH)
                && calendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH))
                && calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR);
    }

    public static boolean isPastDate(Calendar calendar) {
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
//        calendar.set(Calendar.SECOND, 0);
//        currentCalendar.set(Calendar.SECOND, 0);

        Log.e("CUR", "" + currentCalendar.getTimeInMillis());
        Log.e("CALENDAR", "" + calendar.getTimeInMillis());

        return calendar.getTimeInMillis() < currentCalendar.getTimeInMillis();
    }
}
