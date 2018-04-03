package com.example.android.constantinenews.utilities;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * Created by merouane on 16/02/2018.
 */

public class DateUtils {

    private static final String TAG = DateUtils.class.toString();

    public static long getTimeStamp(String pubDate){


        //DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;


        Date date = null;
        try{
            DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
            date = formatter.parse(pubDate);

            return date.getTime();
        }catch (Exception e){
            Log.e(TAG, "could not parse date");
        }

        return 1;
    }

    public static String getDateFromLong(long value){

        Date date = new Date(value);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yy");
        String dateText = dateFormatter.format(date);
        return dateText;
    }
}
