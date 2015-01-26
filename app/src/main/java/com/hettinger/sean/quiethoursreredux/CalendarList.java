package com.hettinger.sean.quiethoursreredux;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;



/**
 * The CalendarList Fragment is used to display events retreived from the user's calendar and displays the events in a listview using the CalendarListViewAdapter.
 * The user can set the phone on silent for each event available
 */
public class CalendarList extends Fragment implements LoaderManager.LoaderCallbacks<Object> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    static CalendarReader reader;

    CalendarDatabaseHandler cdb;
    ArrayList<ListViewItem> listViewItems = new ArrayList<ListViewItem>();
    CalendarListViewAdapter adapter;
    HashMap<String, Integer> calendarHashMap;
    Context context;
    ListView calendarList;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarList.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarList newInstance(String param1, String param2) {
        CalendarList fragment = new CalendarList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CalendarList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("CalendarList", "onCreate called");
        super.onCreate(savedInstanceState);
        cdb = new CalendarDatabaseHandler(getActivity().getApplicationContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("CalendarList", "onCreateView called");
        // Inflate the layout for this fragment




        reader = new CalendarReader();

        View rootView = inflater.inflate(R.layout.fragment_calendar_list, container, false);
        final TextView textView = (TextView)rootView.findViewById(R.id.calendarEmptyView);

        calendarList = (ListView)rootView.findViewById(R.id.calendarList);
        calendarList.setEmptyView(rootView.findViewById(R.id.calendarEmptyView));

        context = getActivity();

        adapter = new CalendarListViewAdapter(context, R.layout.listview_item_row, listViewItems);
        calendarList.setAdapter(adapter);

        calendarHashMap = new HashMap<String, Integer>();

        readCalendarEvents(context);  //Calendar Events are read
        loadListView(cdb.getAllCalendarEvents()); //Calendar Events are displayed

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public Loader<Object> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Object> objectLoader, Object o) {
    }

    @Override
    public void onLoaderReset(Loader<Object> objectLoader) {
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void onDestroyView() {
        Log.i("CalendarList", "onDestroyView called");
        ViewGroup mContainer = (ViewGroup) getView().findViewById(R.id.calendarListLayout);
        mContainer.removeAllViews();
        super.onDestroyView();
    }




    //
    public void loadListView(ArrayList<CalendarEvent> list) {


        Log.i("CalendarList", "loadListView called");

        if(cdb!=null){
            ArrayList<CalendarEvent> listOfEvents = list;
            if(listViewItems == null)
                listViewItems = new ArrayList<ListViewItem>();
            listViewItems.clear();

        for (CalendarEvent event : listOfEvents) {
            Switch listSwitch = new Switch(context);
            listSwitch.setChecked(event.getIsSet());
            ListViewItem listViewItem = new ListViewItem(event.getName(),listSwitch, event.getId());
            listViewItems.add(listViewItem);
            calendarHashMap.put(event.getName(), event._id);
            adapter.notifyDataSetChanged();
            }
        }
    }

    public static void readCalendarEvents(Context context) {
        Log.i("CalendarList", "readCalendarEvents called");
        reader.readCalendarEvent(context);
    }
}







