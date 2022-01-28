package com.cz.rubik.model;

import com.cz.rubik.utils.tools.ArraysUtils;
import com.cz.rubik.utils.business.LayerUtils;
import com.cz.rubik.utils.business.RotateLayerUtils;
import com.cz.rubik.utils.tools.VectorUtils;

/*
 * 小立方体类
 * 小立方体编号: 从左到右、下到上、后到前编号
 */
public class Cube extends AbstractModel {
    private static final int SQUARE_NUM = 6; // 每个小立方体方块数
    private Square[] mSquares = new Square[SQUARE_NUM];

    public Cube(int id, int[] location, AbstractModel parent) {
        super(id, location, parent);
        for (int i = 0; i < mSquares.length; i++) { // 按照左、右、下、上、后、前的顺序遍历
            int squareId = id * mSquares.length + i;
            mSquares[i]= new Square(squareId, ArraysUtils.getArr(i), this);
        }
    }

    @Override
    public synchronized void onCreate() {
        forStubModels(mSquares, square -> square.onCreate());
    }

    @Override
    public synchronized void onChangeConfig() {
        forStubModels(mSquares, square -> square.onChangeConfig());
    }

    @Override
    public synchronized void onDraw() {
        forStubModels(mSquares, square -> square.onDraw());
    }

    @Override
    public synchronized void getTouchedSquare(SquareEnvoy outSquareRecord, float[] pos) {
        forStubModels(mSquares, square -> square.getTouchedSquare(outSquareRecord, pos));
    }

    @Override
    public synchronized void onChangeVertex() {
        forStubModels(mSquares, square -> square.onChangeVertex());
    }

    @Override
    public synchronized void onChangeLocation(int rotateLayer, int rotateAngle) {
        int axis = rotateLayer / Rubik.sOrder_1;
        RotateLayerUtils.rotate(mSquares, axis, rotateAngle);
        setToStubModels(mSquares, (cube, i) -> cube.setLocation(ArraysUtils.getArr(i)));
        forStubModels(mSquares, square -> square.onChangeLocation(rotateLayer, rotateAngle));
    }

    @Override
    public synchronized float[] getCenter() {
        float[] center1 = mSquares[0].getCenter();
        float[] center2 = mSquares[1].getCenter();
        return VectorUtils.getCenter(center1, center2);
    }

    public void getRoundSquare(int layer, Square[] out) {
        int axis = layer / Rubik.sOrder_1;
        Boolean[] isFaces = getFromSquares(mSquares, Boolean.class, (square -> square.isFace()));
        LayerUtils.getLayerRound(mSquares, axis, isFaces, out);
    }

    public Square getSquare(int index) {
        return mSquares[index];
    }
}
