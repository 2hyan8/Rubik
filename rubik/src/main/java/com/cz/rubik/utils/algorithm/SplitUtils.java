package com.cz.rubik.utils.algorithm;

/*
 * 三维数组分割算法工具类
 */
public class SplitUtils {

    // 将三维数组划分为2部分
    public static <T> void split(T[][][] arr, T[] part1, T[][] part2, int axis, int value) {
        // part2中的坐标一定要按照 (x,y), (y,z), (z,x) 的顺序排列，即右手准则排列，否则旋转时坐标就不对
        if (axis == 0) {
            for (int j = 0; j < arr[value].length; j++) {
                for (int k = 0; k < arr[value][j].length; k++) {
                    part2[j][k] = arr[value][j][k];
                }
            }
        } else if (axis == 1) {
            for (int k = 0; k < arr[0][value].length; k++) {
                for (int i = 0; i < arr.length; i++) {
                    part2[k][i] = arr[i][value][k];
                }
            }
        } else {
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[i].length; j++) {
                    part2[i][j] = arr[i][j][value];
                }
            }
        }
        if (part1 == null) return;
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                for (int k = 0; k < arr[i][j].length; k++) {
                    int tempIndex = getIndex(i, j ,k, axis);
                    if (tempIndex != value) {
                        part1[index++] = arr[i][j][k];
                    }
                }
            }
        }
    }

    // 将划分的三维数组恢复
    public static <T> void restore(T[][][] arr, T[][] part, int axis, int value) {
        // part中的坐标一定要按照 (x,y), (y,z), (z,x) 的顺序排列，即右手准则排列，否则旋转时坐标就不对
        if (axis == 0) {
            for (int j = 0; j < arr[value].length; j++) {
                for (int k = 0; k < arr[value][j].length; k++) {
                    arr[value][j][k] = part[j][k];
                }
            }
        } else if (axis == 1) {
            for (int k = 0; k < arr[0][value].length; k++) {
                for (int i = 0; i < arr.length; i++) {
                    arr[i][value][k] = part[k][i];
                }
            }
        } else {
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[i].length; j++) {
                    arr[i][j][value] = part[i][j];
                }
            }
        }
    }

    private static <T>  int getIndex(int i, int j, int k, int axis) {
        if (axis == 0) return i;
        if (axis == 1) return j;
        return k;
    }
}
