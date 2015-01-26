package com.hettinger.sean.quiethoursreredux;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Custom Adapter used to set list views with textview and switch button with onCheckChanged method
 *
 * Created by Sean on 1/26/2015.
 */
public class CalendarListViewAdapter extends ArrayAdapter<ListViewItem> {

    Context context;
    int layoutResourceId;
    ArrayList<ListViewItem> data = null;

    public CalendarListViewAdapter(Context context, int layoutResourceId, ArrayList<ListViewItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ListViewItemHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ListViewItemHolder();

            holder.listViewSwitch = (Switch) row.findViewById(R.id.listViewSwitch);
            holder.txtTitle = (TextView) row.findViewById(R.id.txtTitle);

            holder.listViewSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    CalendarDatabaseHandler db = new CalendarDatabaseHandler(getContext());
                    ListViewItem item = data.get(position);
                    int alarmId = item.id;

                     if(isChecked) {

                        CalendarEvent calendarEvent = db.getCalendarEvent(alarmId);
                        calendarEvent.setIsSet(true);
                        db.updateEvent(calendarEvent);
                        Log.i("CalendarEvent of Id ", alarmId + " is set");
                         Log.i("CalendarEvent Actual isSet = ", Boolean.toString(calendarEvent.getIsSet()));
                    }
                    else {

                        CalendarEvent calendarEvent = db.getCalendarEvent(alarmId);
                        calendarEvent.setIsSet(false);
                        db.updateEvent(calendarEvent);
                        Log.i("CalendarEvent of Id ", alarmId + " is off");
                         Log.i("Calendar Actual isSet = ", Boolean.toString(calendarEvent.getIsSet()));

                    }
                }
            });

            row.setTag(holder);

        } else {
            holder = (ListViewItemHolder) row.getTag();
        }

        ListViewItem listViewItem = data.get(position);
        holder.txtTitle.setText(listViewItem.title);
        holder.listViewSwitch.setChecked(false);
        return row;
    }

    static class ListViewItemHolder {
        Switch listViewSwitch;
        TextView txtTitle;
    }
}
