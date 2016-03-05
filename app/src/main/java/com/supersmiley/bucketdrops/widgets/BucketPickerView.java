package com.supersmiley.bucketdrops.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.supersmiley.bucketdrops.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BucketPickerView extends LinearLayout implements View.OnTouchListener {
    public static final int LEFT = 0;
    public static final int TOP = 1;
    public static final int  RIGHT = 2;
    public static final int  BOTTOM = 3;
    private Calendar mCalendar;
    private TextView mTextDate;
    private TextView mTextMonth;
    private TextView mTextYear;
    private SimpleDateFormat mFormatter;
    private boolean mIncrement;
    private boolean mDecrement;
    public static final int DELAY = 250;
    private int MESSAGE_WHAT = 123;
    private int mActiveId;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(mIncrement){
                increment(mActiveId);
            }

            if(mDecrement){
                decrement(mActiveId);
            }

            // While there is a button pressed, keep sending message to increment or decrement continuously.
            if(mIncrement || mDecrement){
                mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT, DELAY);
            }

            return true;
        }
    });

    public BucketPickerView(Context context) {
        super(context);
        init(context);
    }

    public BucketPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BucketPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.bucket_picker_view, this);
        mCalendar = Calendar.getInstance();
        mFormatter = new SimpleDateFormat("MMM");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mTextDate = (TextView) this.findViewById(R.id.tv_date);
        mTextMonth = (TextView) this.findViewById(R.id.tv_month);
        mTextYear = (TextView) this.findViewById(R.id.tv_year);

        mTextDate.setOnTouchListener(this);
        mTextMonth.setOnTouchListener(this);
        mTextYear.setOnTouchListener(this);

        int date = mCalendar.get(Calendar.DATE);
        int month = mCalendar.get(Calendar.MONTH);
        int year = mCalendar.get(Calendar.YEAR);

        update(date, month, year, 0, 0, 0);
    }

    private void update(int date, int month, int year, int hour, int minute, int second){
        mCalendar.set(Calendar.DATE, date);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.HOUR, hour);
        mCalendar.set(Calendar.MINUTE, minute);
        mCalendar.set(Calendar.SECOND, second);

        mTextMonth.setText(mFormatter.format(mCalendar.getTime()));
        mTextDate.setText(date + "");
        mTextYear.setText(year + "");
    }

    public long getTime(){
        return mCalendar.getTimeInMillis();
    }

    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v     The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     *              the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(v.getId()){
            case R.id.tv_date:
                processEventsFor(mTextDate , event);

                break;
            case R.id.tv_month:
                processEventsFor(mTextMonth , event);

                break;
            case R.id.tv_year:
                processEventsFor(mTextYear , event);

                break;
        }

        return true;
    }

    private void processEventsFor(TextView textView, MotionEvent event){
        Drawable[] drawables = textView.getCompoundDrawables();

        if(hasDrawableTop(drawables) && hasDrawableBottom(drawables)){
            Rect topBounds = drawables[TOP].getBounds();
            Rect bottomBounds = drawables[BOTTOM].getBounds();
            float x = event.getX();
            float y = event.getY();
            mActiveId = textView.getId();

            if(topDrawableHit(textView, topBounds.height(), x, y)){
                if(isActionDown(event)){
                    mIncrement = true;
                    increment(textView.getId());
                    mHandler.removeMessages((MESSAGE_WHAT));
                    mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT, DELAY);
                }
                if(isActionUpOrCancel(event)){
                    mIncrement = false;
                }
            } else if( bottomDrawableHit(textView, bottomBounds.height(), x, y)){
                if(isActionDown(event)){
                    mDecrement = true;
                    decrement(textView.getId());
                    mHandler.removeMessages((MESSAGE_WHAT));
                    mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT, DELAY);
                }
                if(isActionUpOrCancel(event)){
                    mDecrement = false;
                }
            } else {
                mIncrement = mDecrement = false;
            }
        }
    }

    private void increment(int id){
        switch(id){
            case R.id.tv_date:
                mCalendar.add(Calendar.DATE, 1);

                break;
            case R.id.tv_month:
                mCalendar.add(Calendar.MONTH, 1);

                break;
            case R.id.tv_year:
                mCalendar.add(Calendar.YEAR, 1);

                break;
        }

        setTexts(mCalendar);
    }

    private void decrement(int id){
        switch(id){
            case R.id.tv_date:
                mCalendar.add(Calendar.DATE, -1);

                break;
            case R.id.tv_month:
                mCalendar.add(Calendar.MONTH, -1);

                break;
            case R.id.tv_year:
                mCalendar.add(Calendar.YEAR, -1);

                break;
        }

        setTexts(mCalendar);
    }

    private void setTexts(Calendar calendar) {
        int date = calendar.get(Calendar.DATE);
        int year = calendar.get(Calendar.YEAR);

        mTextDate.setText(date + "");
        mTextMonth.setText(mFormatter.format(mCalendar.getTime()));
        mTextYear.setText(year + "");
    }

    private boolean isActionUpOrCancel(MotionEvent event){
        return event.getAction() == MotionEvent.ACTION_UP
                || event.getAction() == MotionEvent.ACTION_CANCEL;
    }

    private boolean isActionDown(MotionEvent event){
        return event.getAction() == MotionEvent.ACTION_DOWN;
    }

    private boolean topDrawableHit(TextView textView, int drawableHeight, float x, float y){
        int xmin = textView.getPaddingLeft();
        int xmax = textView.getWidth() - textView.getPaddingRight();
        int ymin = textView.getPaddingTop();
        int ymax = textView.getPaddingTop() + drawableHeight;

        return x > xmin && x < xmax && y > ymin && y < ymax;
    }

    private boolean bottomDrawableHit(TextView textView, int drawableHeight, float x, float y){
        int xmin = textView.getPaddingLeft();
        int xmax = textView.getWidth() - textView.getPaddingRight();
        int ymax = textView.getHeight() - textView.getPaddingBottom();
        int ymin = ymax - drawableHeight;

        return x > xmin && x < xmax && y > ymin && y < ymax;
    }

    private boolean hasDrawableTop(Drawable[] drawables){
        return drawables[TOP] != null;
    }

    private boolean hasDrawableBottom(Drawable[] drawables){
        return drawables[BOTTOM] != null;
    }
}
