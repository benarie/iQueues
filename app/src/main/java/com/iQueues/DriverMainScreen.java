package com.iQueues;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import data.GlobalUtils;
import data.Globals;

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
    ProgressDialog progressDialog;


    final String DATE_FRAGMENT_TAG = "date_fragment";
    final String TIME_LIST_FRAGMENT_TAG = "time_list_fragment";
    final String TAG = "DriverMainScreen";
    final String DATE_TAG = "date";
    final String TIME_TAG = "time";
    final String KEY_NAME = "name";


    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference orderRef = database.collection("orders");


    Order order = new Order();
    UserDetails userDetails = UserDetails.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);

        timeTv = findViewById(R.id.time_text_output);

        checkTime(); // check time in day

        nameTv = findViewById(R.id.name_text_output);
        dateTv = findViewById(R.id.date_text_view);
        timeTv = findViewById(R.id.time_text_view);
        baseTv = findViewById(R.id.no_queue_text_view);

        editedQueueBtn = findViewById(R.id.edited_btn);
        deleteQueueBtn = findViewById(R.id.delete_btn);
        insertQueueBtn = findViewById(R.id.insert_queue_btn);
        targetBtn = findViewById(R.id.target_btn);
        cardView = findViewById(R.id.queue_cell);


        String uid = GlobalUtils.getStringFromLocalStorage(DriverMainScreen.this,Globals.UID_LOCAL_STORAGE_KEY);
        pullDataOfOrderFromFireStore(uid);

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

        //replace between fragments
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new TimeListFragment(), TIME_LIST_FRAGMENT_TAG)
                .addToBackStack(null).commit();

        Fragment fragment = getFragmentManager().findFragmentByTag(DATE_FRAGMENT_TAG);
        getFragmentManager().beginTransaction().remove(fragment).commit();

        order.setDate(date);

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

        order.setTime(time);

        pushOrderDataToDataBase();

    }

    @Override
    protected void onStart() {
        super.onStart();

        getUserDetails();
    }

/////////////

    public void getUserDetails() {

        String name = GlobalUtils.getStringFromLocalStorage(this, Globals.FULL_NAME_LOCAL_STORAGE_KEY);

        nameTv.setText(name);

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

    private void pushOrderDataToDataBase() {

        String date = order.getDate();
        String time = order.getTime();
        String uid = GlobalUtils.getStringFromLocalStorage(DriverMainScreen.this, Globals.UID_LOCAL_STORAGE_KEY);
        String status = Globals.ACTIVE_ORDER_STATUS;
        String orderId = orderRef.document().getId();

        Order order = new Order(orderId, date, time, uid, status);

        orderRef.document(orderId)
                .set(order)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DriverMainScreen.this, "order saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DriverMainScreen.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });

        /*// Atomically add a new orderId to the "orderIds" array field.
        DocumentReference userRef = database.collection("users").document(uid);
        userRef.update("idQueue", FieldValue.arrayUnion(orderId));*/
    }

    private void pullDataOfOrderFromFireStore(String uid) {

        orderRef.whereEqualTo("uid", uid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot single : list) {
                     OrdersQueue.getInstance().add(single.toObject(Order.class));
                     order = single.toObject(Order.class);

                    afterGetOrderData();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e + "pullDataOfOrderFromFireStore");
            }
        });

    }

    private void afterGetOrderData() {

        if (OrdersQueue.getInstance().getActive() != null) {

            dateTv.setText(order.getDate());
            timeTv.setText(order.getTime());

            baseTv.setVisibility(View.GONE);
            dateTv.setVisibility(View.VISIBLE);
            timeTv.setVisibility(View.VISIBLE);
            dateTv.setText(order.getDate());
            timeTv.setText(order.getTime());

            insertQueueBtn.setVisibility(View.GONE);
            editedQueueBtn.setVisibility(View.VISIBLE);
            deleteQueueBtn.setVisibility(View.VISIBLE);
            targetBtn.setVisibility(View.VISIBLE);

        } else {

            baseTv.setVisibility(View.VISIBLE);
            dateTv.setVisibility(View.GONE);
            timeTv.setVisibility(View.GONE);
            baseTv.setText(R.string.note);

            insertQueueBtn.setVisibility(View.VISIBLE);
            editedQueueBtn.setVisibility(View.GONE);
            deleteQueueBtn.setVisibility(View.GONE);
            targetBtn.setVisibility(View.GONE);

        }
    }
}
