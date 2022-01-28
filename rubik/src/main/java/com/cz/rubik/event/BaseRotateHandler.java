package com.cz.rubik.event;


import com.cz.rubik.model.Rubik;
import com.cz.rubik.render.RubikGLSurfaceView;
import com.cz.rubik.score.IStepHandler;
import com.cz.rubik.utils.tools.ArraysUtils;

public abstract class BaseRotateHandler implements IRotateHandler {
    protected transient RubikGLSurfaceView mSurfaceView;
    protected transient IStepHandler mStepHandler;
    protected Rubik mModel;
    protected float mWidth;
    protected float mHeight;
    private boolean isReady = false;

    public BaseRotateHandler(Rubik rubik) {
        mModel = rubik;
    }

    // transient修饰与未修饰的成员变量需要分开初始化
    public void init(RubikGLSurfaceView surfaceView, IStepHandler stepHandler) {
        mSurfaceView = surfaceView;
        mStepHandler = stepHandler;
    }

    @Override
    public void onHandlerReady() {
        if (!isReady) {
            mWidth = mSurfaceView.getWidth();
            mHeight = mSurfaceView.getHeight();
        }
    }

    @Override
    public void onRotateEnd() {
        mModel.getTransform().computeModelMatrix(ArraysUtils.getArr(0f, 0f, 1f), 0f);
    }

    // 正则化，将触摸点的坐标映射到[-1, 1]之间
    protected float[] getNormPosArr(float[] pos) {
        float x1 = pos[0] / mWidth * 2 - 1;
        float y1 = 1 - pos[1] / mHeight * 2;
        return ArraysUtils.getArr(x1, y1);
    }
}
