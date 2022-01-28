package com.cz.rubik.score;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class TimeHandler {
    private Timer mTimer;
    private TimerTask mTimerTask;
    private TextView mTimeView;
    private int mTime = 0;

    public TimeHandler(TextView timeView, int time) {
        mTimeView = timeView;
        mTime = time;
    }

    public void start() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
            };
        }
        mTimer.schedule(mTimerTask, 0, 1000);
    }

    public void stop() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    public int getTime() {
        return mTime;
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            mTime++;
            String timeStr = "";
            int hour = mTime / 3600;
            if (hour > 0) {
                timeStr += hour >= 10 ? hour : "0" + hour;
                timeStr += ":";
            }
            int time = mTime % 3600;
            int minute = time / 60;
            if (hour > 0 || minute > 0) {
                timeStr += minute >= 10 ? minute : "0" + minute;
                timeStr += ":";
            }
            int second = time % 60;
            timeStr += second >= 10 ? second : "0" + second;
            mTimeView.setText(timeStr);
        }
    };
}
