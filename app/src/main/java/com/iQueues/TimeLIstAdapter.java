package com.iQueues;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * The type Time list adapter.
 */
public class TimeLIstAdapter extends BaseAdapter {

    private ArrayList<TimeListFragment.Time> times;
    private Context context;

    /**
     * Instantiates a new Time list adapter.
     *
     * @param times   the times
     * @param context the context
     */
    public TimeLIstAdapter(ArrayList<TimeListFragment.Time> times, Context context) {
        this.times = times;
        this.context = context;
    }

    @Override
    public int getCount() {
        return times.size();
    }

    @Override
    public Object getItem(int position) {
        return times.get(position);
    }

    @Override
    public boolean isEmpty() {
        return times.isEmpty();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.time_cell, null);

        TextView orderTimeTv = convertView.findViewById(R.id.order_time_text_view);
        TimeListFragment.Time time = times.get(position);

        orderTimeTv.setText(time.getTime());

        if (!time.isAvailable) {
            convertView.setAlpha(0.2f);
        } else {
            convertView.setAlpha(1f);
        }
        return convertView;
    }
}
