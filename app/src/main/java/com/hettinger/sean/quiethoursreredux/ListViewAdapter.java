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
 *
 * Adapter used to create custom listview items for alarm events.
 *
 * Created by Sean on 1/26/2015.
 */
public class ListViewAdapter extends ArrayAdapter<ListViewItem> {

    Context context;
    int layoutResourceId;
    ArrayList<ListViewItem> data = null;

    public ListViewAdapter(Context context, int layoutResourceId, ArrayList<ListViewItem> data) {
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

                    DatabaseHandler db = new DatabaseHandler(getContext());
                    ListViewItem item = data.get(position);
                    int alarmId = item.id;

                     if(isChecked) {
                        AlarmEvent alarm = db.getAlarm(alarmId);
                        alarm.setIsSet(true);
                        db.updateAlarm(alarm);
                        Log.i("Alarm of Id ", alarmId + " is set");
                         Log.i("Alarm Actual isSet = ", Boolean.toString(alarm.getIsSet()));
                    }
                    else {
                        AlarmEvent alarm = db.getAlarm(alarmId);
                        alarm.setIsSet(false);
                        db.updateAlarm(alarm);
                        Log.i("Alarm of Id ", alarmId + " is off");
                        Log.i("Alarm Actual isSet = ", Boolean.toString(alarm.getIsSet()));
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
