package com.hettinger.sean.quiethoursreredux;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * CalendarEvent Object.  A new alarm is created for each event saved in the user's calendar
 *
 * Created by Sean on 1/24/2015.
 */
public class CalendarEvent {



    int _id;
    String _name = "";
    boolean _isSet = false;
    Date _startTime;
    Date _endTime;


    public CalendarEvent(){
        Log.i("BlankCalendarEvent", "created");
    }

    public CalendarEvent(int id, String name, String startTime, String endTime, boolean isSet){


        Log.i("CalendarEvern", "created");

        this._id = id;
        this._name = name;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy HH:mm");
            this._startTime = sdf.parse(startTime);
            this._endTime = sdf.parse(endTime);
        } catch(java.text.ParseException e){
            e.printStackTrace();
        }


    }


    public void setId(int id) {
        this._id = id;
    }

    public int getId() {
        return _id;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getName(){
        return _name;
    }

    public void setIsSet(boolean isSet){
        this._isSet = isSet;
    }

    public boolean getIsSet(){
        return  _isSet;
    }

    public void setStartTime(Date startTime){
        this._startTime = startTime;
    }

    public Date getStartTime(){
        return _startTime;
    }

    public void setEndTime(Date endTime){
        this._endTime = endTime;
    }

    public Date getEndTime(){
        return _endTime;
    }

}