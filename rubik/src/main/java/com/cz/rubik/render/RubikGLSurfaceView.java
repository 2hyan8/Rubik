package com.cz.rubik.render;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.cz.rubik.event.EventDispatcher;
import com.cz.rubik.model.Rubik;
import com.cz.rubik.score.IStepHandler;

import java.io.Serializable;

/*
 * 显示魔方的SurfaceView类
 */
public class RubikGLSurfaceView extends GLSurfaceView {
    private EventDispatcher mEventDispatcher;

    public RubikGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(3);
    }

    public RubikGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(3);
    }

    public void init(RubikRender render, EventDispatcher dispatcher) {
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
        setRenderer(render);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mEventDispatcher = dispatcher;
    }

    @Override
    public synchronized boolean onTouchEvent(MotionEvent event) {
        return mEventDispatcher.onTouchEvent(event);
    }
}
