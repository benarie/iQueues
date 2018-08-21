package com.iQueues;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TimeListFragment extends ListFragment {

    String[] timePerDay = {"08:00 - 08:30", "08:30 - 09:00",
            "09:00 - 09:30", "09:30 - 10:00",
            "10:00 - 10:30", "10:00 - 10:30",
            "10:30 - 11:00", "11:00 - 11:30",
            "11:30 - 12:00", "12:00 - 12:30",
            "12:30 - 13:00", "13:00 - 13:30",
            "13:30 - 14:00", "14:00 - 14:30",
            "14:30 - 15:00", "15:00 - 15:30",
            "15:30 - 16:00", "16:30 - 17:00"};

    public interface OnTimeListFragmentListener {

        void onListItemClicked(String time);
    }

    OnTimeListFragmentListener callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity = (Activity) context;
        try {
            callback = (OnTimeListFragmentListener) activity;
        } catch (ClassCastException ex) {
            throw new ClassCastException(activity.toString() + "must implement the OnTimeListFragmentListener interface ");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //get the list from activity
      //  ArrayList<String> timeByDateList = getArguments().getStringArrayList("ArrayList");

        View v = inflater.inflate(R.layout.time_fragment, container, false);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, timePerDay);
        setListAdapter(adapter);

        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        callback.onListItemClicked(timePerDay[position]);

    }

}
