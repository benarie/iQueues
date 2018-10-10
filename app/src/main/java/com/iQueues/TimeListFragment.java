package com.iQueues;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class TimeListFragment extends ListFragment {

    public class Time {
        String time;
        boolean isAvailable = true;

        private Time(String time) {
            this.time = time;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    private ProgressBar progressBar;
    private String date;
    private ListView listView;

    String TAG = "TimeListFragment";
    final static String DATA_RECEIVE = "data_receive";


    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference orderRef = database.collection("orders");

    private ArrayList<TimeListFragment.Time> times = new ArrayList<>();

    public interface OnTimeListFragmentListener {

        void onListItemClicked(String time);

    }

    OnTimeListFragmentListener callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        times.add(new TimeListFragment.Time("08:00 - 08:30"));
        times.add(new TimeListFragment.Time("08:30 - 09:00"));
        times.add(new TimeListFragment.Time("09:00 - 09:30"));
        times.add(new TimeListFragment.Time("09:30 - 10:00"));
        times.add(new TimeListFragment.Time("10:00 - 10:30"));
        times.add(new TimeListFragment.Time("10:30 - 11:00"));
        times.add(new TimeListFragment.Time("11:00 - 11:30"));
        times.add(new TimeListFragment.Time("11:30 - 12:00"));
        times.add(new TimeListFragment.Time("12:00 - 12:30"));
        times.add(new TimeListFragment.Time("12:30 - 13:00"));
        times.add(new TimeListFragment.Time("13:00 - 13:30"));
        times.add(new TimeListFragment.Time("13:00 - 13:30"));
        times.add(new TimeListFragment.Time("13:30 - 14:00"));
        times.add(new TimeListFragment.Time("14:00 - 14:30"));
        times.add(new TimeListFragment.Time("14:30 - 15:00"));
        times.add(new TimeListFragment.Time("15:00 - 15:30"));
        times.add(new TimeListFragment.Time("15:30 - 16:00"));
        times.add(new TimeListFragment.Time("16:00 - 16:30"));
        times.add(new TimeListFragment.Time("16:30 - 17:00"));


        Activity activity = (Activity) context;
        try {
            callback = (OnTimeListFragmentListener) activity;
        } catch (ClassCastException ex) {
            throw new ClassCastException(activity.toString() + "must implement the OnTimeListFragmentListener interface ");
        }

        // show loading spinner here

       /* progressBar = new ProgressBar(context);
        progressBar = getView().findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
*/

        getAvailableTimesFromFireStore();

        /*  progressBar.setVisibility(View.GONE);*/


    }

    private void getAvailableTimesFromFireStore() {
        /* call firebase here to check which times can be used.
        after that add success and fail listeners.
        don't forget to remove the loading spinner inside the listeners :) */

        Bundle args = getArguments();
        if (args != null) {
            date = args.getString(DATA_RECEIVE);
        }

        orderRef.whereEqualTo("status", "active")
                .whereEqualTo("date", date)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documentsList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot document : documentsList) {
                            for (TimeListFragment.Time availableTimes : times) {
                                if (availableTimes.getTime().equals(document.getString("time"))) {
                                    availableTimes.isAvailable = false;
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e + "getAvailableTimesFromFireStore");
            }
        });
    }


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.time_fragment, container, false);
        TimeLIstAdapter timeLIstAdapter = new TimeLIstAdapter(times, getContext());
        setListAdapter(timeLIstAdapter);

        return v;

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        callback.onListItemClicked(times.get(position).getTime());
    }


}
