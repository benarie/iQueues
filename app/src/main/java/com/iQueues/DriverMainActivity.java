package com.iQueues;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import data.GlobalUtils;
import data.Globals;
import data.ViewManager;

public class DriverMainActivity extends AppCompatActivity implements DateFragment.OnQueueFragmentListener, TimeListFragment.OnTimeListFragmentListener {
    ViewManager viewManager;
    TextView nameTv;
    TextView timeTv;
    TextView dateTv;
    TextView baseTv;
    TextView timeLeftTv_1;
    TextView timeLeftTv;
    TextView firstTv;
    Button insertQueueBtn;
    ImageButton deleteQueueBtn;
    ImageButton changeQueueBtn;
    Button directionBtn;
    ImageButton powerBtn;
    CardView cardView;
    CountDownTimer countDownTimer;
    AlarmManager manager;
    ProgressBar progressBar;

    final String DATE_FRAGMENT_TAG = "date_fragment";
    final String TIME_LIST_FRAGMENT_TAG = "time_list_fragment";
    final String TAG = "DriverMainActivity";
    final String TOTAL_TIME = "TOTAL_TIME";

    private String dateOfOrder;
    private String timeOfOrder;
    private Long currentTimeDate;
    private Long convertDate;
    private Long totalTime;
    private Long hoursToLeft;
    private Long minutesToLeft;
    private Long secondsToLeft;

    boolean orderFlag = true;

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference orderRef = database.collection("orders");

    Order order = new Order();


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
        timeLeftTv_1 = findViewById(R.id.time_left_text_view_1);
        timeLeftTv = findViewById(R.id.time_left_text_view);
        firstTv = findViewById(R.id.first_text_view);

        changeQueueBtn = findViewById(R.id.change_btn);
        deleteQueueBtn = findViewById(R.id.delete_btn);
        insertQueueBtn = findViewById(R.id.insert_queue_btn);
        directionBtn = findViewById(R.id.direction_btn);
        powerBtn = findViewById(R.id.btn_power);
        cardView = findViewById(R.id.queue_cell);
        progressBar = findViewById(R.id.main_progress_bar);

        viewManager = new ViewManager(firstTv, timeTv, dateTv, baseTv, timeLeftTv_1, timeLeftTv, insertQueueBtn, deleteQueueBtn, changeQueueBtn, directionBtn);

        currentTimeDate = GlobalUtils.getTimeStamp();

        String uid = GlobalUtils.getStringFromLocalStorage(DriverMainActivity.this, Globals.UID_LOCAL_STORAGE_KEY);
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

        logoutFromMainActivity();

        deleteQueueOnClick();

        changeQueueOnClick();

        directionOnClick();

    }

    private void logoutFromMainActivity() {

        powerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalUtils.cleanUserDataFromMemory(DriverMainActivity.this);

                Intent intent = new Intent(DriverMainActivity.this, SignInProcess.class);
                startActivity(intent);
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
    public void onConfirmDateBtnClicked(String date) {

        order.setDate(date);

        TimeListFragment tlf = new TimeListFragment();
        Bundle args = new Bundle();
        args.putString(TimeListFragment.DATA_RECEIVE, date);
        tlf.setArguments(args);

        //replace between fragments
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, tlf, TIME_LIST_FRAGMENT_TAG)
                .addToBackStack(null).commit();

        Fragment fragment = getFragmentManager().findFragmentByTag(DATE_FRAGMENT_TAG);
        getFragmentManager().beginTransaction().remove(fragment).commit();
    }

    /////////////
    @Override
    public void onDeleteDateBtnClicked() {

        Fragment fragment = getFragmentManager().findFragmentByTag(DATE_FRAGMENT_TAG);
        getFragmentManager().beginTransaction().remove(fragment).commit();
    }

    @Override
    public void onListItemClicked(final TimeListFragment.Time time) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (time.isAvailable) {

                    order.setTime(time.time);
                    order.setStatus(Globals.ACTIVE_ORDER_STATUS);

                    createAlertDialog();
                }
            }

        });

    }

    private void createAlertDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(DriverMainActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.confirm_queue_dialog, null);

        TextView dateDialogTv = dialogView.findViewById(R.id.date_dialog);
        TextView timeDialogTv = dialogView.findViewById(R.id.time_dialog);
        Button confirmDialogBtn = dialogView.findViewById(R.id.confirm_dialog);
        Button editingDialogBtn = dialogView.findViewById(R.id.editing_dialog);

        final AlertDialog alertDialog = builder.setView(dialogView).create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dateDialogTv.setText(order.getDate());
        timeDialogTv.setText(order.getTime());

        confirmDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timingNotification();

                pushDataOfOrderToFireStore();

                Fragment fragment = getFragmentManager().findFragmentByTag(TIME_LIST_FRAGMENT_TAG);
                getFragmentManager().beginTransaction().remove(fragment).commit();

                progressBar.setVisibility(View.VISIBLE);
                viewManager.clearScreen();

                alertDialog.dismiss();
                progressBar.setVisibility(View.GONE);

            }
        });

        editingDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new DateFragment(), DATE_FRAGMENT_TAG)
                        .addToBackStack(null).commit();

                Fragment fragment = getFragmentManager().findFragmentByTag(TIME_LIST_FRAGMENT_TAG);
                getFragmentManager().beginTransaction().remove(fragment).commit();

                alertDialog.dismiss();

            }
        });
        alertDialog.show();
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
        else if (hour > 20 && hour <= 23)
            timeTv.setText("לילה טוב");
    }

    private void pushDataOfOrderToFireStore() {

        final String date = order.getDate();
        final String time = order.getTime();
        final String uid = GlobalUtils.getStringFromLocalStorage(DriverMainActivity.this, Globals.UID_LOCAL_STORAGE_KEY);
        String status = Globals.ACTIVE_ORDER_STATUS;
        final String orderId = orderRef.document().getId(); //generate new id

        final Order order = new Order(orderId, date, time, uid, status);

        if (!OrdersQueue.getInstance().isThereActive()) {
            orderRef.document(orderId) //make a new order
                    .set(order)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(DriverMainActivity.this, "ההזמנה נשמרה", Toast.LENGTH_SHORT).show();
                            OrdersQueue.getInstance().add(order);

                            dateOfOrder = OrdersQueue.getInstance().getDate();
                            timeOfOrder = OrdersQueue.getInstance().getTime();

                            convertDate = GlobalUtils.convertDateToTimestamp(dateOfOrder, timeOfOrder);
                            afterGetOrderData(convertDate, orderFlag);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DriverMainActivity.this, "תקלה!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                        }
                    });
        } else {// when we update the order queue

            final Order temp = OrdersQueue.getInstance().getActive();
            orderFlag = false;

            orderRef.document(temp.getOrderId())
                    .update("date", date, "time", time)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            OrdersQueue.getInstance().updateOrder(temp.getOrderId(), date, time);

                            dateOfOrder = date;
                            timeOfOrder = time;

                            convertDate = GlobalUtils.convertDateToTimestamp(dateOfOrder, timeOfOrder);
                            afterGetOrderData(convertDate, orderFlag);

                            Toast.makeText(DriverMainActivity.this, "העדכון בוצע בהצלחה", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DriverMainActivity.this, "תקלה!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                }
            });
        }

    }

    private void pullDataOfOrderFromFireStore(String uid) {

        orderRef.whereEqualTo("uid", uid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot single : list) {
                    OrdersQueue.getInstance().add(single.toObject(Order.class));
                    order = single.toObject(Order.class);

                    dateOfOrder = OrdersQueue.getInstance().getDate();
                    timeOfOrder = OrdersQueue.getInstance().getTime();

                    convertDate = GlobalUtils.convertDateToTimestamp(dateOfOrder, timeOfOrder);

                    afterGetOrderData(convertDate, orderFlag);
                    progressBar.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e + "pullDataOfOrderFromFireStore");
            }
        });
    }

    private void afterGetOrderData(long convertDate, boolean orderFlag) {

        String status = OrdersQueue.getInstance().getStatus();
        final Order temp = OrdersQueue.getInstance().getFirst();

        totalTime = convertDate - currentTimeDate;
        hoursToLeft = TimeUnit.MILLISECONDS.toHours(totalTime);


        if (status != null) {// NO QUEUE
            if (currentTimeDate <= convertDate) {// QUEUE OR NEW QUEUE
                if (OrdersQueue.getInstance().getStatus().equals(Globals.ACTIVE_ORDER_STATUS)) {// QUEUE IS ACTIVE
                    if (hoursToLeft < 24) { // BETWEEN 24 HOURS
                        if (orderFlag) {// NEW QUEUE

                            countDownTimer().start();
                        } else {// UPDATE QUEUE
                            countDownTimer.cancel();
                            viewManager.updateOrder();
                            countDownTimer().start();
                        }
                    } else {// NOT BETWEEN 24 HOURS
                        countDownTimer.cancel();
                        viewManager.notBetween24Hours(temp);
                    }
                } else {// QUEUE IS NOT ACTIVE
                    viewManager.noQueue();
                    OrdersQueue.getInstance().clearQueue();
                }
            } else {//remove order from local
                OrdersQueue.getInstance().clearQueue();
            }
        } else {// No queue
            countDownTimer.cancel();
            viewManager.noQueue();
        }
    }

    private CountDownTimer countDownTimer() {

        final Order temp = OrdersQueue.getInstance().getFirst();

        countDownTimer = new CountDownTimer(totalTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                hoursToLeft = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                millisUntilFinished -= TimeUnit.HOURS.toMillis(hoursToLeft);

                minutesToLeft = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutesToLeft);

                secondsToLeft = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);

                viewManager.between24Hours(hoursToLeft, minutesToLeft, secondsToLeft, temp);

            }

            @Override
            public void onFinish() {
                timeLeftTv_1.setText("התור שלך הגיע");
                viewManager.noQueue();
                changeStatusFromFirestore();
                OrdersQueue.getInstance().clearQueue();
            }
        };
        return countDownTimer;
    }

    public void deleteQueueOnClick() {

        deleteQueueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String orderId = OrdersQueue.getInstance().getActive().getOrderId();

                AlertDialog.Builder builder = new AlertDialog.Builder(DriverMainActivity.this);
                builder.setMessage("האם לבטל את התור הזה?");
                builder.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteLastQueueFromFireStore(orderId);
                        GlobalUtils.cleanUserDataFromMemory(DriverMainActivity.this);
                    }
                });
                builder.setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(false);
                builder.show();

            }
        });
    }


    private void deleteLastQueueFromFireStore(final String orderId) {

        orderRef.document(orderId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        OrdersQueue.getInstance().clearQueue();
                        afterGetOrderData(convertDate, orderFlag);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error deleting document", e);
            }
        });

    }

    public void changeQueueOnClick() {

        changeQueueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.fragment_container, new DateFragment(), DATE_FRAGMENT_TAG);

                transaction.addToBackStack(null).commit();

            }
        });


    }

    public void directionOnClick() {

        directionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    // Launch Waze to look for jerusalem:
                    String url = "https://waze.com/ul?q=31.794307, 35.187647&navigate=yes";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // If Waze is not installed, open it in Google Play:
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
                    startActivity(intent);
                }
            }
        });
    }

    private void changeStatusFromFirestore() {

        final Order temp = OrdersQueue.getInstance().getFirst();
        String statusAfterPull = Globals.INACTIVE_ORDER_STATUS;
        orderRef.document(temp.getOrderId())
                .update("status", statusAfterPull)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        OrdersQueue.getInstance().clearQueue();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.toString());
            }
        });

    }

    private void timingNotification() {

        int milliSecond = 1000;
        int second = 60;
        int minutes = 30;

        String date = order.getDate();
        String time = order.getTime();

        convertDate = GlobalUtils.convertDateToTimestamp(date, time);
        currentTimeDate = GlobalUtils.getTimeStamp();
        totalTime = convertDate - currentTimeDate;
        totalTime -= milliSecond * second * minutes;

        Intent intent = new Intent(DriverMainActivity.this, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, currentTimeDate + totalTime, pendingIntent);

//        totalTime -= 1000 * 60 * 5;
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, currentTimeDate + totalTime, 0, pendingIntent);
    }
}
