package com.monitumapp.android.monitum.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Parsing dates.
 */
public class DateUtils {
    private static SimpleDateFormat sFullUtcSdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
    private static SimpleDateFormat sDayOnlySdf = new SimpleDateFormat("EEE MMM dd, yyyy", Locale.getDefault());
    private static SimpleDateFormat sDateOnlySdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    public static Date parseDate(String dateStr) {
        if (TextUtils.isEmpty(dateStr)) return null;
        Date date = null;
        try {
            date = sFullUtcSdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static boolean isToday(Date date) {
        boolean result = false;
        Calendar todayCal = Calendar.getInstance(Locale.getDefault());
        if (todayCal != null) {
            Date today = todayCal.getTime();

            if (today != null) {
                result = date.getDay() == today.getDay() &&
                         date.getMonth() == today.getMonth() &&
                         date.getYear() == today.getYear();
            }
        }
        return result;
    }

    public static String formatDayOnly(Date date) {
        if (date == null) {
            return "-";
        } else {
            return sDayOnlySdf.format(date);
        }
    }

    public static String formatDateOnly(Date date) {
        if (date == null) {
            return "-";
        } else {
            return sDateOnlySdf.format(date);
        }
    }

    public static String formatDateOnly(int month, int day, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.YEAR, year);
        return sDateOnlySdf.format(calendar.getTime());
    }
}
