package com.hettinger.sean.quiethoursreredux;
//need to add on/off switches/indicators for alarm and calendar listview options


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
*NewEventFragments deals with the setting of custom alarms
 */
public class NewEventFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    boolean alarmExists;
    boolean onOff;

    DatabaseHandler db;


    TimePicker startTimePicker;
    TimePicker endTimePicker;

    CheckBox mondayCheckBox;
    CheckBox tuesdayCheckBox;
    CheckBox wednesdayCheckBox;
    CheckBox thursdayCheckBox;
    CheckBox fridayCheckBox;
    CheckBox saturdayCheckBox;
    CheckBox sundayCheckBox;

    int id;

    Date startTime;
    Date endTime;

    Button saveAlarm;

    AlarmEvent alarm = new AlarmEvent();

    android.support.v7.app.ActionBar actionBar;

    private OnFragmentInteractionListener mListener;

    public static NewEventFragment newInstance(String param1, String param2) {
        NewEventFragment fragment = new NewEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public NewEventFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_new_event, container, false);

        actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();;

        db = new DatabaseHandler(getActivity().getApplicationContext());

        startTimePicker = (TimePicker)rootView.findViewById(R.id.startTimePicker);
        endTimePicker = (TimePicker)rootView.findViewById(R.id.endTimePicker);

        mondayCheckBox = (CheckBox)rootView.findViewById(R.id.mondayCheckBox);
        tuesdayCheckBox = (CheckBox)rootView.findViewById(R.id.tuesdayCheckBox);
        wednesdayCheckBox = (CheckBox)rootView.findViewById(R.id.wednesdayCheckBox);
        thursdayCheckBox = (CheckBox)rootView.findViewById(R.id.thursdayCheckBox);
        fridayCheckBox = (CheckBox)rootView.findViewById(R.id.fridayCheckBox);
        saturdayCheckBox = (CheckBox)rootView.findViewById(R.id.saturdayCheckBox);
        sundayCheckBox = (CheckBox)rootView.findViewById(R.id.sundayCheckBox);

        saveAlarm = (Button)rootView.findViewById(R.id.saveAlarmButton);

        actionBar.setNavigationMode(android.app.ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        if (getArguments() != null) {
            int loadedAlarmId = getArguments().getInt("alarmId", 0);
            if (loadedAlarmId != 0) {
                alarmExists = true;
                    Log.i("Alarm Exists, Loading Alarm Id: ", Integer.toString(loadedAlarmId));
                    alarm = db.getAlarm(loadedAlarmId);
                    loadAlarmParameters(alarm);
                    setTimePickers();
                    actionBar.setTitle(alarm.getName());
                }
            }
        else{
            Log.i("alarmExists", "Set to false");
                alarmExists = false;
                actionBar.setTitle("New Alarm");
        }

            saveAlarm.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.saveAlarmButton:
                                if (!alarmExists) {

                                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                    final EditText input = new EditText(getActivity());

                                    alert.setTitle("Please enter a name for your alarm");
                                    alert.setView(input);

                                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {

                                            String enteredName;

                                            if (input.getText().toString() != null)
                                                enteredName = input.getText().toString();
                                            else
                                                enteredName = " ";

                                            alarm.setName(enteredName);
                                            setAlarmParameters(alarm);
                                            db.addAlarm(alarm);
                                            getActivity().getSupportFragmentManager().popBackStack();
                                            closeEventScreen();
                                            onDestroyView();
                                        }
                                    });

                                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                        }
                                    });
                                    alert.show();
                                } else {
                                    setAlarmParameters(alarm);
                                    db.updateAlarm(alarm);
                                    closeEventScreen();
                                    onDestroyView();
                                }
                                break;
                        }
                    }

                });
        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void onDestroyView() {
        ViewGroup mContainer = (ViewGroup) getView().findViewById(R.id.eventSelectionLayout);
        mContainer.removeAllViews();
        super.onDestroyView();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onDetach() {
        super.onDetach();
    }

    public Date getStartTime(){

        int startHour = startTimePicker.getCurrentHour();
        int startMinute = startTimePicker.getCurrentMinute();
        Date newStartTime = new Date();

        String startTimeString = String.format("%02d", startHour) + ":" + String.format("%02d", startMinute);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try{
            newStartTime = sdf.parse(startTimeString);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return newStartTime;
    }

    public Date getEndTime() {

        int endHour = endTimePicker.getCurrentHour();
        int endMinute = endTimePicker.getCurrentMinute();
        Date newEndTime = new Date();

        String endTimeString = String.format("%02d", endHour) + ":" + String.format("%02d", endMinute);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            newEndTime = sdf.parse(endTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newEndTime;
    }

    public void setAlarmParameters(AlarmEvent alarm){

        alarm.setMondaySet(mondayCheckBox.isChecked());
        alarm.setTuesdaySet(tuesdayCheckBox.isChecked());
        alarm.setWednesdaySet(wednesdayCheckBox.isChecked());
        alarm.setThursdaySet(thursdayCheckBox.isChecked());
        alarm.setFridaySet(fridayCheckBox.isChecked());
        alarm.setSaturdaySet(saturdayCheckBox.isChecked());
        alarm.setSundaySet(sundayCheckBox.isChecked());
        alarm.setIsSet(onOff);
        alarm.setStartTime(getStartTime());
        alarm.setEndTime(getEndTime());
        alarm.setId(id);
    }

    public void loadAlarmParameters(AlarmEvent alarm){
        id = alarm.getId();
        mondayCheckBox.setChecked(alarm.getMondaySet());
        tuesdayCheckBox.setChecked(alarm.getTuesdaySet());
        wednesdayCheckBox.setChecked(alarm.getWednesdaySet());
        thursdayCheckBox.setChecked(alarm.getThursdaySet());
        fridayCheckBox.setChecked(alarm.getFridaySet());
        saturdayCheckBox.setChecked(alarm.getSaturdaySet());
        sundayCheckBox.setChecked(alarm.getSundaySet());
        endTime = alarm.getEndTime();
        startTime = alarm.getStartTime();
    }

    public void setTimePickers() {
        startTimePicker.setCurrentHour(startTime.getHours());
        startTimePicker.setCurrentMinute(startTime.getMinutes());

        endTimePicker.setCurrentHour(endTime.getHours());
        endTimePicker.setCurrentMinute(endTime.getMinutes());
    }

    public void closeEventScreen(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        AlarmList fragment = new AlarmList();
        ft.replace(R.id.eventSelectionLayout, fragment );
        ft.commit();
    }
}
