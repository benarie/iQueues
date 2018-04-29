package com.iQueues;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class DriverMainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);

    /*    ArrayList<String> time_per_day  = new ArrayList<>();
        time_per_day.add("08:00 - 08:30");
        time_per_day.add("08:30 - 09:00");
        time_per_day.add("09:00 - 09:30");
        time_per_day.add("09:30 - 10:00");
        time_per_day.add("10:00 - 10:30");
        time_per_day.add("10:30 - 11:00");
        time_per_day.add("11:00 - 11:30");
        time_per_day.add("11:30 - 12:00");
        time_per_day.add("12:00 - 12:30");
        time_per_day.add("12:30 - 13:00");
        time_per_day.add("13:00 - 13:30");
        time_per_day.add("13:30 - 14:00");
        time_per_day.add("14:00 - 14:30");
        time_per_day.add("14:30 - 15:00");
        time_per_day.add("15:00 - 15:30");
        time_per_day.add("15:30 - 16:00");
        time_per_day.add("16:30 - 17:00");*/

        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        String date = "בתאריך: " + dayOfWeek + "/" + month + "/" + year;
        String time = "בשעה: " + hourOfDay + ":" + minutes;
        String noQueue = "אין לך תור כרגע";
        boolean isQueue = false;

        ArrayList<Queue> queue_per_day = new ArrayList<>();
        queue_per_day.add(new Queue(date, time, isQueue));

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        QueueAdapter adapter = new QueueAdapter(queue_per_day);
        recyclerView.setAdapter(adapter);


    }
}


/*               progressDialog = new ProgressDialog(SignInProcess.this);
                    progressDialog.setMessage("loading" + user.getDisplayName() + "queue, please wait");
                    progressDialog.show();

                    queueRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            queues.clear();
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    queue = snapshot.getValue(Queue.class);
                                    queues.add(queue);
                                }
                                adapter.notifyDataSetChanged();
                            }
                            //progressDialog.dismiss();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {


                        }
                    });*/