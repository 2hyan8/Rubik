package com.cz.rubik.event;

import android.view.MotionEvent;

import androidx.core.util.Consumer;

import com.cz.rubik.model.Rubik;
import com.cz.rubik.render.RubikGLSurfaceView;
import com.cz.rubik.score.IStepHandler;
import com.cz.rubik.utils.tools.ArraysUtils;

import java.io.Serializable;

/*
 * 事件分发器
 */
public class EventDispatcher implements Serializable {
    private BaseRotateHandler[] mRotateHandlers = new BaseRotateHandler[2];
    private int mRotateMode = IRotateHandler.ROTETE_MODE_UNDEFINE;

    public EventDispatcher(Rubik rubik) {
        mRotateHandlers[0] = new TotalRotateHandler(rubik);
        mRotateHandlers[1] = new PartRotateHandler(rubik);
    }

    public synchronized void init(RubikGLSurfaceView surfaceView, IStepHandler stepHandler) {
        forAllHandler(handler -> handler.init(surfaceView, stepHandler));
    }

    public synchronized boolean onTouchEvent(MotionEvent event) {
        float[] pos = ArraysUtils.getArr(event.getX(), event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onRotateStart(pos);
                break;
            case MotionEvent.ACTION_MOVE:
                onRotate(pos);
                break;
            case MotionEvent.ACTION_UP:
                onRotateEnd();
                break;
        }
        return true;
    }

    private void onRotateStart(float[] pos) {
        if (mRotateHandlers[1].onRotateStart(pos)) {
            mRotateMode = IRotateHandler.ROTETE_MODE_PART;
        } else if (mRotateHandlers[0].onRotateStart(pos)) {
            mRotateMode = IRotateHandler.ROTETE_MODE_TOTAL;
        }
    }

    private void onRotate(float[] pos) {
        forHandler(handler -> handler.onRotate(pos));
    }

    private void onRotateEnd() {
        forHandler(handler -> handler.onRotateEnd());
        mRotateMode = IRotateHandler.ROTETE_MODE_UNDEFINE;
    }

    private void forHandler(Consumer<BaseRotateHandler> consumer) {
        if (mRotateMode == IRotateHandler.ROTETE_MODE_TOTAL) {
            consumer.accept(mRotateHandlers[0]);
        } else if (mRotateMode == IRotateHandler.ROTETE_MODE_PART) {
            consumer.accept(mRotateHandlers[1]);
        }
    }

    private void forAllHandler(Consumer<BaseRotateHandler> consumer) {
        for (int i = 0; i <mRotateHandlers.length; i++) {
            consumer.accept(mRotateHandlers[i]);
        }
    }
}
