package com.hettinger.sean.quiethoursreredux;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Sean on 1/19/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "SavedAlarmsDatabase";

    // Contacts table name
    private static final String TABLE_ALARM = "Alarms";


    // Contacts Table Columns namecreates
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ST_TIME = "startTime";
    private static final String KEY_END_TIME = "endTime";
    private static final String KEY_IS_SET = "isSet";
    private static final String KEY_MONDAY_SET = "Monday";
    private static final String KEY_TUESDAY_SET = "Tuesday";
    private static final String KEY_WEDNESDAY_SET = "Wednesday";
    private static final String KEY_THURSDAY_SET = "Thursday";
    private static final String KEY_FRIDAY_SET = "Friday";
    private static final String KEY_SATURDAY_SET = "Saturday";
    private static final String KEY_SUNDAY_SET = "Sunday";
    private static final String KEY_GOES_OVERNIGHT = "goesOvernight";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ALARM_TABLE = "CREATE TABLE "
                + TABLE_ALARM + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_NAME + " TEXT, "
                + KEY_ST_TIME + " TEXT, "
                + KEY_END_TIME + " TEXT, "
                + KEY_IS_SET + " TEXT, "
                + KEY_MONDAY_SET + " TEXT, "
                + KEY_TUESDAY_SET + " TEXT, "
                + KEY_WEDNESDAY_SET + " TEXT, "
                + KEY_THURSDAY_SET + " TEXT, "
                + KEY_FRIDAY_SET + " TEXT, "
                + KEY_SATURDAY_SET + " TEXT, "
                + KEY_SUNDAY_SET + " TEXT, "
                + KEY_GOES_OVERNIGHT + " TEXT)";
        db.execSQL(CREATE_ALARM_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARM);

        // Create tables again
        onCreate(db);
    }
    // Adding new contact
    public void addAlarm(AlarmEvent alarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy HH:mm");


        values.put(KEY_NAME, alarm.getName());
        values.put(KEY_ST_TIME, sdf.format(alarm.getStartTime()));
        values.put(KEY_END_TIME, sdf.format(alarm.getEndTime()));
        values.put(KEY_IS_SET, alarm.getIsSet());
        values.put(KEY_MONDAY_SET, alarm.getMondaySet());
        values.put(KEY_TUESDAY_SET, alarm.getTuesdaySet());
        values.put(KEY_WEDNESDAY_SET, alarm.getWednesdaySet());
        values.put(KEY_THURSDAY_SET, alarm.getThursdaySet());
        values.put(KEY_FRIDAY_SET, alarm.getFridaySet());
        values.put(KEY_SATURDAY_SET, alarm.getSaturdaySet());
        values.put(KEY_SUNDAY_SET, alarm.getSundaySet());


        // Inserting Row
        db.insert(TABLE_ALARM, null, values);
        db.close();
        // Closing database connection
    }

    // Getting single contact
    public AlarmEvent getAlarm(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ALARM, new String[]{KEY_ID, KEY_NAME, KEY_ST_TIME, KEY_END_TIME, KEY_IS_SET,
                        KEY_MONDAY_SET, KEY_TUESDAY_SET, KEY_WEDNESDAY_SET, KEY_THURSDAY_SET, KEY_FRIDAY_SET, KEY_SATURDAY_SET, KEY_SUNDAY_SET, KEY_GOES_OVERNIGHT}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        AlarmEvent alarm = new AlarmEvent(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4) == 1, cursor.getInt(5) == 1, cursor.getInt(6) == 1, cursor.getInt(7) == 1, cursor.getInt(8) == 1, cursor.getInt(9) == 1, cursor.getInt(10) == 1, cursor.getInt(11) == 1);

        return alarm;
    }

    // Getting All Alarms
    public ArrayList<AlarmEvent> getAllAlarms() {
        ArrayList<AlarmEvent> alarmEventList = new ArrayList<AlarmEvent>();

        String selectQuery = "SELECT * FROM " + TABLE_ALARM;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do {
                AlarmEvent alarm = new AlarmEvent();
                alarm.setId(Integer.parseInt(cursor.getString(0)));
                alarm.setName(cursor.getString(1));
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy HH:mm");
                    alarm.setStartTime(sdf.parse(cursor.getString(2)));
                    alarm.setEndTime(sdf.parse(cursor.getString(3)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                alarm.setIsSet(cursor.getInt(4) == 1);
                alarm.setMondaySet(cursor.getInt(5) == 1);
                alarm.setTuesdaySet(cursor.getInt(6) == 1);
                alarm.setWednesdaySet(cursor.getInt(7) == 1);
                alarm.setThursdaySet(cursor.getInt(8) == 1);
                alarm.setFridaySet(cursor.getInt(9) == 1);
                alarm.setSaturdaySet(cursor.getInt(10) == 1);
                alarm.setSundaySet(cursor.getInt(11) == 1);
                alarmEventList.add(alarm);
            }
            while(cursor.moveToNext());}



        return alarmEventList;
    }


    public int updateAlarm(AlarmEvent alarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, alarm.getName());
        SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy HH:mm");
        values.put(KEY_ST_TIME, sdf.format(alarm.getStartTime()));
        values.put(KEY_END_TIME, sdf.format(alarm.getEndTime()));
        values.put(KEY_IS_SET, alarm.getIsSet());
        values.put(KEY_MONDAY_SET, alarm.getMondaySet());
        values.put(KEY_TUESDAY_SET, alarm.getTuesdaySet());
        values.put(KEY_WEDNESDAY_SET, alarm.getWednesdaySet());
        values.put(KEY_THURSDAY_SET, alarm.getThursdaySet());
        values.put(KEY_FRIDAY_SET, alarm.getFridaySet());
        values.put(KEY_SATURDAY_SET, alarm.getSaturdaySet());
        values.put(KEY_SUNDAY_SET, alarm.getSundaySet());


        return db.update(TABLE_ALARM, values, KEY_ID + " = ?",
                new String[] { String.valueOf(alarm.getId()) });


    }


    public void deleteAlarm(AlarmEvent alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALARM, KEY_ID + " = ?",
                new String[] { String.valueOf(alarm.getId()) });
        db.close();

    }


}