package com.iQueues;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
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
    Button insertQueueBtn;
    Boolean isActive = false;

    final String DATE_FRAGMENT_TAG = "date_fragment";
    final String TIME_LIST_FRAGMENT_TAG = "time_list_fragment";


    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener;


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference queueRef = database.getReference("queue");
    private DatabaseReference userRef = database.getReference("users");

    FirebaseUser user = auth.getCurrentUser();

    List<Queue> queues = new ArrayList<>();

    Queue queue = new Queue();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);

        timeTv = findViewById(R.id.time_text_output);
        checkTime(); // check time in day

        nameTv = findViewById(R.id.name_text_output);
        getUserDetails(); // get name

        ArrayList<Queue> queue_per_day = new ArrayList<>();
        queue_per_day.add(new Queue());

        final RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(DriverMainScreen.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        final QueueAdapter adapter = new QueueAdapter(queue_per_day);
        recyclerView.setAdapter(adapter);

        //READ DATA FROM FIREBASE
        queueRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                queues.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Queue queue = snapshot.getValue(Queue.class);
                        queues.add(queue);

                        if (queue == null){

                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                 /*   adapter.getItemId(R.id.no_queue_text_view);
                    recyclerView.setVisibility(View.INVISIBLE);
                    adapter.getItemId(R.id.date_text_view);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.getItemId(R.id.time_text_view);
                    recyclerView.setVisibility(View.VISIBLE);

                    recyclerView.setAdapter(adapter);*/


                   /* queue = new Queue();
                    queueRef.child(auth.getCurrentUser().getUid()).setValue(queue);*/

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        insertQueueBtn = findViewById(R.id.insert_queue_btn);
        insertQueueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //
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
        queue.setStatus(true);

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

/*    public void checkStatus() {

        queueRef.orderByChild("status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String status = snapshot.child("status").getValue(String.class);

                    if (status.equals("true")) {
                        isActive = true;
                    } else
                        isActive = false;
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }*/

}






