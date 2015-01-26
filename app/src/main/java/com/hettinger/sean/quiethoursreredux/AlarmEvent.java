package com.hettinger.sean.quiethoursreredux;


import java.io.Serializable;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Alarm Event Class
 *
 * Object holding information for user-created alarms
 *
 * Created by Sean Hettinger on 1/17/2015.
 */
public class AlarmEvent {

    int _id;
    String _name = "";
    Date _startTime;
    Date _endTime;
    boolean _isSet = false;
    boolean _mondaySet =false;
    boolean _tuesdaySet = false;
    boolean _wednesdaySet = false;
    boolean _thursdaySet = false;
    boolean _fridaySet = false;
    boolean _saturdaySet = false;
    boolean _sundaySet = false;


    public AlarmEvent(){

    }


    public AlarmEvent(int id, String name, String startTime, String endTime, boolean isSet, boolean mondaySet, boolean tuesdaySet, boolean wednesdaySet, boolean thursdaySet, boolean fridaySet, boolean saturdaySet, boolean sundaySet) {

        this._id = id;
        this._name = name;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy HH:mm");
            this._startTime = sdf.parse(startTime);
            this._endTime = sdf.parse(endTime);
        }catch(ParseException e){
            e.printStackTrace();
        }
        this._isSet = isSet;
        this._mondaySet = mondaySet;
        this._tuesdaySet = tuesdaySet;
        this._wednesdaySet = wednesdaySet;
        this._thursdaySet = thursdaySet;
        this._fridaySet = fridaySet;
        this._saturdaySet = saturdaySet;
        this._sundaySet = sundaySet;



    }

    public AlarmEvent(String name, String startTime, String endTime, boolean isSet){
        this._name = name;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy HH:mm");
            this._startTime = sdf.parse(startTime);
            this._endTime = sdf.parse(endTime);
        }catch(ParseException e){
            e.printStackTrace();
        }
        this._isSet = isSet;

    }

    public AlarmEvent(String name, String startTime, String endTime, boolean isSet, boolean mondaySet, boolean tuesdaySet, boolean wednesdaySet, boolean thursdaySet, boolean fridaySet, boolean saturdaySet, boolean sundaySet) {

        this._name = name;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy HH:mm");
            this._startTime = sdf.parse(startTime);
            this._endTime = sdf.parse(endTime);
        }catch(ParseException e){
            e.printStackTrace();
        }this._isSet = isSet;
        this._mondaySet = mondaySet;
        this._tuesdaySet = tuesdaySet;
        this._wednesdaySet = wednesdaySet;
        this._thursdaySet = thursdaySet;
        this._fridaySet = fridaySet;
        this._saturdaySet = saturdaySet;
        this._sundaySet = sundaySet;

    }

    public int getId(){
        return this._id;
    }
    public void setId(int id){
        this._id = id;
    }

    public String getName(){
        return this._name;
    }
    public void setName(String name){
        this._name = name;
    }

    public Date getStartTime(){
        return this._startTime;
    }
    public void setStartTime(Date startTime){
        this._startTime = startTime;
    }

    public Date getEndTime(){
        return this._endTime;
    }
    public void setEndTime(Date endTime){
        this._endTime = endTime;
    }

    public boolean getIsSet(){
        return _isSet;
    }
    public void setIsSet(boolean isSet){
        this._isSet = isSet;
    }

    public boolean getMondaySet(){
        return this._mondaySet;
    }
    public void setMondaySet(boolean set){
        this._mondaySet = set;
    }

    public boolean getTuesdaySet(){
        return this._tuesdaySet;
    }
    public void setTuesdaySet(boolean set){
        this._tuesdaySet = set;
    }

    public boolean getWednesdaySet(){
        return this._wednesdaySet;
    }
    public void setWednesdaySet(boolean set){
        this._wednesdaySet = set;
    }

    public boolean getThursdaySet(){
        return this._thursdaySet;
    }
    public void setThursdaySet(boolean set){
        this._thursdaySet = set;
    }

    public boolean getFridaySet(){
        return this._fridaySet;
    }
    public void setFridaySet(boolean set){
        this._fridaySet = set;
    }

    public boolean getSaturdaySet(){
        return this._saturdaySet;
    }
    public void setSaturdaySet(boolean set){
        this._saturdaySet = set;
    }

    public boolean getSundaySet(){
        return this._sundaySet;
    }
    public void setSundaySet(boolean set){
        this._sundaySet = set;
    }


}
