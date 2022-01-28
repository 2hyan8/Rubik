package com.cz.rubik.utils.business;

import com.cz.rubik.utils.tools.ArraysUtils;

/*
 * 魔方旋转层相关算法工具类
 */
public class LayerUtils {

    // 获取旋转层四周的小立方体(out中的元素必须按照右手准则排列)
    public static  <T> void getLayerRound(T[][][] arr, int axis, int value, T[] out) {
        int index = 0;
        int maxIndex = arr.length - 1;
        int[] point = ArraysUtils.getArr(0, 0);
        if (axis == 0) {
            while (index < out.length) {
                out[index++] = arr[value][point[0]][point[1]];
                nextIndex(point, maxIndex);
            }
        } else if (axis == 1) {
            while (index < out.length) {
                out[index++] = arr[point[1]][value][point[0]];
                nextIndex(point, maxIndex);
            }
        } else {
            while (index < out.length) {
                out[index++] = arr[point[0]][point[1]][value];
                nextIndex(point, maxIndex);
            }
        }
    }

    // 获取小立方体中位于旋转侧面的方块(out中的元素必须按照右手准则排列)
    public static  <T> void getLayerRound(T[] arr, int axis, Boolean[] filter, T[] out) {
        int[] rotateIndex = RotateLayerUtils.getRotateIndex(axis);
        int i = 0;
        boolean pre = filter[rotateIndex[rotateIndex.length - 1]];
        for (; i < rotateIndex.length; i++) {
            boolean now = filter[rotateIndex[i]];
            if (!pre && now) break;
            pre = now;
        }
        out[0] = arr[rotateIndex[i]];
        int next = (i + 1) % rotateIndex.length;
        if (filter[rotateIndex[next]]) {
            out[1] = arr[rotateIndex[next]];
        }
    }

    // 获取下一个索引
    private static void nextIndex(int[] point, int maxIndex) {
        if (point[1] == 0 && point[0] < maxIndex) {
            point[0]++;
        } else if (point[0] == maxIndex && point[1] < maxIndex) {
            point[1]++;
        } else if (point[1] == maxIndex && point[0] > 0) {
            point[0]--;
        } else {
            point[1]--;
        }
    }
}
