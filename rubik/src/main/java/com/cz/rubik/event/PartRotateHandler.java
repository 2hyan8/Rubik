package com.cz.rubik.event;

import com.cz.rubik.model.IModel;
import com.cz.rubik.model.Rubik;
import com.cz.rubik.model.SquareEnvoy;
import com.cz.rubik.utils.algorithm.AngleUtils;
import com.cz.rubik.utils.tools.ArraysUtils;
import com.cz.rubik.utils.tools.VectorUtils;

/*
 * 局部旋转处理器
 */
public class PartRotateHandler extends BaseRotateHandler {
    private static final float ROTATE_TATE = 400; //旋转比率
    private SquareEnvoy mSquare1 = new SquareEnvoy();
    private SquareEnvoy mSquare2 = new SquareEnvoy();
    private volatile boolean isReady = false;
    private int mLayer = IModel.INVALID_LAYER;
    private float[] mRotateAxis; // 旋转轴
    private float mRotateAngle; // 旋转角度
    private float[] mFirstNormPos; // 触摸起点
    private float[] mStartNormPos; // 旋转生效的起点
    private float[] mCurrNormPos; // 当前触摸点（旋转过程中）

    public PartRotateHandler(Rubik rubik) {
        super(rubik);
    }

    @Override
    public synchronized boolean onRotateStart(float[] pos) {
        onHandlerReady();
        if (Rubik.sOrder_1 <= 1) return false;
        mFirstNormPos = getNormPosArr(pos);
        getTouchedSquare(mSquare1, mFirstNormPos);
//        if (mSquare1.isValid()) {
//            int[] cubeLoc = mSquare1.getCubeLocation();
//            int squareLoc = mSquare1.getSquareLocation();
//            Toast.makeText(mSurfaceView.getContext(), "Cube：" + Arrays.toString(cubeLoc) + ", Square: " + squareLoc, Toast.LENGTH_SHORT).show();
//        }
        return mSquare1.isValid();
    }

    @Override
    public synchronized void onRotate(float[] pos) {
        mCurrNormPos = getNormPosArr(pos);
        if (!isReady) {
            getTouchedSquare(mSquare2, mCurrNormPos);
            mLayer = mSquare1.getLayer(mSquare2, mSurfaceView.getContext());
            if (mLayer != IModel.INVALID_LAYER) {
//                Toast.makeText(mSurfaceView.getContext(), "滑动" + mLayer + "层", Toast.LENGTH_SHORT).show();
                mModel.setLayer(mLayer);
                mRotateAxis = getRotateAxis();
                mStartNormPos = adjustNormPos();
                isReady = true;
            }
        } else {
            adjustNormPos();
            mRotateAngle = getRotateAngle();
            mModel.getTransform().setRotateAttr(mRotateAxis, mRotateAngle);
            mSurfaceView.requestRender();
        }
    }

    @Override
    public synchronized void onRotateEnd() {
        if (mLayer == IModel.INVALID_LAYER) {
            return;
        }
        int theta = AngleUtils.getNearAngle(mRotateAngle);
        mModel.getTransform().setRotateAttr(mRotateAxis, theta);
        mModel.onChangeVertex();
        mModel.onChangeLocation(mLayer, theta);
        super.onRotateEnd();
        mSurfaceView.requestRender();
        mLayer = IModel.INVALID_LAYER;
        mModel.setLayer(mLayer);
        isReady = false;
        mSquare1.reset();
        mSquare2.reset();
        if (theta != 0) {
            mStepHandler.increaseStep();
        }
    }

    @Override
    public synchronized float[] getRotateAxis() {
        return mModel.getRotateAxis();
    }

    @Override
    public synchronized float getRotateAngle() {
        float[] pos1 = ArraysUtils.getArr(mStartNormPos[0], mStartNormPos[1], 0);
        float[] pos2 = ArraysUtils.getArr(mCurrNormPos[0], mCurrNormPos[1], 0);
        float[] vec = VectorUtils.getVec(pos1, pos2);
        float dis = VectorUtils.getVecNorm(vec);
        float angle = AngleUtils.getMagneticAngle(dis * ROTATE_TATE);
        float[] zVec = ArraysUtils.getArr(0f, 0f, 1f);
        float[] planeRotateAxis = VectorUtils.getCrossProduct(zVec, vec); // zVec X vec
        boolean isAcute = VectorUtils.isAcuteAngle(planeRotateAxis, mRotateAxis);
        return isAcute ? angle : -angle;
    }

    private synchronized void getTouchedSquare(SquareEnvoy outSquareRecord, float[] normPos) {
        outSquareRecord.reset();
        mModel.getTouchedSquare(outSquareRecord, normPos);
    }

    private float[] adjustNormPos() {
        mCurrNormPos[0] = mCurrNormPos[0] * (mWidth / mHeight);
        return mCurrNormPos;
    }
}
