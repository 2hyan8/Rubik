package com.cz.rubik.utils.business;

import com.cz.rubik.utils.tools.ArraysUtils;
import com.cz.rubik.utils.algorithm.RotateArrUtils;

/*
 * 旋转层相关算法工具类
 */
public class RotateLayerUtils {

    // 旋转小立方体的周围4个方块
    public static <T> void rotate(T[] arr, int axis, int theta) {
        if (theta == 0) return;
        int[] rotateIndex = getRotateIndex(axis);
        T[] rotateArr = ArraysUtils.getStub(arr, rotateIndex);
        int step = getStep(1, theta);
        RotateArrUtils.rotate(rotateArr, step);
        ArraysUtils.override(arr, rotateIndex, rotateArr);
    }

    // 旋转一层的所有小立方体
    public static <T> void rotate(T[][] arr, int theta) {
        if (theta == 0) return;
        int step = getStep(arr.length - 1, theta);
        RotateArrUtils.rotate(arr, step);
    }

    // 获取一维数组中需要旋转的元素索引
    public static int[] getRotateIndex(int axis) {
        if (axis == 0) return ArraysUtils.getArr(2, 4, 3, 5);
        if (axis == 1) return ArraysUtils.getArr(0, 5, 1, 4);
        return ArraysUtils.getArr(0, 2, 1, 3);
    }

    // 旋转时元素需要移动的步数
    private static int getStep(int len, int theta) {
        int step = Math.abs(theta) / 90 * len;
        return theta > 0 ? step : -step;
    }
}