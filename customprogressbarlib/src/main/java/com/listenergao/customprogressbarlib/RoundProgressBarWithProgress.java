package com.listenergao.customprogressbarlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;


public class RoundProgressBarWithProgress extends HorizontalProgressBarWithProgress {

    private int mRadius = dp2px(30);

    private int mMaxPaintWidth;
    public RoundProgressBarWithProgress(Context context) {
        this(context,null);
    }

    public RoundProgressBarWithProgress(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoundProgressBarWithProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        mReachHeight = (int) (mUnReachHeight * 2.5f);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBarWithProgress);
        try {
            mRadius = (int) ta.getDimension(R.styleable.RoundProgressBarWithProgress_radius,mRadius);
        }finally {
            ta.recycle();
        }


        mPaint.setStyle(Paint.Style.STROKE);

        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMaxPaintWidth = Math.max(mReachHeight,mUnReachHeight);

        int expect = mRadius * 2 + mMaxPaintWidth + getPaddingLeft() + getPaddingRight();


        int width = resolveSize(expect,widthMeasureSpec);
        int height = resolveSize(expect, heightMeasureSpec);


        int realWidth = Math.min(width,height);


        mRadius = (realWidth - getPaddingLeft() - getPaddingRight() - mMaxPaintWidth) / 2;

        setMeasuredDimension(realWidth,realWidth);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        String text = getProgress() + "%";
        int textWidth = (int) mPaint.measureText(text);
        int textHeight = (int) ((mPaint.descent() + mPaint.ascent()) / 2);

        canvas.save();

        canvas.translate(getPaddingLeft() + mMaxPaintWidth / 2,getPaddingTop() + mMaxPaintWidth / 2);
        mPaint.setStyle(Paint.Style.STROKE);

        mPaint.setColor(mUnReachColor);
        mPaint.setStrokeWidth(mUnReachHeight);
        canvas.drawCircle(mRadius,mRadius,mRadius,mPaint);


        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mReachHeight);

        float sweepAngle = getProgress() * 1.0f / getMax() * 360;
        canvas.drawArc(new RectF(0,0,mRadius*2,mRadius*2),0,sweepAngle,false,mPaint);

        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(text,mRadius - textWidth / 2,mRadius - textHeight,mPaint);
        canvas.restore();

    }
}