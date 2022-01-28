package com.cz.rubik.matrix;

import android.opengl.GLES30;
import android.opengl.Matrix;
import android.util.Log;

import com.cz.rubik.model.IModel;
import com.cz.rubik.utils.tools.ArraysUtils;
import com.cz.rubik.utils.algorithm.InnerPointUtils;

import java.io.Serializable;
import java.util.Arrays;

/*
 * 顶点坐标变换类
 */
public class Transform implements Serializable {
    private static final int OBSER_DIMENSION = 2; // 观测空间维度
    private static final int MATRIX_LENGTH = 16; // 变换矩阵长度
    private static final int OFFSET = 0; // 偏移
    private static final float BASE_NEAR = 6f; // 近平面基础值
    private int mProgramId; // 着色器程序id
    private float mRatio; // surfaceView宽高比
    private volatile float[] mModelMatrix; // 模型变换矩阵
    private float[] mViewMatrix; //观测变换矩阵
    private float[] mProjectionMatrix; // 投影变换矩阵
    private volatile float[] mMvpMatrix; // MVP变换矩阵
    private volatile float[] mAxis = ArraysUtils.getArr(0f, 0f, 1f); // 旋转轴
    private volatile float mAngle = 0; // 旋转角度
    private float[] mEye = ArraysUtils.getArr(0f, 0f, 10f); // 眼睛坐标
    private float[] mCenter = ArraysUtils.getArr(0f, 0f, 0f); // 模型中心坐标
    private float[] mUp = ArraysUtils.getArr(0f, 1f, 0f); // 观察姿态
    private float mNear = BASE_NEAR; // 近平面z值
    private float mFar = 13; // 远平面z值
    float[] mTempIn = new float[IModel.DIMENSION_4];
    float[] mTempOut = new float[IModel.DIMENSION_4];

    public Transform() {}

    public synchronized void init(int programId, float ratio) {
        mProgramId = programId;
        mRatio = ratio;
        mNear = mRatio > 1 ? BASE_NEAR : BASE_NEAR * mRatio;
        mViewMatrix = getIdentityMatrix(MATRIX_LENGTH, OFFSET);
        mProjectionMatrix = getIdentityMatrix(MATRIX_LENGTH, OFFSET);
        Matrix.setLookAtM(mViewMatrix, 0, mEye[0], mEye[1], mEye[2], mCenter[0], mCenter[1], mCenter[2], mUp[0], mUp[1], mUp[2]); // 获取观测变换矩阵
        Matrix.frustumM(mProjectionMatrix, 0, -mRatio, mRatio, -1, 1, mNear, mFar); // 获取投影变换矩阵
    }

    //计算MVP变换矩阵
    public synchronized void computeMvpMatrix(boolean enableRotate) {
        mModelMatrix = getIdentityMatrix(MATRIX_LENGTH, OFFSET);
        if (enableRotate) {
            Matrix.rotateM(mModelMatrix, 0, mAngle, mAxis[0], mAxis[1], mAxis[2]);
        }
        //计算MVP变换矩阵: mvpMatrix = projectionMatrix * viewMatrix * modelMatrix
        float[] tempMatrix = getIdentityMatrix(MATRIX_LENGTH, OFFSET);
        mMvpMatrix = getIdentityMatrix(MATRIX_LENGTH, OFFSET);
        Matrix.multiplyMM(tempMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMvpMatrix, 0, mProjectionMatrix, 0, tempMatrix, 0);
        //设置MVP变换矩阵
        int mvpMatrixHandle = GLES30.glGetUniformLocation(mProgramId, "mvpMatrix");
        GLES30.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mMvpMatrix, 0);
    }

    // 计算模型矩阵
    public synchronized void computeModelMatrix(float[] axis, float angle) {
        setRotateAttr(axis, angle);
        mModelMatrix = getIdentityMatrix(MATRIX_LENGTH, OFFSET);
        Matrix.rotateM(mModelMatrix, 0, mAngle, mAxis[0], mAxis[1], mAxis[2]);
    }

    // 更新顶点坐标
    public synchronized float[] updateVertex(float[] oldVertex, float[] newVertex) {
        int num = oldVertex.length / IModel.DIMENSION_3;
        for (int i = 0; i < num; i++) {
            int offset = i * IModel.DIMENSION_3;
            for (int j = 0; j < IModel.DIMENSION_3; j++) {
                mTempIn[j] = oldVertex[offset + j];
                mTempOut[j] = 0;
            }
            mTempIn[IModel.DIMENSION_3] = 0;
            mTempOut[IModel.DIMENSION_3] = 0;
            Matrix.multiplyMV(mTempOut, 0, mModelMatrix, 0, mTempIn, 0);
            for (int j = 0; j < IModel.DIMENSION_3; j++) {
                newVertex[offset + j] = mTempOut[j];
            }
        }
        return newVertex;
    }

    // 设置旋转参数
    public synchronized void setRotateAttr(float[] axis, float angle) {
        Log.e("xxx-zy", "setRotateAttr: axis: " + Arrays.toString(axis) + ", angle: " + angle);
        for (int i = 0; i < axis.length; i++) {
            mAxis[i] = axis[i];
        }
        mAngle = angle;
    }

    // 触摸点是否命中方块
    public boolean isHit(float[][] square, float[] pos) {
        float[][] tempPos = new float[square.length][OBSER_DIMENSION];
        for (int i = 0; i < square.length; i++) {
            for (int j = 0; j < IModel.DIMENSION_3; j++) {
                mTempIn[j] = square[i][j];
                mTempOut[j] = 0;
            }
            mTempIn[IModel.DIMENSION_3] = 1;
            mTempOut[IModel.DIMENSION_3] = 0;
            Matrix.multiplyMV(mTempOut, 0, mMvpMatrix, 0, mTempIn, 0);
            for (int j = 0; j < OBSER_DIMENSION; j++) {
                tempPos[i][j] = mTempOut[j] / mTempOut[IModel.DIMENSION_3];
            }
        }
        boolean isHit = InnerPointUtils.isPointInQuadrangle(tempPos, pos);
        return isHit;
    }

    // 获取触摸点在模型坐标系中的坐标
    public float[] getPosInModelCoord(float[] pos) {
        return ArraysUtils.getArr(pos[0], pos[1], mEye[2] - mNear);
    }

    // 获取单位矩阵
    private float[] getIdentityMatrix(int size, int offset) {
        float[] matrix = new float[size];
        Matrix.setIdentityM(matrix, offset);
        return matrix;
    }
}
