package com.iQueues;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DriverMainScreen extends AppCompatActivity implements InsertQueueFragment.OnQueueFragmentListener {

    private String date, time;
    String noQueue = "אין לך תור כרגע";
    boolean isQueue = false;
    TextView nameTv;
    Button insertQueueBtn;
    final String DATE_FRAGMENT_TAG = "date_fragment";

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener;


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userRrf = database.getReference("users");

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

        nameTv = findViewById(R.id.name_text_output);

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {

            nameTv.setText(user.getDisplayName());
        } else {
            nameTv.setText("שלום");
        }


        ArrayList<Queue> queue_per_day = new ArrayList<>();
        queue_per_day.add(new Queue(noQueue));

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        QueueAdapter adapter = new QueueAdapter(queue_per_day);
        recyclerView.setAdapter(adapter);


        insertQueueBtn = findViewById(R.id.insert_queue_btn);
        insertQueueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.fragment_container, new InsertQueueFragment(), DATE_FRAGMENT_TAG);
                transaction.commit();
            }
        });


    }

    @Override
    public void onDateBtnClicked(String date) {

        Fragment fragment = getFragmentManager().findFragmentByTag(DATE_FRAGMENT_TAG);
        getFragmentManager().beginTransaction().remove(fragment).commit();
    }

    @Override
    public void onDeleteBtnClicked() {

        Fragment fragment = getFragmentManager().findFragmentByTag(DATE_FRAGMENT_TAG);
        getFragmentManager().beginTransaction().remove(fragment).commit();
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