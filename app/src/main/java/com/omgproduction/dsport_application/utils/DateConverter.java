package com.omgproduction.dsport_application.utils;

import com.omgproduction.dsport_application.config.ApplicationConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Florian on 05.12.2016.
 */

public class DateConverter implements ApplicationConfig{

    private SimpleDateFormat dateFormat;

    public DateConverter(){
        dateFormat = new SimpleDateFormat(APPLICATION_CONFIG_DATE_FORMAT);
    }

    //public String convertDate(Context context, String dateString) {
    //
    //        Calendar currentCalendar = getCurrentCalendar();
//
    //        Date postDate = dateFormat.parse(dateString);
    //        Calendar postCalendar = Calendar.getInstance();
    //        postCalendar.setTime(postDate);
//
//
//
    //    } catch (ParseException e) {
    //        e.printStackTrace();
    //    }
    //    return null;
    //}
    public String convertString(String dateString){
        Calendar currentCalendar = getCurrentCalendar();
        Calendar postCalendar = getPostCalendar(dateString);
        if(postCalendar==null) return "unknown";
        long offset = currentCalendar.getTimeZone().getOffset(postCalendar.getTimeInMillis())-(60*60*1000);

        CharSequence timeAgo = android.text.format.DateUtils.getRelativeTimeSpanString(postCalendar.getTimeInMillis()+offset,currentCalendar.getTimeInMillis(),10000);

        return timeAgo.toString();
    }

    private Calendar getPostCalendar(String dateString) {
        try {
            Date postDate = dateFormat.parse(dateString);
            Calendar postCalendar = Calendar.getInstance();
            postCalendar.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
            postCalendar.setTime(postDate);
            return postCalendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Calendar getCurrentCalendar(){
        Date currentDate = new Date();
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(currentDate);
        return currentCalendar;
    }
}
