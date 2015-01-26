package com.hettinger.sean.quiethoursreredux;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


//Accesses user's calendar, reads all future calendar events, and adds them to the database of saved calendar events

public class CalendarReader{

    public CalendarReader() {

    }

    public static void readCalendarEvent(Context context) {
        Log.i("CalendarReader readCalender", "called");
        CalendarDatabaseHandler cdb = new CalendarDatabaseHandler(context);
        cdb.deleteAllEvents();
        Cursor cursor = context.getContentResolver()
            .query(
                Uri.parse("content://com.android.calendar/events"),
                    new String[]{"calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation"}, null,
                                 null, null);
        cursor.moveToFirst();
        // fetching calendars name
        String CNames[] = new String[cursor.getCount()];

        // fetching calendars id
        SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy HH:mm");
        for (int i = 0; i < CNames.length; i++) {
            String endDateString = getDate((Long.parseLong(cursor.getString(4))));
            String startDateString = getDate(Long.parseLong(cursor.getString(3)));
            try{
                Date endDate = sdf.parse(endDateString);
                Date currentDate = new Date();
                if(endDate.after(new Date())){
                    Log.i("CalendarReader", "added Calendar Event");
                    Log.i("CNames.length is,", Integer.toString(CNames.length));
                    Log.i("EndDate of Calendar event is", sdf.format(endDate));

                    CalendarEvent calendarEvent = new CalendarEvent(i, cursor.getString(1),startDateString, endDateString, false);
                    cdb.addCalendarEvent(calendarEvent);}
            }
            catch(ParseException e){
                e.printStackTrace();
            }

            CNames[i] = cursor.getString(1);
            cursor.moveToNext();
        }
        cursor.close();
    }

    public static String getDate(long milliSeconds) {
        SimpleDateFormat sdf = new SimpleDateFormat(
                "MM.dd.yyyy HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return sdf.format(calendar.getTime());
    }
}