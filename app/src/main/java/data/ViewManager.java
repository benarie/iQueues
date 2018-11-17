package data;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iQueues.Order;
import com.iQueues.R;

public class ViewManager {

    TextView timeTv;
    TextView dateTv;
    TextView baseTv;
    TextView timeLeftTv_1;
    TextView timeLeftTv;

    Button insertQueueBtn;
    Button deleteQueueBtn;
    Button changeQueueBtn;
    Button directionBtn;

    public ViewManager(TextView timeTv, TextView dateTv, TextView baseTv, TextView timeLeftTv_1, TextView timeLeftTv, Button insertQueueBtn, Button deleteQueueBtn, Button changeQueueBtn, Button directionBtn) {
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

        timeLeftTv.setVisibility(View.GONE);
        timeLeftTv_1.setVisibility(View.GONE);
        baseTv.setVisibility(View.VISIBLE);
        dateTv.setVisibility(View.GONE);
        timeTv.setVisibility(View.GONE);
        baseTv.setText(R.string.note);

        insertQueueBtn.setVisibility(View.VISIBLE);
        changeQueueBtn.setVisibility(View.GONE);
        deleteQueueBtn.setVisibility(View.GONE);
        directionBtn.setVisibility(View.GONE);
    }

    public void notBetween24Hours(Order temp) {

        timeLeftTv.setVisibility(View.GONE);
        timeLeftTv_1.setVisibility(View.GONE);
        baseTv.setVisibility(View.GONE);
        dateTv.setVisibility(View.VISIBLE);
        timeTv.setVisibility(View.VISIBLE);
        dateTv.setText(temp.getDate());
        timeTv.setText(temp.getTime());

        insertQueueBtn.setVisibility(View.GONE);
        changeQueueBtn.setVisibility(View.VISIBLE);
        deleteQueueBtn.setVisibility(View.VISIBLE);
        directionBtn.setVisibility(View.VISIBLE);
    }

    public void between24Hours(Long hoursToLeft, Long minutesToLeft, Long secondsToLeft) {


        timeLeftTv.setVisibility(View.VISIBLE);
        timeLeftTv_1.setVisibility(View.VISIBLE);
        timeLeftTv_1.setText(hoursToLeft + ":" + minutesToLeft + ":" + secondsToLeft);

        baseTv.setVisibility(View.GONE);
        dateTv.setVisibility(View.GONE);
        timeTv.setVisibility(View.GONE);
        timeLeftTv.setVisibility(View.VISIBLE);

        insertQueueBtn.setVisibility(View.GONE);
        changeQueueBtn.setVisibility(View.VISIBLE);
        deleteQueueBtn.setVisibility(View.VISIBLE);
        directionBtn.setVisibility(View.VISIBLE);
    }

    public void updateOrder(){

        timeLeftTv_1.setText("");
    }
}
