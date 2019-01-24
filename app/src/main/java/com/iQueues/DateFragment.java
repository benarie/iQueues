package com.iQueues;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class DateFragment extends Fragment {

    private String picDate;
    String TAG = "date_tag";

    public interface OnQueueFragmentListener {

        void onConfirmDateBtnClicked(String date);

        void onDeleteDateBtnClicked();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.date_fragment, container, false);
        final CalendarView calendarView = viewGroup.findViewById(R.id.calendar);

        calendarView.setMinDate(System.currentTimeMillis() - 1000);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();

                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                int currentDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);


                if (dayOfMonth == currentDate && hour >= 17) {
                    Toast.makeText(calendarView.getContext(), "אין אפשרות להזמין תור!", Toast.LENGTH_SHORT).show();
                } else {

                    picDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    Log.d(TAG, picDate);
                }

            }
        });

        TextView okDateBtn = viewGroup.findViewById(R.id.ok_date_btn);
        okDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (picDate != null) {

                    callBack.onConfirmDateBtnClicked(picDate);
                } else {
                    Toast.makeText(calendarView.getContext(), "בחר יום", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView cancelDateBtn = viewGroup.findViewById(R.id.cancel_date_btn);
        cancelDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onDeleteDateBtnClicked();
            }
        });

        return viewGroup;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
