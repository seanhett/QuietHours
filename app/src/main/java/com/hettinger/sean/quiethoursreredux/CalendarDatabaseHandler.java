package com.hettinger.sean.quiethoursreredux;

/**
 * A SQLite database handler for CRUD operations of Calendar Events
 *
 * Created by Sean on 1/24/2015.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Class used to handle the CalendarEvent database
 *
 * Created by Sean on 1/19/2015.
 */
public class CalendarDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;


    // Database Name
    private static final String DATABASE_NAME = "SavedCalendarDatabase";

    // Contacts table name
    private static final String TABLE_CALENDAR = "Calendars";


    // Contacts Table Columns namecreates
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ST_TIME = "startTime";
    private static final String KEY_END_TIME = "endTime";
    private static final String KEY_IS_SET = "isSet";



    public CalendarDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.i("OncreaeCalendarEventDatabase", "called");

        String CREATE_CALENDAR_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_CALENDAR + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_NAME + " TEXT, "
                + KEY_ST_TIME + " TEXT, "
                + KEY_END_TIME + " TEXT, "
                + KEY_IS_SET + " TEXT)";
        db.execSQL(CREATE_CALENDAR_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("onUpgradeCalendarEventDatabase", "called");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALENDAR);

        // Create tables again
        onCreate(db);
    }
    // Adding new contact
    public void addCalendarEvent(CalendarEvent calendarEvent) {
        Log.i("addCalendarEvent", "called");
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy HH:mm");

        values.put(KEY_NAME, calendarEvent.getName());
        values.put(KEY_ST_TIME, sdf.format(calendarEvent.getStartTime()));
        values.put(KEY_END_TIME, sdf.format(calendarEvent.getEndTime()));
        values.put(KEY_IS_SET, calendarEvent.getIsSet());

        // Inserting Row
        db.insert(TABLE_CALENDAR, null, values);
        db.close();
        // Closing database connection
    }

    // Getting single contact
    public CalendarEvent getCalendarEvent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CALENDAR, new String[]{KEY_ID, KEY_NAME, KEY_ST_TIME, KEY_END_TIME, KEY_IS_SET}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        return new CalendarEvent(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4) == 1);
    }


    public ArrayList<CalendarEvent> getAllCalendarEvents() {
        Log.i("GetAllCalendarEvent", "called");
        ArrayList<CalendarEvent> calendarEventList = new ArrayList<CalendarEvent>();

        String selectQuery = "SELECT * FROM " + TABLE_CALENDAR;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                CalendarEvent calendarEvent = new CalendarEvent();
                calendarEvent.setId(Integer.parseInt(cursor.getString(0)));
                calendarEvent.setName(cursor.getString(1));
                try{
                SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy HH:mm");
                calendarEvent.setStartTime(sdf.parse(cursor.getString(2)));
                calendarEvent.setEndTime(sdf.parse(cursor.getString(3)));
                }catch (ParseException e){
                    e.printStackTrace();
                }
                calendarEvent.setIsSet(cursor.getInt(4) == 1);

                calendarEventList.add(calendarEvent);
            }while(cursor.moveToNext());
        }
        return calendarEventList;
    }


    public int updateEvent(CalendarEvent event) {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy HH:mm");
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, event.getName());
        values.put(KEY_ST_TIME, sdf.format(event.getStartTime()));
        values.put(KEY_END_TIME, sdf.format(event.getEndTime()));
        values.put(KEY_IS_SET, event.getIsSet());

        return db.update(TABLE_CALENDAR, values, KEY_ID + " = ?",
                new String[] { String.valueOf(event.getId()) });


    }


    public void deleteEvent(CalendarEvent calendar) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CALENDAR, KEY_ID + " = ?",
                new String[] { String.valueOf(calendar.getId()) });
        db.close();
    }

    public void deleteAllEvents(){
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(TABLE_CALENDAR, null, null);
    }
}
