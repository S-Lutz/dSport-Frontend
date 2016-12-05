package com.omgproduction.dsport_application.utils;

import android.content.Context;
import android.util.Log;

import com.omgproduction.dsport_application.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Florian on 05.12.2016.
 */

public class DateUtils {
    public static String convertString(Context context,String dateString){

        try {
            Date currentDate = new Date();
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTime(currentDate);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
            Date postDate = dateFormat.parse(dateString);
            Calendar postCalendar = Calendar.getInstance();
            postCalendar.setTime(postDate);
            Locale currentLocale = Locale.getDefault();
            SimpleDateFormat targetDateFormat;

            if(currentCalendar.get(Calendar.YEAR) == postCalendar.get(Calendar.YEAR)){
                if(currentCalendar.get(Calendar.MONTH)==postCalendar.get(Calendar.MONTH)){
                    if(currentCalendar.get(Calendar.DAY_OF_MONTH)==postCalendar.get(Calendar.DAY_OF_MONTH)){
                        if(currentCalendar.get(Calendar.WEEK_OF_MONTH)==postCalendar.get(Calendar.WEEK_OF_MONTH)){
                            if(currentCalendar.get(Calendar.HOUR_OF_DAY)==postCalendar.get(Calendar.HOUR_OF_DAY)){
                                if(currentCalendar.get(Calendar.MINUTE)<=postCalendar.get(Calendar.MINUTE)+5&&currentCalendar.get(Calendar.MINUTE)>=postCalendar.get(Calendar.MINUTE)){
                                    //Same Minute //just now
                                    return context.getString(R.string.now);
                                }else {
                                    //Same Hour //minutes ago
                                    return context.getString(R.string.same_hour, postCalendar.get(Calendar.MINUTE));
                                }
                            }else {
                                //Same day //Today , Only hours
                                Log.e("DATE",context.getString(R.string.same_day));
                                targetDateFormat = new SimpleDateFormat(context.getString(R.string.same_day),currentLocale);
                                return targetDateFormat.format(postDate);
                            }
                        }else {
                            //Same week //Today , Only hours
                            Log.e("DATE",context.getString(R.string.same_week));
                            targetDateFormat = new SimpleDateFormat(context.getString(R.string.same_week),currentLocale);
                            return targetDateFormat.format(postDate);
                        }
                    }else {
                        //Same month but another day //Tag /Monat / Uhrzeit
                        Log.e("DATE",context.getString(R.string.same_month));
                        targetDateFormat = new SimpleDateFormat(context.getString(R.string.same_month),currentLocale);
                        return targetDateFormat.format(postDate);
                    }
                }else {
                    //Same year but other month //Without year without hours... //Monat ausgeschrieben
                    Log.e("DATE",context.getString(R.string.same_year));
                    targetDateFormat = new SimpleDateFormat(context.getString(R.string.same_year),currentLocale);
                    return targetDateFormat.format(postDate);
                }
            }else{
                //Another year //Complete date without hours... //Monat ausgeschrieben
                Log.e("DATE",context.getString(R.string.last_years));
                targetDateFormat = new SimpleDateFormat(context.getString(R.string.last_years),currentLocale);
                return targetDateFormat.format(postDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }
}
