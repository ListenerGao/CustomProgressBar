package com.listenergao.customprogressbar_library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;


/**
 * Created by ListenerGao on 2016/7/14.
 * 自定义带有进度的水平方向的进度条
 */
public class HorizontalProgressBarWithProgress extends ProgressBar {

    //设置自定义属性的默认值
    private static final int DEFAULT_TEXT_SIZE = 10; //默认字体的大小  单位sp
    private static final int DEFAULT_TEXT_COLOR = 0xFFFC00D1; //默认字体的颜色
    private static final int DEFAULT_UNREACH_COLOR = 0xFFD3D6DA; //默认进度条没有到达的颜色
    private static final int DEFAULT_UNREACH_HEIGHT = 2; //默认进度条没有到达的高度  单位dp
    private static final int DEFAULT_REACH_COLOR = DEFAULT_TEXT_COLOR; //默认进度条已到达的颜色  和字体颜色一致
    private static final int DEFAULT_REACH_HEIGHT = 2; //默认进度条已到达的高度  单位dp
    private static final int DEFAULT_TEXT_OFFSET = 10; //默认字体间隔的大小  单位dp

    //声明自定义属性的变量
    protected int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
    protected int mTextColor = DEFAULT_TEXT_COLOR;
    protected int mUnReachColor = DEFAULT_UNREACH_COLOR;
    protected int mUnReachHeight = dp2px(DEFAULT_UNREACH_HEIGHT);
    protected int mReachColor = DEFAULT_REACH_COLOR;
    protected int mReachHeight = dp2px(DEFAULT_REACH_HEIGHT);
    protected int mTextOffset = dp2px(DEFAULT_TEXT_OFFSET);
    /**
     * 创建画笔
     */
    protected Paint mPaint = new Paint();

    /**
     *控件真实的宽度,减去padding值后.
     * 可能会在onMeasure中赋值,在onDraw中使用.
     */
    private int mRealWidth;

    public HorizontalProgressBarWithProgress(Context context) {
        this(context,null);
    }

    public HorizontalProgressBarWithProgress(Context context, AttributeSet attrs) {
       this(context, attrs,0);
    }

    public HorizontalProgressBarWithProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义的属性
        obtainStyledAttrs(attrs);
    }


    /**
     * 获取自定义属性
     * @param attrs
     */
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
            //TypedArray对象是一个共享资源,必须在被使用后进行回收
            ta.recycle();
        }

        //设置字体的大小,为下文的测量字体的高度做准备.
        mPaint.setTextSize(mTextSize);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取宽度测量的模式以及值
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);    因为是进度条,用户肯定会指定其宽度
        int widthVal = MeasureSpec.getSize(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        //调用该方法,表示已经确定了view的宽和高
        setMeasuredDimension(widthVal,height);
        //实际绘制的宽度
        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    /**
     * 测量高
     * @param heightMeasureSpec
     * @return
     */
    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        if (mode == MeasureSpec.EXACTLY){  //精确值,用户设置的值为固定值,如200dp或为match_parent
            //给了精确值就不再进行测量
            result = size;
        }else {
            //测量字体的高度
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());
            //从已走进度条的高度,字体的高度,以及未走进度条的高度中得到最大的高度值 getPaddingTop() + getPaddingBottom()这两个值必加.  Math.abs(textHeight)得到字体高度的绝对值.
            result = getPaddingTop() + getPaddingBottom() +Math.max(Math.max(mReachHeight,mUnReachHeight),Math.abs(textHeight));

            if (mode == MeasureSpec.AT_MOST){   //即使自己测量,但测量值不能超过给定的size值
                result = Math.min(result,size);
            }
        }

        return result;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
//        super.onDraw(canvas);     不需要父类的方法,完全由自己控制
        canvas.save();
        canvas.translate(getPaddingLeft(),getHeight()/2);
        //标识,是否需要绘制  默认是需要绘制的.
        boolean noNeedUnReach = false;

        /*******************绘制已走的进度条********************/
        //获取文本
        String text = getProgress() + "%";
        //得到文本的宽度
        int textWidth = (int) mPaint.measureText(text);
        float radio = getProgress() * 1.0f / getMax();
        float progressX = radio * mRealWidth;
        if (progressX + textWidth > mRealWidth){    //如果当前的进度+字体的宽度大于要绘制的进度条的宽度
            //重新为progressX赋值
            progressX = mRealWidth - textWidth;
            noNeedUnReach = true; //不需要绘制了
        }

        float endX = progressX - mTextOffset/2;
        if (endX > 0) { //进度条刚开始时是0,防止出现负数
            //设置画笔的颜色
            mPaint.setColor(mReachColor);
            //设置画笔的宽度
            mPaint.setStrokeWidth(mReachHeight);
            //开始绘制
            canvas.drawLine(0,0,endX,0,mPaint);
        }

        /********************绘制文本*************************/
        mPaint.setColor(mTextColor);
        float y = -(mPaint.descent()+mPaint.ascent())/2;    //让字体居中显示
        canvas.drawText(text,progressX,y,mPaint);

        /******************绘制没有走的进度条******************/
        if (!noNeedUnReach){
            //开始绘制的位置
            float start = progressX + mTextOffset/2 + textWidth;
            mPaint.setColor(mUnReachColor);
            //设置画笔的宽度
            mPaint.setStrokeWidth(mUnReachHeight);
            //开始绘制
            canvas.drawLine(start,0,mRealWidth,0,mPaint);
        }

        canvas.restore();
    }

    /**
     * dp转换为px
     * @param dpVal
     * @return
     */
    protected int dp2px(int dpVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpVal,getResources().getDisplayMetrics());
    }

    /**
     * sp转换位px
     * @param spVal
     * @return
     */
    protected int sp2px(int spVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,spVal,getResources().getDisplayMetrics());
    }

}
