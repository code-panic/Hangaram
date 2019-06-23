package com.hangaram.hellgaram.widget;

import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

class PeriodGiver {
    private static final String TAG = "PeriodGiver";

    /*위젯 업데이트 시간 배열*/
    public static int[][] sUpdateTimeArray = {
            {9, 15},
            {10, 40},
            {12, 5},
            {14, 25},
            {15, 50},
            {17, 10}
    };

    /*
     * 1교시: 1
     * 2교시: 2...
     * */
    public static int getCurrentPeriod() {
        Log.d(TAG, "getCurrentPeriod 함수 작동하기");
        Calendar calendar = Calendar.getInstance();

        for (int period = 0; period < sUpdateTimeArray.length; period++) {
            if (calendar.get(Calendar.HOUR_OF_DAY) * 100 + calendar.get(Calendar.MINUTE) < (sUpdateTimeArray[period][0] * 100 + sUpdateTimeArray[period][1])) {
                Log.d(TAG, "현재시간: " + (calendar.get(Calendar.HOUR_OF_DAY) * 100 + calendar.get(Calendar.MINUTE)) + " 비교시간: " + (sUpdateTimeArray[period][0] * 100 + sUpdateTimeArray[period][1]) + " 과목: " + period);
                return period + 1;
            }
        }
        return 7;
    }

    /*
     * 월 1
     * 화 2..
     * */
    static int getDayOfWeek(int period) {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, 0);

        if ((period >= 7 && calendar.get(Calendar.DAY_OF_WEEK) == 6) || !(calendar.get(Calendar.DAY_OF_WEEK) >= 1 && calendar.get(Calendar.DAY_OF_WEEK) <= 5)) {
            /*금요일 6교시 이후나 토요일, 일요일일때*/
            return 1;
        } else if (period >= 7) {
            /*월,화,수,목 6교시 이후*/
            return calendar.get(Calendar.DAY_OF_WEEK);
        } else {
            /*그 밖의 경우*/
            return calendar.get(Calendar.DAY_OF_WEEK) - 1;
        }
    }

    public static int getPeriod(int period) {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, 0);

        if (period >= 7 || !(calendar.get(Calendar.DAY_OF_WEEK) >= 2 && calendar.get(Calendar.DAY_OF_WEEK) <= 6)) {
            /*6교시 이후나 토요일, 일요일일때*/
            return period - 6;
        } else {
            /*그 밖의 경우*/
            return period;
        }
    }
}
