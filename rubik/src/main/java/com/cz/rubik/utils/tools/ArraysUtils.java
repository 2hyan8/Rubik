package com.cz.rubik.utils.tools;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/*
 * 数组转换工具类
 */
public class ArraysUtils {

    // 封装数组
    public static float[] getArr(float ... values) {
        return values;
    }

    // 封装数组
    public static int[] getArr(int ... values) {
        return values;
    }

    // 创建一维数组
    public static <R> R[] newArr(Class clazz, int length) {
        return (R[]) Array.newInstance(clazz, length);
    }

    // 创建二维数组
    public static <R> R[][] newArr(Class clazz, int length1, int length2) {
        return (R[][]) Array.newInstance(clazz, length1, length2);
    }

    // 获取数组子集
    public static <T> T[] getStub(T[] arr, int[] index) {
        T[] stub = (T[]) Array.newInstance(arr.getClass().getComponentType(), index.length);
        for (int i = 0; i < index.length; i++) {
            stub[i] = arr[index[i]];
        }
        return stub;
    }

    // 覆盖数组
    public static <T> void override(T[] arr, int[] index, T[] stub) {
        for (int i = 0; i < index.length; i++) {
            arr[index[i]] = stub[i];
        }
    }

    // 将二维数组转换为一维数组
    public static void arr2Dto1D(float[][] source, float[] target) {
        int index = 0;
        for (int i = 0; i < source.length; i++) {
            for (int j = 0; j < source[i].length; j++) {
                target[index++] = source[i][j];
            }
        }
    }

    // 将一维数组转换为二维数组
    public static void arr1Dto2D(float[] source, float[][] target) {
        int index = 0;
        for (int i = 0; i < target.length; i++) {
            for (int j = 0; j < target[i].length; j++) {
                target[i][j] = source[index++];
            }
        }
    }

    // 所有元素增加value
    public static float[] allAdd(int[] arr, float value) {
        float[] res = new float[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i] + value;
        }
        return res;
    }

    // 获取一维数组对应的缓存数据
    public static FloatBuffer getFloatBuffer(float[] floatArr) {
        FloatBuffer fb = ByteBuffer.allocateDirect(floatArr.length * Float.BYTES)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer();
        fb.put(floatArr);
        fb.position(0);
        return fb;
    }

    // 获取二维数组对应的缓存数据
    public static FloatBuffer getFloatBuffer(float[][] floatArr) {
        float[] tempFloatArr = new float[floatArr.length * floatArr[0].length];
        arr2Dto1D(floatArr, tempFloatArr);
        FloatBuffer fb = getFloatBuffer(tempFloatArr);
        return fb;
    }
}
