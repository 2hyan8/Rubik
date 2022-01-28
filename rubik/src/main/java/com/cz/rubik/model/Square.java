package com.cz.rubik.model;

import android.opengl.GLES30;

import com.cz.rubik.utils.tools.ArraysUtils;
import com.cz.rubik.utils.render.ResourcesUtils;
import com.cz.rubik.utils.render.TextureUtils;
import com.cz.rubik.utils.tools.VectorUtils;

import java.nio.FloatBuffer;

/*
 * 方块类
 * 方块编号: 按照左、右、下、上、后、前的顺序编号
 * mLocation: 0~5对应：左、右、下、上、后、前
 */
public class Square extends AbstractModel {
    private static final int VERTEX_NUM = 4; // 每个方块顶点数
    private static final float BASE_SQUARE_SIDE = 1.8f; // 每个方块的基准边长
    private float mSquareSide; // 每个方块的边长
    private boolean mIsFace; // 方块是否处于魔方的表面
    private volatile float[][] mVertex = new float[VERTEX_NUM][DIMENSION_3]; // 顶点坐标数组
    private float[][] mTexture = new float[VERTEX_NUM][DIMENSION_2]; // 纹理坐标数组
    private volatile int mMipmap; // 贴图资源
    private volatile transient FloatBuffer mVertexBuffer;
    private transient FloatBuffer mTextureBuffer;
    private int mTextureId;
    private float[] mTempOldVertex = new float[VERTEX_NUM * DIMENSION_3];
    private float[] mTempNewVertex = new float[VERTEX_NUM * DIMENSION_3];

    public Square(int id, int[] location, AbstractModel parent) {
        super(id, location, parent);
        mSquareSide =  BASE_SQUARE_SIDE / Rubik.sOrder_1;
    }

    @Override
    public synchronized void onCreate() {
        int axis = mLocation[0] / 2; // 坐标分量不变的轴，或三视图方向：左视图、仰视图、后视图
        float forward = mLocation[0] % 2 - 0.5f; // 相对小立方体中心的朝向
        float[] pos = getCubePosition();
        for (int i = 0; i < VERTEX_NUM; i++) {
            for (int j = 0; j < DIMENSION_3; j++) {
                if (j == axis) {
                    mVertex[i][j] = (pos[j] + forward) * mSquareSide;
                } else if (j == (axis + 1) % DIMENSION_3) {
                    mVertex[i][j] = (pos[j] + i % 2 - 0.5f) * mSquareSide;
                } else {
                    mVertex[i][j] = (pos[j] + i / 2 - 0.5f) * mSquareSide;
                }
            }
            mTexture[i][0] = i % 2;
            mTexture[i][1] = i / 2;
        }
        int[] cubeLoc = getParentLocation();
        mMipmap = ResourcesUtils.getMipmap(cubeLoc, mLocation[0]);
        mIsFace = ResourcesUtils.isFace(mMipmap);
    }

    @Override
    public synchronized void onChangeConfig() {
        mTextureId = TextureUtils.loadTexture(mMipmap);
        mVertexBuffer = ArraysUtils.getFloatBuffer(mVertex);
        mTextureBuffer = ArraysUtils.getFloatBuffer(mTexture);
    }

    @Override
    public synchronized void onDraw() {
        //准备顶点坐标和纹理坐标
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, mVertexBuffer);
        GLES30.glVertexAttribPointer(1, 2, GLES30.GL_FLOAT, false, 0, mTextureBuffer);
        //激活纹理
        GLES30.glActiveTexture(GLES30.GL_TEXTURE);
        //绑定纹理
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTextureId);
        //绘制方块
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, VERTEX_NUM);
    }

    @Override
    public synchronized void getTouchedSquare(SquareEnvoy outSquareRecord, float[] pos) {
        if (!mIsFace || !mTransform.isHit(mVertex, pos)) {
            return;
        }
        float[] tempPos = mTransform.getPosInModelCoord(pos);
        float dis = VectorUtils.getDistance(getCenter(), tempPos);
        if (outSquareRecord.isFurther(dis)) {
            outSquareRecord.setTo(this, dis);
        }
    }

    @Override
    public synchronized void onChangeVertex() {
        ArraysUtils.arr2Dto1D(mVertex, mTempOldVertex);
        mTransform.updateVertex(mTempOldVertex, mTempNewVertex);
        ArraysUtils.arr1Dto2D(mTempNewVertex, mVertex);
        mVertexBuffer = ArraysUtils.getFloatBuffer(mVertex);
    }

    @Override
    public synchronized void onChangeLocation(int rotateLayer, int rotateAngle) {}

    @Override
    public synchronized float[] getCenter() {
        return VectorUtils.getCenter(mVertex[1], mVertex[2]);
    }

    public boolean isFace() {
        return mIsFace;
    }

    public synchronized int getMap() {
        return mMipmap;
    }

    public synchronized void setMap(int mipmap) {
        mMipmap = mipmap;
    }

    private float[] getCubePosition() {
        int[] loc = getParentLocation();
        float delta = (Rubik.sOrder_1 - 1.0f) / 2;
        float[] pos = ArraysUtils.allAdd(loc, -delta);
        return pos;
    }
}
