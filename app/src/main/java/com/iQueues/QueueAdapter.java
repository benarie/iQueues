package com.iQueues;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.QueueViewHolder> {

    private List<Queue> queues;

    public QueueAdapter(List<Queue> queues) {
        this.queues = queues;
    }

    @Override
    public int getItemCount() {
        return queues.size();
    }

    public class QueueViewHolder extends RecyclerView.ViewHolder {

        TextView dateTv;
        TextView timeTv;
        TextView noQueueTv;

        public QueueViewHolder(View itemView) {
            super(itemView);
            dateTv = itemView.findViewById(R.id.date_text_view);
            timeTv = itemView.findViewById(R.id.time_text_view);
            noQueueTv = itemView.findViewById(R.id.no_queue_text_view);
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

    @Override
    public void onBindViewHolder(@NonNull QueueViewHolder holder, int position) {

        Queue queue = queues.get(position);
        holder.dateTv.setText(queue.getDate());
        holder.timeTv.setText(queue.getTime());
        holder.noQueueTv.setText("אין לך תור כרגע");

    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}
