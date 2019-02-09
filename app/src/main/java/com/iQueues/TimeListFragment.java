package com.iQueues;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import data.GlobalUtils;


/**
 * The type Time list fragment.
 */
public class TimeListFragment extends ListFragment {


    private ProgressBar progressBar;
    private String date;
    private long currentTimeDate;


    /**
     * The Tag.
     */
    String TAG = "TimeListFragment";
    /**
     * The constant DATA_RECEIVE.
     */
    final static String DATA_RECEIVE = "data_receive";


    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference orderRef = database.collection("orders");

    private ArrayList<TimeListFragment.Time> times = new ArrayList<>();


    /**
     * Instantiates a new Time list fragment.
     */
    public TimeListFragment() {

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

        currentTimeDate = GlobalUtils.getTimeStamp();
    }

    /**
     * The type Time.
     */
    public static class Time {
        /**
         * The Time.
         */
        String time;
        /**
         * The Is available.
         */
        boolean isAvailable = true;

        /**
         * Instantiates a new Time.
         *
         * @param time the time
         */
        Time(String time) {
            this.time = time;
        }

        /**
         * Gets time.
         *
         * @return the time
         */
        String getTime() {
            return time;
        }

        /**
         * Sets time.
         *
         * @param time the time
         */
        public void setTime(String time) {
            this.time = time;
        }
    }


    /**
     * The interface On time list fragment listener.
     */
    public interface OnTimeListFragmentListener {

        /**
         * On list item clicked.
         *
         * @param time the time
         */
        void onListItemClicked(TimeListFragment.Time time);

    }

    /**
     * The Callback.
     */
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progressBar = getActivity().findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        Bundle args = getArguments();
        if (args != null) {
            date = args.getString(DATA_RECEIVE);
        }

        getAvailableTimesFromFireStore(date);
    }

    /**
     * call fire store to check which times can be used.
     * after that add success and fail listeners.
     *
     * @param date
     */
    private void getAvailableTimesFromFireStore(final String date) {

        orderRef.whereEqualTo("date", date)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documentsList = queryDocumentSnapshots.getDocuments();
                        ArrayList<String> notAvailableTImes = new ArrayList<>();
                        long fridayConvertTime = GlobalUtils.convertDateToTimestamp(date, "12:30");

                        for (DocumentSnapshot document : documentsList) {
                            notAvailableTImes.add(document.getString("time"));
                        }
                        //sort the time list from fire store
                        Collections.sort(notAvailableTImes);

                        for (Time time : times) {
                            for (String notAvailableTIme : notAvailableTImes) {
                                if (time.getTime().equals(notAvailableTIme)) { // if time is selected
                                    time.isAvailable = false;
                                }
                            }
                            //convert to time stamp from string
                            String timeOfOrder = time.getTime();
                            long convertDate = GlobalUtils.convertDateToTimestamp(date, timeOfOrder);

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(convertDate);

                            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                            if (currentTimeDate >= convertDate) {// if the time of day passed
                                time.isAvailable = false;
                            } else if (dayOfWeek == Calendar.FRIDAY) {// if the day is friday
                                if (convertDate >= fridayConvertTime) {// after 12:00 isAvailable = false
                                    time.isAvailable = false;
                                }
                            } else if (dayOfWeek == Calendar.SATURDAY) {// if the day is saturday
                                time.isAvailable = false;
                            }
                        }
                        TimeLIstAdapter timeLIstAdapter = new TimeLIstAdapter(times, getActivity());
                        setListAdapter(timeLIstAdapter);

                        progressBar.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e + "getAvailableTimesFromFireStore");
            }
        });
    }


    /**
     * inflate the time list
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.time_fragment, container, false);

    }

    /**
     * call the callback from main activity
     *
     * @param l
     * @param v
     * @param position
     * @param id
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        callback.onListItemClicked(times.get(position));
    }


}
