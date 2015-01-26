package com.hettinger.sean.quiethoursreredux;

/*
    Background check is an intent started when the app loads.  It's called by MainActivity every 60 seconds and checks each alarm and calendar event.
    If the event is turned the timeframe is checked and the phone is put to normal volume or silent accordingly.
 */

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;


public class BackgroundCheck extends IntentService {

    AudioManager amanager;
    ArrayList<AlarmEvent> alarms = new ArrayList<AlarmEvent>();
    ArrayList<CalendarEvent> calendarEvents = new ArrayList<CalendarEvent>();


    public BackgroundCheck() {
        super("BackgroundCheck");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("BackgroundCheck", "Running");

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        CalendarDatabaseHandler cdb = new CalendarDatabaseHandler(getApplicationContext());
        calendarEvents = cdb.getAllCalendarEvents();
        alarms = db.getAllAlarms();


        for(AlarmEvent alarm : alarms)
            if(alarm.getIsSet())
                checkAlarmEvent(alarm);

        for(CalendarEvent calendarEvent : calendarEvents)
            if(calendarEvent.getIsSet())
                checkCalendarEvent(calendarEvent);


        return super.onStartCommand(intent,flags,startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    //Sets phone to silent
    public void setPhoneToSilent(){
        amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int ringMode = amanager.getRingerMode();

        Log.i("SetPhone to silent called and mode is = ", Integer.toString(amanager.getMode()));
        if(ringMode != AudioManager.RINGER_MODE_SILENT) {
            Toast.makeText(getApplicationContext(), "Quiet Hours: Phone set to Silent",
                    Toast.LENGTH_SHORT).show();
            //turn ringer silent
            amanager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            Log.i(".RINGER_MODE_SILENT", "Set to true");
            // turn on sound, enable notifications
            amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
            Log.i("STREAM_SYSTEM", "Set to False");
            //notifications
            amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
            Log.i("STREAM_NOTIFICATION", "Set to False");
            //alarm
            amanager.setStreamMute(AudioManager.STREAM_ALARM, false);
            Log.i("STREAM_ALARM", "Set to False");
            //ringer
            amanager.setStreamMute(AudioManager.STREAM_RING, false);
            Log.i("STREAM_RING", "Set to False");
            //media
            amanager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            Log.i("STREAM_MUSIC", "Set to False");
        }
    }

    //Sets phone to normal volume
    public void setPhoneToNormal(){
        amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int ringMode = amanager.getRingerMode();
        Log.i("SetPhoneToNormal", "Called");
        if(ringMode != AudioManager.RINGER_MODE_NORMAL){
            Toast.makeText(getApplicationContext(), " Quiet Hours: Phone set to Normal",
                    Toast.LENGTH_SHORT).show();
            amanager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            Log.i("RINGER_MODE_NORMAL", "Set to true");
            //turn off sound, disable notifications
            amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
            Log.i("STREAM_SYSTEM", "Set to true");
            //notifications
            amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
            Log.i("STREAM_NOTIFICATION", "Set to true");
            //alarm
            amanager.setStreamMute(AudioManager.STREAM_ALARM, true);
            Log.i("STREAM_ALARM", "Set to true");
            //ringer
            amanager.setStreamMute(AudioManager.STREAM_RING, true);
            Log.i("STREAM_RING", "Set to true");
            //media
            amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            Log.i("STREAM_MUSIC", "Set to true");
        }
    }

    //Checks if alarm event is turned on and compares the dates if it is.
    public void checkAlarmEvent(AlarmEvent alarm){
        Log.i("checkAlarmEvent called for alarm:", alarm.getName());

        Date currentTime = new Date();
        Date startTime = alarm.getStartTime();
        Date endTime = alarm.getEndTime();

            if(currentTime.after(startTime) && currentTime.before(endTime))
                setPhoneToSilent();
            else
                setPhoneToNormal();
        }

    //Same as checkAlarm event but a CalendarEvent object is passed through.
    public void checkCalendarEvent(CalendarEvent calendarEvent){
        Log.i("checkAlarmEvent called for alarm:", calendarEvent.getName());
        if(calendarEvent._isSet){
            Date currentTime = new Date();
            Date startTime = calendarEvent.getStartTime();
            Date endTime = calendarEvent.getEndTime();

            if(currentTime.after(startTime) && currentTime.before(endTime))
                setPhoneToSilent();
            else
                setPhoneToNormal();
        }
    }









}
