package data;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iQueues.Order;

/**
 * The type View manager.
 */
public class ViewManager {

    private TextView timeTv;
    private TextView dateTv;
    private TextView baseTv;
    private TextView timeLeftTv_1;
    private TextView timeLeftTv;
    private TextView firstTv;
    private ProgressBar progressBar;

    private Button insertQueueBtn;
    private ImageButton deleteQueueBtn;
    private ImageButton changeQueueBtn;
    private Button directionBtn;


    /**
     * Instantiates a new View manager.
     *  @param firstTv        the first tv
     * @param timeTv         the time tv
     * @param dateTv         the date tv
     * @param baseTv         the base tv
     * @param timeLeftTv_1   the time left tv 1
     * @param timeLeftTv     the time left tv
     * @param insertQueueBtn the insert queue btn
     * @param deleteQueueBtn the delete queue btn
     * @param changeQueueBtn the change queue btn
     * @param directionBtn   the direction btn
     * @param progressBar    the progress bar
     * @param waitTv
     */
    public ViewManager(TextView firstTv, TextView timeTv, TextView dateTv, TextView baseTv, TextView timeLeftTv_1, TextView timeLeftTv, Button insertQueueBtn, ImageButton deleteQueueBtn, ImageButton changeQueueBtn, Button directionBtn, ProgressBar progressBar, TextView waitTv) {
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
        this.progressBar = progressBar;
    }

    /**
     * No queue.
     */
    public void noQueue() {

        firstTv.setVisibility(View.VISIBLE);
        timeLeftTv.setVisibility(View.GONE);
        timeLeftTv_1.setVisibility(View.GONE);
        baseTv.setVisibility(View.VISIBLE);
        baseTv.setText("אין לך תור כרגע!");
        dateTv.setVisibility(View.GONE);
        timeTv.setVisibility(View.GONE);

        insertQueueBtn.setVisibility(View.VISIBLE);
        insertQueueBtn.setClickable(true);
        changeQueueBtn.setVisibility(View.GONE);
        deleteQueueBtn.setVisibility(View.GONE);
        directionBtn.setVisibility(View.GONE);

    }

    /**
     * Not between 24 hours.
     *
     * @param temp the temp
     */
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
        progressBar.setVisibility(View.GONE);
    }

    /**
     * Between 24 hours.
     *
     * @param hoursToLeft   the hours to left
     * @param minutesToLeft the minutes to left
     * @param secondsToLeft the seconds to left
     * @param temp          the temp
     */
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
        progressBar.setVisibility(View.GONE);

        insertQueueBtn.setVisibility(View.GONE);
        changeQueueBtn.setVisibility(View.VISIBLE);
        deleteQueueBtn.setVisibility(View.VISIBLE);
        directionBtn.setVisibility(View.VISIBLE);
    }

    /**
     * Update order.
     */
    public void updateOrder() {

        dateTv.setText("");
        timeTv.setText("");
        timeLeftTv_1.setText("");
    }

    /**
     * Clear screen.
     */
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
