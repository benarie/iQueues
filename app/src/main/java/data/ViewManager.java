package data;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.iQueues.Order;

public class ViewManager {

    private TextView timeTv;
    private TextView dateTv;
    private TextView baseTv;
    private TextView timeLeftTv_1;
    private TextView timeLeftTv;
    private TextView firstTv;

    private Button insertQueueBtn;
    private ImageButton deleteQueueBtn;
    private ImageButton changeQueueBtn;
    private Button directionBtn;


    public ViewManager(TextView firstTv, TextView timeTv, TextView dateTv, TextView baseTv, TextView timeLeftTv_1, TextView timeLeftTv, Button insertQueueBtn, ImageButton deleteQueueBtn, ImageButton changeQueueBtn, Button directionBtn) {
        this.firstTv = firstTv;
        this.timeTv = timeTv;
        this.dateTv = dateTv;
        this.baseTv = baseTv;
        this.timeLeftTv_1 = timeLeftTv_1;
        this.timeLeftTv = timeLeftTv;
        this.insertQueueBtn = insertQueueBtn;
        this.deleteQueueBtn = deleteQueueBtn;
        this.changeQueueBtn = changeQueueBtn;
        this.directionBtn = directionBtn;
    }

    public void noQueue() {

        firstTv.setVisibility(View.VISIBLE);
        timeLeftTv.setVisibility(View.GONE);
        timeLeftTv_1.setVisibility(View.GONE);
        baseTv.setVisibility(View.VISIBLE);
        baseTv.setText("אין לך תור כרגע!");
        dateTv.setVisibility(View.GONE);
        timeTv.setVisibility(View.GONE);

        insertQueueBtn.setVisibility(View.VISIBLE);
        changeQueueBtn.setVisibility(View.GONE);
        deleteQueueBtn.setVisibility(View.GONE);
        directionBtn.setVisibility(View.GONE);
    }

    public void notBetween24Hours(Order temp) {

        firstTv.setVisibility(View.INVISIBLE);
        baseTv.setText("יש לך תור!");
        timeLeftTv.setVisibility(View.GONE);
        timeLeftTv_1.setVisibility(View.GONE);
        baseTv.setVisibility(View.VISIBLE);
        dateTv.setVisibility(View.VISIBLE);
        timeTv.setVisibility(View.VISIBLE);
        dateTv.setText(temp.getDate());
        timeTv.setText(temp.getTime());

        insertQueueBtn.setVisibility(View.GONE);
        changeQueueBtn.setVisibility(View.VISIBLE);
        deleteQueueBtn.setVisibility(View.VISIBLE);
        directionBtn.setVisibility(View.VISIBLE);
    }

    public void between24Hours(Long hoursToLeft, Long minutesToLeft, Long secondsToLeft, Order temp) {

        timeLeftTv.setVisibility(View.VISIBLE);
        timeLeftTv_1.setVisibility(View.VISIBLE);
        timeLeftTv_1.setText(hoursToLeft + ":" + minutesToLeft + ":" + secondsToLeft);
        firstTv.setVisibility(View.INVISIBLE);
        baseTv.setText("יש לך תור!");
        baseTv.setVisibility(View.VISIBLE);
        dateTv.setVisibility(View.VISIBLE);
        timeTv.setVisibility(View.VISIBLE);
        timeLeftTv.setVisibility(View.VISIBLE);
        dateTv.setText(temp.getDate());
        timeTv.setText(temp.getTime());

        insertQueueBtn.setVisibility(View.GONE);
        changeQueueBtn.setVisibility(View.VISIBLE);
        deleteQueueBtn.setVisibility(View.VISIBLE);
        directionBtn.setVisibility(View.VISIBLE);
    }

    public void updateOrder() {

        timeLeftTv_1.setText("");

    }

    public void clearScreen() {

        firstTv.setVisibility(View.GONE);
        timeLeftTv.setVisibility(View.GONE);
        timeLeftTv_1.setVisibility(View.GONE);
        baseTv.setVisibility(View.GONE);
        dateTv.setVisibility(View.GONE);
        timeTv.setVisibility(View.GONE);

        insertQueueBtn.setVisibility(View.GONE);
        changeQueueBtn.setVisibility(View.GONE);
        deleteQueueBtn.setVisibility(View.GONE);
        directionBtn.setVisibility(View.GONE);

    }
}
