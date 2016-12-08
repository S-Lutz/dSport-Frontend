package com.omgproduction.dsport_application.utils;

import android.content.Context;
import android.util.Log;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.config.BackendConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Florian on 05.12.2016.
 */

public class DateUtils {

    public static String convertDate(Context context, String dateString) {
        try {
            Date currentDate = new Date();
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTime(currentDate);

            SimpleDateFormat dateFormat = new SimpleDateFormat(BackendConfig.DATE_FORMAT);
            Date postDate = dateFormat.parse(dateString);
            Calendar postCalendar = Calendar.getInstance();
            postCalendar.setTime(postDate);



        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String convertString(Context context,String dateString){

        try {
            Date currentDate = new Date();
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTime(currentDate);

            SimpleDateFormat dateFormat = new SimpleDateFormat(BackendConfig.DATE_FORMAT);
            Date postDate = dateFormat.parse(dateString);
            Calendar postCalendar = Calendar.getInstance();
            //TODO NORMALIZE TIMEZONE postCalendar.setTimeZone(currentCalendar.getTimeZone());
            postCalendar.setTime(postDate);

            Locale currentLocale = Locale.getDefault();
            SimpleDateFormat targetDateFormat;

            long timeMillisNow = currentCalendar.getTimeInMillis();
            long timeMillisPost = postCalendar.getTimeInMillis();

            long difference = timeMillisNow-timeMillisPost;

            //TODO Überdenke Difference

            long minute = 1000*60;
            long hour = 60 * minute;
            long day = 24 * hour;
            long week = 7 * day;


            if(currentCalendar.get(Calendar.YEAR) == postCalendar.get(Calendar.YEAR)){
                if(currentCalendar.get(Calendar.MONTH)==postCalendar.get(Calendar.MONTH) || (difference < week&&difference>0)){
                    //WOCHE
                        if(currentCalendar.get(Calendar.DAY_OF_MONTH)==postCalendar.get(Calendar.DAY_OF_MONTH)){
                            //DAY
                            if(difference<hour&&difference>0){
                                int minutesAgo =(int) difference/(int)minute;
                                if(minutesAgo>-5&&minutesAgo<=5){
                                    //Same Minute //just now
                                    return context.getString(R.string.now);
                                }else {
                                    //Same Hour //minutes ago
                                    return context.getString(R.string.same_hour, minutesAgo);
                                }
                            }else {
                                //Same day //Today , Only hours
                                targetDateFormat = new SimpleDateFormat(context.getString(R.string.same_day),currentLocale);
                                return targetDateFormat.format(postDate);
                            }
                        }else {
                            //Same week , Only hours
                            targetDateFormat = new SimpleDateFormat(context.getString(R.string.same_week),currentLocale);
                            return targetDateFormat.format(postDate);
                        }
                }else {
                    //Same year but other month //Without year without hours... //Monat ausgeschrieben
                    targetDateFormat = new SimpleDateFormat(context.getString(R.string.same_year),currentLocale);
                    return targetDateFormat.format(postDate);
                }
            }else{
                //Another year //Complete date without hours... //Monat ausgeschrieben
                targetDateFormat = new SimpleDateFormat(context.getString(R.string.last_years),currentLocale);
                return targetDateFormat.format(postDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }
}
