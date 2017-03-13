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
    public String convertString(String dateString){
        Calendar currentCalendar = getCurrentCalendar();
        Calendar postCalendar;

        try{
            Date postDate = dateFormat.parse(dateString);
            postCalendar = Calendar.getInstance();
            postCalendar.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
            postCalendar.setTime(postDate);
            long offset = currentCalendar.getTimeZone().getOffset(postCalendar.getTimeInMillis())-(60*60*1000);

            CharSequence timeAgo = android.text.format.DateUtils.getRelativeTimeSpanString(postCalendar.getTimeInMillis()+offset,currentCalendar.getTimeInMillis(),10000);

            return timeAgo.toString();
        } catch (ParseException e) {
            return "unknown";
        }
    }
    public String convertDate(Date date){
        return dateFormat.format(date);
    }

    public String getFormatedDate(Date date, Context context){
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        return dateFormat.format(date);
    }

    public Calendar getCurrentCalendar(){
        Date currentDate = new Date();
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(currentDate);
        return currentCalendar;
    }

    public Date getDateFromValues(int year, int month, int dayOfMonth){
        String strThatDay = year+"/"+month+"/"+dayOfMonth;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date d = null;
        try {
            d = formatter.parse(strThatDay);//catch exception
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return d;
    }
}
