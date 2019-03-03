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

/**
 * The type Date fragment.
 */
public class DateFragment extends Fragment {

    private String picDate;
    /**
     * The Tag.
     */
    String TAG = "date_tag";

    /**
     * The interface On queue fragment listener.
     */
    public interface OnQueueFragmentListener {

        /**
         * On confirm date btn clicked.
         *
         * @param date the date
         */
        void onConfirmDateBtnClicked(String date);

        /**
         * On delete date btn clicked.
         */
        void onDeleteDateBtnClicked();
    }

    /**
     * The Call back.
     */
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

    /**
     * Inflate the date fragment
     * Check if work hours are over (after 17:00)
     * Check if work hours are over in friday (after 12:00)
     * pic date
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return viewGroup
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.date_fragment, container, false);
        final CalendarView calendarView = viewGroup.findViewById(R.id.calendar);

        calendarView.setMinDate(System.currentTimeMillis() - 1000);

        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int currentDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        final int dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        final int month = Calendar.getInstance().get(Calendar.MONTH);
        final int year = Calendar.getInstance().get(Calendar.YEAR);


        if (dayOfMonth == currentDate && hour >= 17) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            calendarView.setMinDate(calendar.getTimeInMillis());
        } else if (dayOfWeek == Calendar.FRIDAY && hour >= 12) {
            calendar.add(Calendar.DAY_OF_YEAR, 2);
            calendarView.setMinDate(calendar.getTimeInMillis());

        } else {
            picDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            Toast.makeText(calendarView.getContext(), "התאריך שנבחר: " + picDate, Toast.LENGTH_SHORT).show();
            Log.d(TAG, picDate);
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                picDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                Toast.makeText(calendarView.getContext(), "התאריך שנבחר: " + picDate, Toast.LENGTH_SHORT).show();
                Log.d(TAG, picDate);
            }
        });

        TextView okDateBtn = viewGroup.findViewById(R.id.ok_date_btn);
        okDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (picDate != null) {

                    callBack.onConfirmDateBtnClicked(picDate);
                } else {
                    Toast.makeText(calendarView.getContext(), "אין תורים להיום", Toast.LENGTH_SHORT).show();
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
