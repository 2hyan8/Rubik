package com.cz.rubik.utils.algorithm;

/*
 * 旋转数组算法工具类
 */
public class RotateArrUtils {

    // 旋转一维数组
    public static <T> void rotate(T[] arr, int step) {
        int absStep = Math.abs(step) % arr.length;
        int num = arr.length / absStep;
        for (int i = 0; i < absStep; i++) {
            T temp = arr[i];
            int index = i;
            int pre = (i - step + arr.length) % arr.length;
            for (int j = 0; j < num - 1; j++) {
                arr[index] = arr[pre];
                index = pre;
                pre = (index - step + arr.length) % arr.length;
            }
            arr[index] = temp;
        }
    }

    // 旋转二维数组
    public static <T> void rotate(T[][] arr, int step) {
        int ringNum = arr.length / 2;
        for (int i = 0; i < ringNum; i++) {
            rotateRing(arr, step, i);
            step -= 2;
        }
    }

    // 旋转二维数组中的一个环
    private static <T> void rotateRing(T[][] arr, int step, int ringSeq) {
        int absStep = Math.abs(step);
        int len = arr.length - ringSeq * 2 - 1;
        int num = len * 4 / absStep;
        int[] first = new int[] {ringSeq, ringSeq};
        for (int k = 0; k < absStep; k++) {
            int[] index = first;
            T temp = arr[index[0]][index[1]];
            int[] pre = getPreIndex(index, step, ringSeq, len);
            for (int l = 0; l < num - 1; l++) {
                arr[index[0]][index[1]] = arr[pre[0]][pre[1]];
                index = pre;
                pre = getPreIndex(index, step, ringSeq, len);
            }
            arr[index[0]][index[1]] = temp;
            first = getPreIndex(first, -1, ringSeq, len);
        }
    }

    // 获取旋转环中当前位置的前一个位置（环逆时针方向为正方向）
    private static int[] getPreIndex(int[] currIndex, int step, int ringSeq, int len) {
        int num = len * 4;
        int curr = currIndex[0] + currIndex[1] - ringSeq * 2;
        if (currIndex[0] < currIndex[1]) {
            curr = num - curr;
        }
        int pre = (curr - step + num) % num;
        int[] preIndex = new int[2];
        if (pre < len) {
            preIndex[0] = ringSeq + pre;
            preIndex[1] = ringSeq;
        } else if (pre < 2 * len) {
            preIndex[0] = ringSeq + len;
            preIndex[1] = ringSeq + pre - len;
        } else if (pre < 3 * len) {
            preIndex[0] = ringSeq + 3 * len - pre;
            preIndex[1] = ringSeq + len;
        } else {
            preIndex[0] = ringSeq;
            preIndex[1] = ringSeq + 4 * len - pre;
        }
        return preIndex;
    }
}