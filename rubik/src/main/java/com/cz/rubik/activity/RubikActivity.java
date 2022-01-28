package com.cz.rubik.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cz.rubik.R;
import com.cz.rubik.event.EventDispatcher;
import com.cz.rubik.model.Rubik;
import com.cz.rubik.render.RubikGLSurfaceView;
import com.cz.rubik.render.RubikRender;
import com.cz.rubik.score.IStepHandler;
import com.cz.rubik.score.TimeHandler;

public class RubikActivity extends AppCompatActivity implements IStepHandler {
    private static Context sContext;
    private Rubik mRubik;
    private RubikGLSurfaceView mGlSurfaceView;
    private RubikRender mRubikRender;
    private EventDispatcher mEventDispatcher;
    private TextView mStepNumView;
    private TextView mTimeView;
    private Button mPlay;
    private int mStepNum = 0; // 滑动步数
    private TimeHandler mTimeHandler;
    private boolean mHasStart = false;
    private boolean mInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubik);
        sContext = this;
        initView();
        initGL(savedInstanceState);
        mHasStart = true;
        mInitialized = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGlSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGlSurfaceView.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("mRubik", mRubik);
        outState.putSerializable("mEventDispatcher", mEventDispatcher);
        outState.putInt("mStepNum", mStepNum);
        outState.putInt("time", mTimeHandler.getTime());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (mInitialized) return;
        mRubik = (Rubik) savedInstanceState.getSerializable("mRubik");
        mEventDispatcher = (EventDispatcher) savedInstanceState.getSerializable("mEventDispatcher");
        mStepNum = savedInstanceState.getInt("mStepNum");
        mStepNumView.setText("" + mStepNum);
        mTimeHandler = new TimeHandler(mTimeView, savedInstanceState.getInt("time"));
        mRubikRender = new RubikRender(mRubik);
    }

    @Override
    public void increaseStep() {
        mStepNumView.setText("" + (++mStepNum));
    }

    public static Context getContext() {
        return sContext;
    }

    // 初始化View
    private void initView() {
        mGlSurfaceView = findViewById(R.id.gl_view);
        mStepNumView = findViewById(R.id.step_num);
        mTimeView = findViewById(R.id.time);
//        mPlay = findViewById(R.id.play);
//        mPlay.setOnClickListener(getListener());
    }

    // 初始化GL
    private void initGL(Bundle savedInstanceState) {
        if (mInitialized) return;
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
            initGLSecond();
            return;
        }
        initGLFirst();
        initGLSecond();
    }

    // 初始化GL第一步：创建对象
    private void initGLFirst() {
        int order = getIntent().getIntExtra("order", 1);
        mRubik = new Rubik(order);
        mRubik.onCreate();
        mRubikRender = new RubikRender(mRubik);
        mEventDispatcher = new EventDispatcher(mRubik);
        mTimeHandler = new TimeHandler(mTimeView, 0);
    }

    // 初始化GL第二步：关联对象
    private void initGLSecond() {
        mEventDispatcher.init(mGlSurfaceView, this);
        mGlSurfaceView.init(mRubikRender, mEventDispatcher);
        mTimeHandler.start();
    }

//    private View.OnClickListener getListener() {
//        return view -> {
//            switch (view.getId()) {
//                case R.id.play:
//                    if (mHasStart) {
//                        mTimeHandler.stop();
//                    } else {
//                        mTimeHandler.start();
//                    }
//                    mHasStart = !mHasStart;
//                    break;
//            }
//        };
//    }
}