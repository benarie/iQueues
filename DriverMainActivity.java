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

/**
 * The type Driver main activity.
 */
public class DriverMainActivity extends AppCompatActivity implements DateFragment.OnQueueFragmentListener, TimeListFragment.OnTimeListFragmentListener {
    /**
     * The View manager.
     */
    ViewManager viewManager;
    /**
     * The Time in day tv.
     */
    TextView timeInDayTv;
    /**
     * The Name tv.
     */
    TextView nameTv;
    /**
     * The Time tv.
     */
    TextView timeTv;
    /**
     * The Date tv.
     */
    TextView dateTv;
    /**
     * The Base tv.
     */
    TextView baseTv;
    /**
     * The Time left tv 1.
     */
    TextView timeLeftTv_1;
    /**
     * The Time left tv.
     */
    TextView timeLeftTv;
    /**
     * The First tv.
     */
    TextView firstTv;

    TextView waitTv;
    /**
     * The Insert queue btn.
     */
    Button insertQueueBtn;
    /**
     * The Delete queue btn.
     */
    ImageButton deleteQueueBtn;
    /**
     * The Change queue btn.
     */
    ImageButton changeQueueBtn;
    /**
     * The Direction btn.
     */
    Button directionBtn;
    /**
     * The Power btn.
     */
    ImageButton powerBtn;
    /**
     * The Count down timer.
     */
    CountDownTimer countDownTimer;
    /**
     * The Progress bar.
     */
    ProgressBar progressBar;

    /**
     * The Date fragment tag.
     */
    final String DATE_FRAGMENT_TAG = "date_fragment";
    /**
     * The Time list fragment tag.
     */
    final String TIME_LIST_FRAGMENT_TAG = "time_list_fragment";
    /**
     * The Tag.
     */
    final String TAG = "DriverMainActivity";

    private String dateOfOrder;
    private String timeOfOrder;
    private long currentTimeDate;
    private long convertDate;
    private long totalTime;
    private long hoursToLeft;
    private long minutesToLeft;
    private long secondsToLeft;

    /**
     * The Order flag.
     */
    boolean orderFlag = true;

    /**
     * get instance to Fire store.
     * mack collection to orders into Fire store
     */
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference orderRef = database.collection("orders");

    /**
     * The Order.
     */
    Order order = new Order();

    /**
     * @param savedInstanceState initialize the parameters.
     *                           call to  insertQueueBtn onClick
     *                           call to logoutFromMainActivity().
     *                           call to deleteQueueOnClick().
     *                           call to changeQueueOnClick().
     *                           call to  navigationOnClick().
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);

        timeInDayTv = findViewById(R.id.time_text_output);
        progressBar = findViewById(R.id.main_progress_bar);
        nameTv = findViewById(R.id.name_text_output);
        dateTv = findViewById(R.id.date_text_view);
        timeTv = findViewById(R.id.time_text_view);
        baseTv = findViewById(R.id.no_queue_text_view);
        timeLeftTv_1 = findViewById(R.id.time_left_text_view_1);
        timeLeftTv = findViewById(R.id.time_left_text_view);
        firstTv = findViewById(R.id.first_text_view);
        waitTv = findViewById(R.id.wait_text_view);

        changeQueueBtn = findViewById(R.id.change_btn);
        deleteQueueBtn = findViewById(R.id.delete_btn);
        insertQueueBtn = findViewById(R.id.insert_queue_btn);
        directionBtn = findViewById(R.id.direction_btn);
        powerBtn = findViewById(R.id.btn_power);
        progressBar = findViewById(R.id.main_progress_bar);
        viewManager = new ViewManager(firstTv, timeTv, dateTv, baseTv, timeLeftTv_1, timeLeftTv, insertQueueBtn, deleteQueueBtn, changeQueueBtn, directionBtn, progressBar, waitTv);

        currentTimeDate = GlobalUtils.getTimeStamp();

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

        navigationOnClick();

    }

    /**
     * logout From Main Activity
     * clean User Data From Memory
     */
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
    /////////////////////////////////////////// FUNCTION FROM FRAGMENTS //////////////////////////////////////////////

    /**
     * clear the stack and return backwards
     */


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    /**
     * when clicked on confirm btn the fragments replace
     *
     * @param date the date
     */
    @Override
    public void onConfirmDateBtnClicked(String date) {

        order.setDate(date);

        TimeListFragment tlf = new TimeListFragment();
        Bundle args = new Bundle();
        args.putString(TimeListFragment.DATA_RECEIVE, date);
        tlf.setArguments(args);

        /**replace between fragments */
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, tlf, TIME_LIST_FRAGMENT_TAG)
                .addToBackStack(null).commit();

        Fragment fragment = getFragmentManager().findFragmentByTag(DATE_FRAGMENT_TAG);
        getFragmentManager().beginTransaction().remove(fragment).commit();
    }


    /**
     * return to the main activity and remove date fragment
     */
    @Override
    public void onDeleteDateBtnClicked() {

        Fragment fragment = getFragmentManager().findFragmentByTag(DATE_FRAGMENT_TAG);
        getFragmentManager().beginTransaction().remove(fragment).commit();
    }

    /**
     * set time of order and status to order class
     * create dialog
     *
     * @param time the time
     */
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

    /**
     * create dialog with the time and date of order
     * create the notification
     * push the order to fire store
     * return to the main activity and remove time list fragment
     */
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


    /**
     * get User name Details
     * check Time in the day
     */
    @Override
    protected void onStart() {
        super.onStart();

        String uid = GlobalUtils.getStringFromLocalStorage(DriverMainActivity.this, Globals.UID_LOCAL_STORAGE_KEY);
        pullDataOfOrderFromFireStore(uid);

        getUserDetails();
        checkTime();
    }

/////////////////////////////////////////// FUNCTION FROM MAIN //////////////////////////////////////////////

    /**
     * Gets user details.
     */
    public void getUserDetails() {

        String name = GlobalUtils.getStringFromLocalStorage(this, Globals.FULL_NAME_LOCAL_STORAGE_KEY);
        nameTv.setText(name);
    }

    /**
     * Check time in the day.
     */
    public void checkTime() {

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour >= 0 && hour <= 12)
            timeInDayTv.setText("בוקר טוב");
        else if (hour > 12 && hour <= 17)
            timeInDayTv.setText("צהריים טובים");
        else if (hour > 17 && hour <= 20)
            timeInDayTv.setText("ערב טוב");
        else if (hour > 20 && hour <= 23)
            timeInDayTv.setText("לילה טוב");
    }

    /**
     * connect to the fire store and make a new order
     * or update the order exists
     */
    private void pushDataOfOrderToFireStore() {

        final String date = order.getDate();
        final String time = order.getTime();
        final String uid = GlobalUtils.getStringFromLocalStorage(getApplicationContext(), Globals.UID_LOCAL_STORAGE_KEY);
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
        } else {/** when we update the order queue */

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

    /**
     * pull Data Of Order From Fire Store
     *
     * @param uid pull data buy uid
     */
    private void pullDataOfOrderFromFireStore(String uid) {

        progressBar.setVisibility(View.VISIBLE);
        waitTv.setVisibility(View.VISIBLE);
        String status = Globals.ACTIVE_ORDER_STATUS;
        orderRef.whereEqualTo("uid", uid).whereEqualTo("status", status).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                if (list.size() <= 0) {
                    afterGetOrderData(convertDate, orderFlag);
                }
                for (DocumentSnapshot single : list) {
                    OrdersQueue.getInstance().add(single.toObject(Order.class));
                    order = single.toObject(Order.class);

                    dateOfOrder = OrdersQueue.getInstance().getDate();
                    timeOfOrder = OrdersQueue.getInstance().getTime();

                    convertDate = GlobalUtils.convertDateToTimestamp(dateOfOrder, timeOfOrder);

                    afterGetOrderData(convertDate, orderFlag);
                }
                progressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e + "pullDataOfOrderFromFireStore");
            }
        });
    }

    /**
     * Turn on the timer when the queue is within 24 hours
     *
     * @return the timer
     */
    private CountDownTimer createCountDownTimer() {

        final Order temp = OrdersQueue.getInstance().getFirst();
        if (temp == null) {
            return null;
        }
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

            /**
             *When time is up
             */
            @Override
            public void onFinish() {
                changeStatusFromFireStore();
                OrdersQueue.getInstance().clearQueue();
                viewManager.noQueue();
            }
        }.start();
        return countDownTimer;
    }


    /**
     * Loads the corresponding view according to the queue
     *
     * @param convertDate convert date and time to time stamp
     * @param orderFlag   Determines if the queue is new or up-to-date
     */
    private void afterGetOrderData(long convertDate, boolean orderFlag) {

        String status = OrdersQueue.getInstance().getStatus();
        final Order temp = OrdersQueue.getInstance().getFirst();

        totalTime = convertDate - currentTimeDate;
        hoursToLeft = TimeUnit.MILLISECONDS.toHours(totalTime);
        if (status != null) {// NO QUEUE
            if (currentTimeDate <= convertDate) {// QUEUE OR NEW QUEUE
                if (OrdersQueue.getInstance().getStatus().equals(Globals.ACTIVE_ORDER_STATUS)) { // QUEUE IS ACTIVE
                    if (hoursToLeft < 24) { // BETWEEN 24 HOURS
                        if (orderFlag) {// NEW QUEUE

                            createCountDownTimer().start();
                        } else {// UPDATE QUEUE
                            if (countDownTimer == null) {
                                createCountDownTimer().cancel();
                            } else {
                                countDownTimer.cancel();
                            }
                            viewManager.updateOrder();
                            createCountDownTimer().start();
                        }
                    } else {// NOT BETWEEN 24 HOURS
                        if (countDownTimer == null) {
                            createCountDownTimer().cancel();
                        } else {
                            countDownTimer.cancel();
                        }
                        viewManager.notBetween24Hours(temp);
                    }
                } else {// QUEUE IS NOT ACTIVE

                    viewManager.noQueue();
                    OrdersQueue.getInstance().clearQueue();
                }
            } else {//remove order from local

                changeStatusFromFireStore();
            }
        } else {// No queue
            if (countDownTimer == null) {
                createCountDownTimer();
            } else {
                countDownTimer.cancel();
            }
            viewManager.noQueue();
        }
    }


    /**
     * change Status From Fire Store after the queue is finished
     */
    private void changeStatusFromFireStore() {

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

        OrdersQueue.getInstance().clearQueue();
        viewManager.noQueue();

    }
////////////////////////////////// Functions that are responsible for the buttons ////////////////////////////////

    /**
     * AlertDialog: Delete queue on click.
     */
    public void deleteQueueOnClick() {

        deleteQueueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String orderId = OrdersQueue.getInstance().getFirst().getOrderId();

                AlertDialog.Builder builder = new AlertDialog.Builder(DriverMainActivity.this);
                builder.setMessage("האם לבטל את התור הזה?");
                builder.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteLastQueueFromFireStore(orderId);
                        OrdersQueue.getInstance().clearQueue();
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

    /**
     * delete the Last Queue From Fire Store
     *
     * @param orderId the order Id
     */
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

    /**
     * Change queue on click.
     */
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

    /**
     * navigation by waze to the company  on click.
     */
    public void navigationOnClick() {

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
////////////////////////////////// Function that are responsible for the Notification ////////////////////////////////

    /**
     * timing Notification by order queue
     */
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
    }
}
