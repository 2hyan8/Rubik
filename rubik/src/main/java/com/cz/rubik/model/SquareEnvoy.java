package com.cz.rubik.model;

import android.content.Context;

import java.io.Serializable;

/*
 * 方块使者类
 * 由PartRotateHandler分发给每一个Square, 并保存触摸到的Square,
 * SquareEnvoy对Square进行了封装, 避免PartRotateHandler直接操作Square,
 * 原则上只有Cube才能操作Square, 外界只能直接操作Rubik, 以保护模型的层级结构
 */
public class SquareEnvoy implements Serializable {
    private Square mSquare = null;
    public float mDistance = Float.MAX_VALUE;

    public SquareEnvoy() {}

    public synchronized void reset() {
        mSquare = null;
        mDistance = Float.MAX_VALUE;
    }

    public synchronized void setTo(Square square, float distance) {
        mSquare = square;
        mDistance = distance;
    }

    public synchronized boolean isValid() {
        if (mSquare == null) {
            return false;
        }
        return true;
    }

    public synchronized boolean isFurther(float dis) {
        return mDistance > dis;
    }

    // 获取层号，从左到右、下到上、后到前依次编号
    public synchronized int getLayer(SquareEnvoy sr, Context context) {
        if (!isDiffSquare(sr)) {
            return IModel.INVALID_LAYER;
        }
        int axis = IModel.INVALID_AXIS;
        int[] pos1 = mSquare.getParentLocation();
        int[] pos2 = sr.mSquare.getParentLocation();
        int dire1 = mSquare.getLocation()[0] / 2;
        int dire2 = sr.mSquare.getLocation()[0] / 2;
        for (int i = 0; i < pos1.length; i++) {
            if (pos1[i] == pos2[i] && i != dire1 && i != dire2) {
                axis = i;
                break;
            }
        }
        if (axis == IModel.INVALID_AXIS) {
            return IModel.INVALID_LAYER;
        }
        int layer = axis * Rubik.sOrder_1 + pos1[axis];
        return layer;
    }

    private boolean isDiffSquare(SquareEnvoy sr) {
        if (isValid() &&  sr.isValid() && mSquare != sr.mSquare) {
            return true;
        }
        return false;
    }

    public int[] getCubeLocation() {
        return mSquare.getParentLocation();
    }

    public int getSquareLocation() {
        return mSquare.getLocation()[0];
    }
}
