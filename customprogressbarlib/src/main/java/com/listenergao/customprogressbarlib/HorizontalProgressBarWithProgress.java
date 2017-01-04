package com.listenergao.customprogressbarlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;


public class HorizontalProgressBarWithProgress extends ProgressBar {


    private static final int DEFAULT_TEXT_SIZE = 10;
    private static final int DEFAULT_TEXT_COLOR = 0xFFFC00D1;
    private static final int DEFAULT_UNREACH_COLOR = 0xFFD3D6DA;
    private static final int DEFAULT_UNREACH_HEIGHT = 2;
    private static final int DEFAULT_REACH_COLOR = DEFAULT_TEXT_COLOR;
    private static final int DEFAULT_REACH_HEIGHT = 2;
    private static final int DEFAULT_TEXT_OFFSET = 10;


    protected int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
    protected int mTextColor = DEFAULT_TEXT_COLOR;
    protected int mUnReachColor = DEFAULT_UNREACH_COLOR;
    protected int mUnReachHeight = dp2px(DEFAULT_UNREACH_HEIGHT);
    protected int mReachColor = DEFAULT_REACH_COLOR;
    protected int mReachHeight = dp2px(DEFAULT_REACH_HEIGHT);
    protected int mTextOffset = dp2px(DEFAULT_TEXT_OFFSET);

    protected Paint mPaint = new Paint();


    private int mRealWidth;

    public HorizontalProgressBarWithProgress(Context context) {
        this(context,null);
    }

    public HorizontalProgressBarWithProgress(Context context, AttributeSet attrs) {
       this(context, attrs,0);
    }

    public HorizontalProgressBarWithProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        obtainStyledAttrs(attrs);
    }


    private void obtainStyledAttrs(AttributeSet attrs) {

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalProgressBarWithProgress);
        try {
            mTextSize = (int) ta.getDimension(R.styleable.HorizontalProgressBarWithProgress_progress_text_size,mTextSize);
            mTextColor = ta.getColor(R.styleable.HorizontalProgressBarWithProgress_progress_text_color,mTextColor);

            mUnReachColor = ta.getColor(R.styleable.HorizontalProgressBarWithProgress_progress_unreach_color,mUnReachColor);
            mUnReachHeight = (int) ta.getDimension(R.styleable.HorizontalProgressBarWithProgress_progress_unreach_height,mUnReachHeight);

            mReachColor = ta.getColor(R.styleable.HorizontalProgressBarWithProgress_progress_reach_color,mReachColor);
            mReachHeight = (int) ta.getDimension(R.styleable.HorizontalProgressBarWithProgress_progress_reach_height,mReachHeight);

            mTextOffset = (int) ta.getDimension(R.styleable.HorizontalProgressBarWithProgress_progress_text_offset,mTextOffset);
        }finally {

            ta.recycle();
        }


        mPaint.setTextSize(mTextSize);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthVal = MeasureSpec.getSize(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

        setMeasuredDimension(widthVal,height);

        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }


    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        if (mode == MeasureSpec.EXACTLY){

            result = size;
        }else {

            int textHeight = (int) (mPaint.descent() - mPaint.ascent());

            result = getPaddingTop() + getPaddingBottom() +Math.max(Math.max(mReachHeight,mUnReachHeight),Math.abs(textHeight));

            if (mode == MeasureSpec.AT_MOST){
                result = Math.min(result,size);
            }
        }

        return result;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        canvas.save();
        canvas.translate(getPaddingLeft(),getHeight()/2);

        boolean noNeedUnReach = false;


        String text = getProgress() + "%";

        int textWidth = (int) mPaint.measureText(text);
        float radio = getProgress() * 1.0f / getMax();
        float progressX = radio * mRealWidth;
        if (progressX + textWidth > mRealWidth){

            progressX = mRealWidth - textWidth;
            noNeedUnReach = true;
        }

        float endX = progressX - mTextOffset/2;
        if (endX > 0) {

            mPaint.setColor(mReachColor);

            mPaint.setStrokeWidth(mReachHeight);

            canvas.drawLine(0,0,endX,0,mPaint);
        }


        mPaint.setColor(mTextColor);
        float y = -(mPaint.descent()+mPaint.ascent())/2;
        canvas.drawText(text,progressX,y,mPaint);


        if (!noNeedUnReach){

            float start = progressX + mTextOffset/2 + textWidth;
            mPaint.setColor(mUnReachColor);

            mPaint.setStrokeWidth(mUnReachHeight);

            canvas.drawLine(start,0,mRealWidth,0,mPaint);
        }

        canvas.restore();
    }


    protected int dp2px(int dpVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpVal,getResources().getDisplayMetrics());
    }


    protected int sp2px(int spVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,spVal,getResources().getDisplayMetrics());
    }

}