package com.iQueues;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DriverMainScreen extends AppCompatActivity implements DateFragment.OnQueueFragmentListener, TimeListFragment.OnTimeListFragmentListener {

    TextView nameTv;
    TextView timeTv;
    TextView dateTv;
    TextView baseTv;
    Button insertQueueBtn;
    Button deleteQueueBtn;
    Button editedQueueBtn;
    Button targetBtn;
    CardView cardView;


    final String DATE_FRAGMENT_TAG = "date_fragment";
    final String TIME_LIST_FRAGMENT_TAG = "time_list_fragment";


    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener;


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference queueRef = database.getReference("queue");
    private DatabaseReference userRef = database.getReference("users");

    FirebaseUser user = auth.getCurrentUser();

    Queue queue = new Queue();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);

        timeTv = findViewById(R.id.time_text_output);
        checkTime(); // check time in day

        nameTv = findViewById(R.id.name_text_output);
        getUserDetails(); // get name

        final ArrayList<Queue> queue_cell = new ArrayList<>();
        queue_cell.add(new Queue());

        dateTv = findViewById(R.id.date_text_view);
        timeTv = findViewById(R.id.time_text_view);
        baseTv = findViewById(R.id.no_queue_text_view);

        editedQueueBtn = findViewById(R.id.edited_btn);
        deleteQueueBtn = findViewById(R.id.delete_btn);
        insertQueueBtn = findViewById(R.id.insert_queue_btn);
        targetBtn = findViewById(R.id.target_btn);
        cardView = findViewById(R.id.card);

        //READ DATA FROM FIREBASE
        queueRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                queue_cell.clear();
                if (dataSnapshot.exists()) {
                    Queue queue = dataSnapshot.getValue(Queue.class);

                    queue_cell.add(queue);

                    if (queue.getStatus() != null && queue.getStatus().equalsIgnoreCase("false")) {

                        baseTv.setVisibility(View.GONE);
                        dateTv.setVisibility(View.VISIBLE);
                        timeTv.setVisibility(View.VISIBLE);
                        dateTv.setText(queue.getDate());
                        timeTv.setText(queue.getTime());

                        insertQueueBtn.setVisibility(View.GONE);
                        editedQueueBtn.setVisibility(View.VISIBLE);
                        deleteQueueBtn.setVisibility(View.VISIBLE);
                        targetBtn.setVisibility(View.VISIBLE);
                    } else {

                        baseTv.setVisibility(View.VISIBLE);
                        dateTv.setVisibility(View.GONE);
                        timeTv.setVisibility(View.GONE);
                        baseTv.setText("אין לך תור כרגע");

                        insertQueueBtn.setVisibility(View.VISIBLE);
                        editedQueueBtn.setVisibility(View.GONE);
                        deleteQueueBtn.setVisibility(View.GONE);
                        targetBtn.setVisibility(View.GONE);

                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });




        insertQueueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.fragment_container, new DateFragment(), DATE_FRAGMENT_TAG);
                transaction.addToBackStack(null).commit();

            }
        });


    }

    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }


    /////////////
    @Override
    public void onConfirmBtnClicked(String date) {

        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new TimeListFragment(), TIME_LIST_FRAGMENT_TAG)
                .addToBackStack(null).commit();

        Fragment fragment = getFragmentManager().findFragmentByTag(DATE_FRAGMENT_TAG);
        getFragmentManager().beginTransaction().remove(fragment).commit();


        queue.setDate(date);

    }

    /////////////
    @Override
    public void onDeleteBtnClicked() {

        Fragment fragment = getFragmentManager().findFragmentByTag(DATE_FRAGMENT_TAG);
        getFragmentManager().beginTransaction().remove(fragment).commit();
    }

    @Override
    public void onListItemClicked(String time) {


        Fragment fragment = getFragmentManager().findFragmentByTag(TIME_LIST_FRAGMENT_TAG);
        getFragmentManager().beginTransaction().remove(fragment).commit();

        queue.setTime(time);
        queue.setStatus("true");

        queueRef.child(auth.getCurrentUser().getUid()).setValue(queue);

    }


    public void getUserDetails() {

        userRef.orderByChild("full_Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("full_Name").getValue(String.class);
                    nameTv.setText(name);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void checkTime() {

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if (hour >= 0 && hour <= 12)
            timeTv.setText("בוקר טוב");
        else if (hour > 12 && hour <= 17)
            timeTv.setText("צהריים טובים");
        else if (hour > 17 && hour <= 20)
            timeTv.setText("ערב טוב");
        else if (hour > 20 && hour < 23)
            timeTv.setText("לילה טוב");
    }
}

// use in recycler view

    /*final RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(DriverMainScreen.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        final QueueAdapter adapter = new QueueAdapter(queue_cell);
        recyclerView.setAdapter(adapter);


        adapter.notifyDataSetChanged();*/


