package com.iQueues;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * The type Queue adapter.
 */
public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.QueueViewHolder> {

    private ArrayList<Order> orders;

    /**
     * Instantiates a new Queue adapter.
     *
     * @param orders the orders
     */
    public QueueAdapter(ArrayList<Order> orders) {
        this.orders = orders;
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    /**
     * The type Queue view holder.
     */
    public class QueueViewHolder extends RecyclerView.ViewHolder {

        /**
         * The Date tv.
         */
        TextView dateTv;
        /**
         * The Time tv.
         */
        TextView timeTv;


        /**
         * Instantiates a new Queue view holder.
         *
         * @param itemView the item view
         */
        public QueueViewHolder(View itemView) {
            super(itemView);
            dateTv = itemView.findViewById(R.id.date_text_view);
            timeTv = itemView.findViewById(R.id.time_text_view);

        }

    }

    @NonNull
    @Override
    public QueueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // no need check the view type we have only one kind of call layout;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.queue_cell, parent, false);
        QueueViewHolder queueViewHolder = new QueueViewHolder(v);
        return queueViewHolder;
    }

    /**
     * get the position of order
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull QueueViewHolder holder, int position) {

        Order order = orders.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}
