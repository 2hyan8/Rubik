package com.cz.rubik.event;

import com.cz.rubik.model.Rubik;
import com.cz.rubik.utils.tools.ArraysUtils;
import com.cz.rubik.utils.tools.VectorUtils;

/*
 * 整体旋转处理器
 */
public class TotalRotateHandler extends BaseRotateHandler {
    private static final float ROTATE_TATE = 200; //旋转比率
    private float[] mStartNormPos; // 起点点击的位置
    private float[] mCurrNormPos; // 当前触摸点（旋转过程中）

    public TotalRotateHandler(Rubik rubik) {
        super(rubik);
    }

    @Override
    public synchronized boolean onRotateStart(float[] pos) {
        onHandlerReady();
        mStartNormPos = getNormPosArr(pos);
        return true;
    }

    @Override
    public synchronized void onRotate(float[] pos) {
        mCurrNormPos = getNormPosArr(pos);
        float[] axis = getRotateAxis();
        float angle = getRotateAngle();
        mModel.getTransform().setRotateAttr(axis, angle);
        mSurfaceView.requestRender();
    }

    @Override
    public synchronized void onRotateEnd() {
        mModel.onChangeVertex();
        super.onRotateEnd();
        mSurfaceView.requestRender();
    }

    @Override
    public float[] getRotateAxis() {
        float[] pos1 = ArraysUtils.getArr(mStartNormPos[0], mStartNormPos[1], 0);
        float[] pos2 = ArraysUtils.getArr(mCurrNormPos[0], mCurrNormPos[1], 0);
        float[] vec = VectorUtils.getVec(pos1, pos2);
        float dis = VectorUtils.getVecNorm(vec);
        if (dis < VectorUtils.EPSILON) {
            return ArraysUtils.getArr(0f, 0f, 1f);
        }
        float[] zVec = ArraysUtils.getArr(0f, 0f, 1f);
        return VectorUtils.getCrossProduct(zVec, vec); // zVec X vec
    }

    @Override
    public float getRotateAngle() {
        float dis = VectorUtils.getDistance(mStartNormPos, mCurrNormPos);
        float angle = dis * ROTATE_TATE;
        return angle;
    }

    public float[] getNormPosArr(float[] pos) {
        float[] normPos = super.getNormPosArr(pos);
        normPos[0] = normPos[0] * (mWidth / mHeight);
        return normPos;
    }
}
