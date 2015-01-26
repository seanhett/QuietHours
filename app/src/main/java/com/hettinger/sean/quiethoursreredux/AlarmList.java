package com.hettinger.sean.quiethoursreredux;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.HashMap;





public class AlarmList extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;



    HashMap<String, Integer> alarmHashMap;
    ListView alarmList;
    ArrayList<ListViewItem> listViewItems = new ArrayList<ListViewItem>();
    ListViewAdapter adapter;
    DatabaseHandler db;
    Context context;
    Switch listViewSwitch;


    public static AlarmList newInstance(String param1, String param2) {
        AlarmList fragment = new AlarmList();
        Bundle args = new Bundle();

        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AlarmList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        final View rootView = inflater.inflate(R.layout.fragment_alarm_list, container, false);

        alarmHashMap = new HashMap<String, Integer>();
        alarmList = (ListView)rootView.findViewById(R.id.alarmList);
        listViewSwitch = (Switch)rootView.findViewById(R.id.listViewSwitch);
        alarmList.setEmptyView(rootView.findViewById(R.id.emptyView));
        context = getActivity();
        adapter = new ListViewAdapter(context, R.layout.listview_item_row, listViewItems);
        alarmList.setAdapter(adapter);
        db = new DatabaseHandler(getActivity().getApplicationContext());

        loadListView(db.getAllAlarms());


        //Click listener for alarm events, opens up alarm event fragment with current settings to be modified
        alarmList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override


            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long id) {

                        Log.i("Item id clicked is", Long.toString(id));
                        ListViewItem listviewItem = listViewItems.get(position);
                        String value = listviewItem.title;
                        Log.i("Item of value", value + " has been clicked");
                        Log.i("Id is: ", Long.toString(id));
                        int alarmId = alarmHashMap.get(value);
                        Bundle bundle = new Bundle();
                        bundle.putInt("alarmId", alarmId);
                        NewEventFragment newEvent = new NewEventFragment();
                        newEvent.setArguments(bundle);
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.alarmListLayout, newEvent);
                        ft.commit();

                    }
            });


        //Long clicks on alarm events.  Displays alert dialog with the option to delete clicked event
        alarmList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            public boolean onItemLongClick(AdapterView<?> av, View v, int position, long id) {
                Log.i("LongClick", "Activated");
                ListViewItem listviewItem = listViewItems.get(position);
                final String name = listviewItem.title;

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Delete alarm \"" + name + "\"?");

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        db.deleteAlarm(db.getAlarm(alarmHashMap.get(name)));
                        loadListView(db.getAllAlarms());
                        adapter.notifyDataSetChanged();

                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.show();
                return true;
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



    //Loads database of saved alarm events and creates a new listview with each event
    public void loadListView(ArrayList<AlarmEvent> list) {
        Log.i("Alarm LoadListView ", "called");
        if(db != null){
            ArrayList<AlarmEvent> listOfEvents = list;
            if (listViewItems == null)
                listViewItems = new ArrayList<ListViewItem>();
            listViewItems.clear();

            for (AlarmEvent alarm : listOfEvents) {
                Switch listSwitch = new Switch(context);
                listSwitch.setChecked(alarm.getIsSet());
                ListViewItem listViewItem = new ListViewItem(alarm.getName(), listSwitch, alarm.getId());
                listViewItems.add(listViewItem);
                alarmHashMap.put(alarm.getName(), alarm._id);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
