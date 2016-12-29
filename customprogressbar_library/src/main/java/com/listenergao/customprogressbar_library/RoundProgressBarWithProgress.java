package com.listenergao.customprogressbar_library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Created by ListenerGao on 2016/7/15.
 * 圆形的带有进度的进度条,继承水平方向的进度条,为了复用已写好的属性
 */
public class RoundProgressBarWithProgress extends HorizontalProgressBarWithProgress {
    //默认的半径大小
    private int mRadius = dp2px(30);
    //最大的画笔宽度
    private int mMaxPaintWidth;
    public RoundProgressBarWithProgress(Context context) {
        this(context,null);
    }

    public RoundProgressBarWithProgress(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoundProgressBarWithProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //设置已走的进度的宽度为未走进度宽度的2.5倍,稍微好看一点
        mReachHeight = (int) (mUnReachHeight * 2.5f);

        //获取自定义属性  半径
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBarWithProgress);
        try {
            mRadius = (int) ta.getDimension(R.styleable.RoundProgressBarWithProgress_radius,mRadius);
        }finally {
            ta.recycle();
        }

        //设置画笔为空心的
        mPaint.setStyle(Paint.Style.STROKE);
        //设置抗锯齿
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMaxPaintWidth = Math.max(mReachHeight,mUnReachHeight);
        //期望的宽度值    此处默认四个Padding值一致
        int expect = mRadius * 2 + mMaxPaintWidth + getPaddingLeft() + getPaddingRight();

        //使用另一种方法测量宽和高
        int width = resolveSize(expect,widthMeasureSpec);
        int height = resolveSize(expect, heightMeasureSpec);

        //防止用户这是的宽高不一致,此处是要绘制圆的
        int realWidth = Math.min(width,height);

        //计算半径
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
        /***************************绘制未走的进度******************************/
        mPaint.setColor(mUnReachColor);
        mPaint.setStrokeWidth(mUnReachHeight);
        canvas.drawCircle(mRadius,mRadius,mRadius,mPaint);

        /**************************绘制已走的进度条****************************/
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mReachHeight);
        //此处绘制的是已走进度的 弧度
        float sweepAngle = getProgress() * 1.0f / getMax() * 360;
        canvas.drawArc(new RectF(0,0,mRadius*2,mRadius*2),0,sweepAngle,false,mPaint);

        /***************************绘制文本**********************************/
        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(text,mRadius - textWidth / 2,mRadius - textHeight,mPaint);
        canvas.restore();

    }
}
