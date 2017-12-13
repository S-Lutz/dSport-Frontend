package com.omgproduction.dsport_application.aaRefactored.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
    private static final SimpleDateFormat  srcDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static final SimpleDateFormat trgDateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");

    public static final String convertDate(String dateToConvert){
        Date date;
        try {
            date = srcDateFormat.parse((dateToConvert));
             return trgDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


} 