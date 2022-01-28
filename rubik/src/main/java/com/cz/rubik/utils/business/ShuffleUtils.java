package com.cz.rubik.utils.business;

import com.cz.rubik.model.Cube;
import com.cz.rubik.model.Rubik;
import com.cz.rubik.model.Square;
import com.cz.rubik.utils.algorithm.RotateArrUtils;
import com.cz.rubik.utils.algorithm.SplitUtils;

import java.io.Serializable;
import java.util.Random;

/*
 * 魔方打乱算法工具类
 */
public class ShuffleUtils implements Serializable {
    private Rubik mRubik;

    public ShuffleUtils(Rubik rubik) {
        mRubik = rubik;
    }

    // 打乱魔方
    public void shuffle() {
        int num = mRubik.sOrder_3 * mRubik.sOrder_3;
        Random rand = new Random();
        for (int i = 0; i < num; i++) {
            int layer = rand.nextInt(mRubik.sOrder_1 * 3);
            int sign = (int) Math.round((rand.nextInt(2) - 0.5) * 2);
            initRound(layer, sign * mRubik.sOrder_1);
            initTop(layer, sign * (mRubik.sOrder_1 - 1));
        }
    }

    // 打乱旋转层四周的方块
    private void initRound(int layer, int step) {
        Square[] squares = getLayerRoundSquare(layer);
        Integer[] maps = mRubik.getFromSquares(squares, Integer.class, square -> square.getMap());
        RotateArrUtils.rotate(maps, step);
        mRubik.setToSquares(squares, maps, (square, j) -> square.setMap(j));
    }

    // 打乱旋转层顶部的方块
    private void initTop(int layer, int step) {
        int value = layer % mRubik.sOrder_1;
        if (value > 0 && value < mRubik.sOrder_1 - 1) {
            return;
        }
        Square[][] squares = getLayerTopSquare(layer);
        Integer[][] maps = mRubik.getFromSquares(squares, Integer.class, square -> square.getMap());
        RotateArrUtils.rotate(maps, step);
        mRubik.setToSquares(squares, maps, (square, j) -> square.setMap(j));
    }

    // 获取旋转层四周的方块
    private Square[] getLayerRoundSquare(int layer) {
        int axis = layer / mRubik.sOrder_1;
        int value = layer % mRubik.sOrder_1;
        Cube[] cubes = new Cube[(mRubik.sOrder_1 - 1) * 4];
        LayerUtils.getLayerRound(mRubik.getCubes(), axis, value, cubes);
        Square[] out = new Square[mRubik.sOrder_1 * 4];
        Square[] squares = new Square[2];
        int index = 0;
        for (int i = 0; i < cubes.length; i++) {
            squares[1] = null;
            cubes[i].getRoundSquare(layer, squares);
            out[index++] = squares[0];
            if (squares[1] != null) {
                out[index++] = squares[1];
            }
        }
        return out;
    }

    // 获取旋转层顶部的方块
    private Square[][] getLayerTopSquare(int layer) {
        int axis = layer / mRubik.sOrder_1;
        int value = layer % mRubik.sOrder_1;
        int index = axis * 2 + (value > 0 ? 1 : 0);
        Cube[][] cubes = new Cube[mRubik.sOrder_1][mRubik.sOrder_1];
        SplitUtils.split(mRubik.getCubes(), null, cubes, axis, value);
        Square[][] squares = new Square[mRubik.sOrder_1][mRubik.sOrder_1];
        for (int i = 0; i < cubes.length; i++) {
            for (int j = 0; j < cubes.length; j++) {
                squares[i][j] = cubes[i][j].getSquare(index);
            }
        }
        return squares;
    }
}
