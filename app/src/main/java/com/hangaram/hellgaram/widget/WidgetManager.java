package com.hangaram.hellgaram.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.GregorianCalendar;

class WidgetManager {
    private static final String TAG = "PeriodGiver";

    /*위젯 색깔 바꾸기 용*/
    public static final int WHITE = 255;
    public static final int BLACK = 0;

    /*위젯 업데이트 시간 배열*/
    public static final int[][] TIMETABLE_UPDATE_TIMES = {
            {9, 15},
            {10, 40},
            {12, 5},
            {14, 25},
            {15, 50},
            {17, 10}
    };

    public static void setAlaram(Context context, int hour, int minuate, int id, String action) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minuate);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        /*보낼 인텐트 생성하기*/
        Intent intent = new Intent(context, Timetable2Provider.class);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        /*알람매니저 설정하기*/
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    /*
     * 1교시: 1
     * 2교시: 2...
     * */
    public static int getPeriodByTime(int periodGap) {
        Calendar calendar = Calendar.getInstance();

        for (int period = 0; period < TIMETABLE_UPDATE_TIMES.length; period++) {
            if (calendar.get(Calendar.HOUR_OF_DAY) * 100 + calendar.get(Calendar.MINUTE) < (TIMETABLE_UPDATE_TIMES[period][0] * 100 + TIMETABLE_UPDATE_TIMES[period][1]))
                return period + periodGap + 1;
        }
        return 7 + periodGap;
    }

    /*
     * 월 1
     * 화 2..
     * */
    public static int getNeedDay(int periodGap) {
        int period = getPeriodByTime(periodGap);

        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, 0);

        if ((period >= 7 && calendar.get(Calendar.DAY_OF_WEEK) == 6) || calendar.get(Calendar.DAY_OF_WEEK) == 1 || calendar.get(Calendar.DAY_OF_WEEK) == 7)
            /*금요일 6교시 이후나 토요일, 일요일일때*/
            return 1;
        else if (period >= 7)
            /*월,화,수,목 6교시 이후*/
            return calendar.get(Calendar.DAY_OF_WEEK);
        else
            /*그 밖의 경우*/
            return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static int getNeedPeriod(int periodGap) {
        int period = getPeriodByTime(periodGap);

        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, 0);

        if ((period >= 7 && calendar.get(Calendar.DAY_OF_WEEK) == 6) || calendar.get(Calendar.DAY_OF_WEEK) == 1 || calendar.get(Calendar.DAY_OF_WEEK) == 7)
            /*6교시 이후나 토요일, 일요일일때*/
            return 1 + periodGap;
        else if (period >= 7)
            /*6교시 이후(금요일 제외)*/
            return period - 6;
        else
            /*그 밖의 경우*/
            return period;
    }

    public static String getPeriodName(String[] subjectArray) {
        if (subjectArray.length > 0)
            return subjectArray[0];
        else
            return "";
    }

    public static String getPeriodHint(String[] subjectArray) {
        StringBuilder hintString = new StringBuilder();

        for (int i = 1; i < subjectArray.length; i++) {
            hintString.append(subjectArray[i]);

            if (i != subjectArray.length - 1)
                hintString.append("/");
        }

        return hintString.toString();
    }
}
