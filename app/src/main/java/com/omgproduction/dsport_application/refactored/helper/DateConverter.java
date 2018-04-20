package com.omgproduction.dsport_application.refactored.helper;

import android.content.Context;

import com.omgproduction.dsport_application.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateConverter {

    private final String APPLICATION_CONFIG_DATE_FORMAT ="yyyy-MM-dd HH:mm:ss.SSS";

    private SimpleDateFormat dateFormat;

    public DateConverter(){
        dateFormat = new SimpleDateFormat(APPLICATION_CONFIG_DATE_FORMAT, Locale.getDefault());
    }
    public String convertString(Context context, String dateString){
        Calendar currentCalendar = getCurrentCalendar();
        Calendar postCalendar;

        TimeZone timeZone = TimeZone.getTimeZone("Europe/Berlin");

        try{
            Date postDate = dateFormat.parse(dateString);
            long offset = timeZone.getOffset(postDate.getTime());
            postCalendar = Calendar.getInstance();
            postCalendar.setTime(postDate);
            if(TimeZone.getDefault().inDaylightTime(new Date())&&!postCalendar.getTimeZone().inDaylightTime(postDate)){
                postCalendar.add(Calendar.MILLISECOND, (int) offset);
            }else if(!TimeZone.getDefault().inDaylightTime(new Date())&&postCalendar.getTimeZone().inDaylightTime(postDate)){
                postCalendar.add(Calendar.MILLISECOND, (int) offset + (-1));
            }


            CharSequence timeAgo = android.text.format.DateUtils.getRelativeTimeSpanString(postCalendar.getTimeInMillis(),currentCalendar.getTimeInMillis(),10000);

            return timeAgo.toString();
        } catch (ParseException e) {
            return  context.getString(R.string.unknown);
        }
    }
    public String convertDate(Date date){
        return dateFormat.format(date);
    }

    public String convertNow(){
        return convertDate(new Date());
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