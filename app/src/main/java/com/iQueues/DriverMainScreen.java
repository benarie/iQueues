package com.iQueues;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

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
    final String TAG = "DriverMainScreen";
    final String DATE_TAG = "date";
    final String TIME_TAG = "time";
    final String STATUS_TAG = "status";


    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference queueRef = database.collection("queue");
    private CollectionReference userRef = database.collection("users");


    public ArrayList<String> timeByDateList = new ArrayList<>();

    Queue queue = new Queue();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);

        timeTv = findViewById(R.id.time_text_output);

        checkTime(); // check time in day

        nameTv = findViewById(R.id.name_text_output);

       /* final ArrayList<Queue> queue_cell = new ArrayList<>();
        queue_cell.add(new Queue());*/

        dateTv = findViewById(R.id.date_text_view);
        timeTv = findViewById(R.id.time_text_view);
        baseTv = findViewById(R.id.no_queue_text_view);

        editedQueueBtn = findViewById(R.id.edited_btn);
        deleteQueueBtn = findViewById(R.id.delete_btn);
        insertQueueBtn = findViewById(R.id.insert_queue_btn);
        targetBtn = findViewById(R.id.target_btn);
        cardView = findViewById(R.id.card);

        //READ DATA FROM FIREBASE
       /* queueRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                queue_cell.clear();
                if (dataSnapshot.exists()) {
                    queue = dataSnapshot.getValue(Queue.class);
                    queue_cell.add(queue);

                    if (queue.getStatus().equalsIgnoreCase("true")) {

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

                } else baseTv.setText("אין לך תור כרגע");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
*/

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


        //send list to the timeListFragment
        Bundle args = new Bundle();
        args.putStringArrayList("ArrayList", timeByDateList);
        TimeListFragment timeListFragment = new TimeListFragment();
        timeListFragment.setArguments(args);

        //replace between fragments
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

        Map<String,Object> data = new HashMap<>();
        data.put(DATE_TAG,queue.getDate());
        data.put(TIME_TAG,queue.getTime());
        data.put(STATUS_TAG,queue.getStatus());


        queueRef.document(auth.getCurrentUser().getUid()).set(data);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = auth.getCurrentUser();
        getUserDetails(user);
    }

/////////////

    public void getUserDetails(FirebaseUser user) {

        if (user != null) {

            String name = user.getDisplayName();
            nameTv.setText(name);
        }
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

    @Override
    public void checkTimeByDate(String date) {

        final String picDate = date;

       /* queueRef.orderByChild("queue").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String chosenDate = snapshot.child("date").getValue(String.class);
                        String chosenTime = snapshot.child("time").getValue(String.class);
                        if (picDate.equals(chosenDate)) {

                            timeByDateList.add(chosenTime);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

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

//get user name frome fiebase
    /*    userRef.document(auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                String name = document.getString("full_name");
                                nameTv.setText(name);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });*/




