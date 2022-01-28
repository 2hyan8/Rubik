package com.cz.rubik.model;

import com.cz.rubik.matrix.Transform;
import com.cz.rubik.utils.business.ShuffleUtils;
import com.cz.rubik.utils.tools.ArraysUtils;
import com.cz.rubik.utils.business.LayerUtils;
import com.cz.rubik.utils.business.RotateLayerUtils;
import com.cz.rubik.utils.algorithm.SplitUtils;
import com.cz.rubik.utils.algorithm.RotateArrUtils;
import com.cz.rubik.utils.tools.VectorUtils;

import java.util.Random;

/*
 * 魔方类
 */
public class Rubik extends AbstractModel {
    public static int sOrder_1 = 1; // 魔方阶数
    public static int sOrder_2 = 1; // 魔方阶数平方
    public static int sOrder_3 = 1; // 魔方阶数立方
    private Cube[][][] mCubes;
    private int mLayer = INVALID_LAYER;
    private ShuffleUtils mShuffleUtils;
    private Cube[] mTempCubesPart1; // 旋转单层时，不动的小立方体数组
    private Cube[][] mTempCubesPart2; // 旋转单层时，需要转动的小立方体数组

    public Rubik(int order) {
        setOrder(order);
        mTransform = new Transform();
        mCubes = new Cube[sOrder_1][sOrder_1][sOrder_1];
        mTempCubesPart1 = new Cube[sOrder_3 - sOrder_2];
        mTempCubesPart2 = new Cube[sOrder_1][sOrder_1];
        mShuffleUtils = new ShuffleUtils(this);
        int index = 0;
        for (int i = 0; i < mCubes.length; i++) { // 从左往右遍历
            for (int j = 0; j < mCubes[i].length; j++) { // 从下往上遍历
                for (int k = 0; k < mCubes[i][j].length; k++) { // 从后往前遍历
                    mCubes[i][j][k] = new Cube(index++, ArraysUtils.getArr(i, j, k), this);
                }
            }
        }
    }

    @Override
    public synchronized void onCreate() {
        forStubModels(mCubes, cube -> cube.onCreate());
        mShuffleUtils.shuffle();
        firstOffset();
    }

    @Override
    public synchronized void onChangeConfig() {
        forStubModels(mCubes, cube -> cube.onChangeConfig());
    }

    @Override
    public synchronized void onDraw() {
        if (mLayer == INVALID_LAYER) {
            mTransform.computeMvpMatrix(true);
            forStubModels(mCubes, cube -> cube.onDraw());
        } else {
            mTransform.computeMvpMatrix(false);
            forStubModels(mTempCubesPart1, cube -> cube.onDraw());
            mTransform.computeMvpMatrix(true);
            forStubModels(mTempCubesPart2, cube -> cube.onDraw());
        }
    }

    @Override
    public synchronized void getTouchedSquare(SquareEnvoy outSquareRecord, float[] pos) {
        forStubModels(mCubes, cube -> cube.getTouchedSquare(outSquareRecord, pos));
    }

    @Override
    public synchronized void onChangeVertex() {
        if (mLayer == INVALID_LAYER) {
            forStubModels(mCubes, cube -> cube.onChangeVertex());
        } else {
            mTransform.computeMvpMatrix(true);
            forStubModels(mTempCubesPart2, cube -> cube.onChangeVertex());
        }
    }

    @Override
    public synchronized void onChangeLocation(int rotateLayer, int rotateAngle) {
        int axis = rotateLayer / sOrder_1;
        int value = rotateLayer % sOrder_1;
        RotateLayerUtils.rotate(mTempCubesPart2, rotateAngle);
        SplitUtils.restore(mCubes, mTempCubesPart2, axis, value);
        setToStubModels(mCubes, (cube, i, j, k) -> cube.setLocation(ArraysUtils.getArr(i, j, k)));
        forStubModels(mTempCubesPart2, cube -> cube.onChangeLocation(rotateLayer, rotateAngle));
    }

    public synchronized void setLayer(int layer) {
        mLayer = layer;
        if (mLayer != INVALID_LAYER) {
            int axis = mLayer / sOrder_1;
            int value = mLayer % sOrder_1;
            SplitUtils.split(mCubes, mTempCubesPart1, mTempCubesPart2, axis, value);
        }
    }

    public synchronized float[] getRotateAxis() {
        int axis = mLayer / sOrder_1;
        int maxIndex = Rubik.sOrder_1 - 1;
        int start = 0;
        int end = maxIndex;
        float[] pos1, pos2, pos3, pos4;
        if (axis == 0) {
            pos1 = mCubes[start][0][0].getCenter();
            pos2 = mCubes[start][maxIndex][maxIndex].getCenter();
            pos3 = mCubes[end][0][0].getCenter();
            pos4 = mCubes[end][maxIndex][maxIndex].getCenter();
        } else if (axis == 1) {
            pos1 = mCubes[0][start][0].getCenter();
            pos2 = mCubes[maxIndex][start][maxIndex].getCenter();
            pos3 = mCubes[0][end][0].getCenter();
            pos4 = mCubes[maxIndex][end][maxIndex].getCenter();
        } else {
            pos1 = mCubes[0][0][start].getCenter();
            pos2 = mCubes[maxIndex][maxIndex][start].getCenter();
            pos3 = mCubes[0][0][end].getCenter();
            pos4 = mCubes[maxIndex][maxIndex][end].getCenter();
        }
        float[] center1 = VectorUtils.getCenter(pos1, pos2);
        float[] center2 = VectorUtils.getCenter(pos3, pos4);
        return VectorUtils.getVec(center1, center2);
    }

    public Cube[][][] getCubes() {
        return mCubes;
    }

    // 设置魔方阶数
    private void setOrder(int order) {
        sOrder_1 = order;
        sOrder_2 = sOrder_1 * order;
        sOrder_3 = sOrder_2 * order;
    }

    private void firstOffset() {
        mTransform.computeModelMatrix(ArraysUtils.getArr(0f, -1f, 0f), 45f);
        onChangeVertex();
        mTransform.computeModelMatrix(ArraysUtils.getArr(1f, 0f, 0f), 30f);
        onChangeVertex();
        mTransform.computeModelMatrix(ArraysUtils.getArr(0f, 0f, 1f), 0f);
    }
}
