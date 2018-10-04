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
import android.widget.ProgressBar;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import data.GlobalUtils;
import data.Globals;


public class TimeListFragment extends ListFragment {

    ProgressBar progressBar;

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference orderRef = database.collection("orders");

    private final String[] timePerDay = {"08:00 - 08:30", "08:30 - 09:00",
            "09:00 - 09:30", "09:30 - 10:00",
            "10:00 - 10:30", "10:00 - 10:30",
            "10:30 - 11:00", "11:00 - 11:30",
            "11:30 - 12:00", "12:00 - 12:30",
            "12:30 - 13:00", "13:00 - 13:30",
            "13:30 - 14:00", "14:00 - 14:30",
            "14:30 - 15:00", "15:00 - 15:30",
            "15:30 - 16:00", "16:30 - 17:00"};

    private ArrayList<String> availableTimes = new ArrayList<>();

    public interface OnTimeListFragmentListener {

        void onListItemClicked(String time);

    }

    OnTimeListFragmentListener callback;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        
        for(String s : timePerDay) availableTimes.add(s);

        Activity activity = (Activity) context;
        try {
            callback = (OnTimeListFragmentListener) activity;
        } catch (ClassCastException ex) {
            throw new ClassCastException(activity.toString() + "must implement the OnTimeListFragmentListener interface ");
        }

        // show loading spinner here
        progressBar =activity.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        getAvailableTimesFromFirebase();

    }

    private void getAvailableTimesFromFirebase() {
        /* call firebase here to check which times can be used.
        after that add success and fail listeners.
        don't forget to remove the loading spinner inside the listeners :) */


        progressBar.setVisibility(View.GONE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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
