package com.iQueues;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.WidgetContainer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import java.util.Calendar;

public class InsertQueueFragment extends Fragment {

    String date;
    String TAG = "date_tag";

    public interface OnQueueFragmentListener {

        void onDateBtnClicked(String date);

        void onDeleteBtnClicked();
    }

    OnQueueFragmentListener callBack;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity = (Activity) context;
        try {
            callBack = (OnQueueFragmentListener) activity;
        } catch (ClassCastException ex) {
            throw new ClassCastException(activity.toString() + "Must implement OnQueueFragmentListener interface");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.date_fragment, container, false);

        final CalendarView calendarView = viewGroup.findViewById(R.id.calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = dayOfMonth + "/" + month + "/" + year;
                Log.d(TAG, date);
            }
        });

        TextView okDateBtn = viewGroup.findViewById(R.id.ok_date_btn);
        okDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onDateBtnClicked(date);
            }
        });

        TextView cancelDateBtn = viewGroup.findViewById(R.id.cancel_date_btn);
        cancelDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onDeleteBtnClicked();
            }
        });

        return viewGroup;
    }
}
