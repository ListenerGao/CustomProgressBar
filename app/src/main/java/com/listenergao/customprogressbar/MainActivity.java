package com.listenergao.customprogressbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.listenergao.customprogressbarlib.HorizontalProgressBarWithProgress;
import com.listenergao.customprogressbarlib.RoundProgressBarWithProgress;


public class MainActivity extends AppCompatActivity {

    private static final int MSG_UPDATE = 0x110;
    private HorizontalProgressBarWithProgress mHProgress;
    private RoundProgressBarWithProgress mRoundProgress;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int progress = mHProgress.getProgress();
            mHProgress.setProgress(++progress);
            mRoundProgress.setProgress(++progress);
            if (progress > 100) {
                mHandler.removeMessages(MSG_UPDATE);
            }
            mHandler.sendEmptyMessageDelayed(MSG_UPDATE,100);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHProgress = (HorizontalProgressBarWithProgress) findViewById(R.id.progress);
        mRoundProgress = (RoundProgressBarWithProgress) findViewById(R.id.round_progress);
        mHandler.sendEmptyMessage(MSG_UPDATE);
    }
}
