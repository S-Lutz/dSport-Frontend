package com.omgproduction.dsport_application.utils;

import android.content.Context;
import android.util.Log;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.config.BackendConfig;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

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
            postCalendar.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
            postCalendar.setTime(postDate);


            long offset = currentCalendar.getTimeZone().getOffset(postCalendar.getTimeInMillis())-(60*60*1000);


            CharSequence timeAgo = android.text.format.DateUtils.getRelativeTimeSpanString(postCalendar.getTimeInMillis()+offset,currentCalendar.getTimeInMillis(),10000);

            return timeAgo.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }
}
