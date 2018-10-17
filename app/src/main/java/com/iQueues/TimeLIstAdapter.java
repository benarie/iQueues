package com.iQueues;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class TimeLIstAdapter extends BaseAdapter {

    private ArrayList<TimeListFragment.Time> times ;

    private Context context;

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
        long tStart = System.currentTimeMillis();
//        if (convertView == null) {// no suitable view in the listView recycler view is found.

        convertView = LayoutInflater.from(context).inflate(R.layout.time_cell, null);
//        }

        TextView orderTimeTv = convertView.findViewById(R.id.order_time_text_view);

        TimeListFragment.Time time = times.get(position);

        orderTimeTv.setText(time.getTime());

        if (!time.isAvailable) {
            convertView.setAlpha(0.5f);
        }else{
            convertView.setAlpha(1f);
        }
        System.out.printf("AVIHU: %s \n", (System.currentTimeMillis() - tStart));
        return convertView;
    }
}
