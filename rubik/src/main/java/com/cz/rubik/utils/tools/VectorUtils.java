package com.cz.rubik.utils.tools;

/*
 * 向量工具类（向量基本运算）
 */
public class VectorUtils {
    public static final float EPSILON = 0.000001f;

    // 生成点1到点2的向量
    public static float[] getVec(float[] pos1, float[] pos2) {
        float[] vec = new float[pos1.length];
        for (int i = 0; i < pos1.length; i++) {
            vec[i] = pos2[i] - pos1[i];
        }
        return vec;
    }

    // 计算向量范数/模长
    public static float getVecNorm(float[] vec) {
        double norm = 0;
        for (int i = 0; i < vec.length; i++) {
            norm += vec[i] * vec[i];
        }
        return (float) Math.sqrt(norm);
    }

    // 计算2点之间的距离
    public static float getDistance(float[] pos1, float[] pos2) {
        double dis = 0;
        for (int i = 0; i < pos1.length; i++) {
            dis += (pos1[i] - pos2[i]) * (pos1[i] - pos2[i]);
        }
        return (float) Math.sqrt(dis);
    }

    // 计算2点的中心
    public static float[] getCenter(float[] pos1, float[] pos2) {
        float[] pos = new float[pos1.length];
        for (int i = 0; i < pos1.length; i++) {
            pos[i] = (pos1[i] + pos2[i]) / 2;
        }
        return pos;
    }

    // 计算向量内积
    public static float getInnerProduct(float[] vec1, float[] vec2) {
        double innerProduct = 0;
        for (int i = 0; i < vec1.length; i++) {
            innerProduct += vec1[i] * vec2[i];
        }
        return (float) innerProduct;
    }

    // 计算向量叉乘
    public static float[] getCrossProduct(float[] vec1, float[] vec2) {
        float[] vec = new float[vec1.length];
        vec[0] = vec1[1] * vec2[2] - vec1[2] * vec2[1];
        vec[1] = vec1[2] * vec2[0] - vec1[0] * vec2[2];
        vec[2] = vec1[0] * vec2[1] - vec1[1] * vec2[0];
        return vec;
    }

    // 计算向量叉乘，只返回z值
    public static float getCrossProductZ(float[] vec1, float[] vec2) {
        return vec1[0] * vec2[1] - vec1[1] * vec2[0];
    }

    // 计算2向量的夹角
    public static float getAngle(float[] vec1, float[] vec2) {
        double innerProduct = 0;
        double norm1 = 0;
        double norm2 = 0;
        for (int i = 0; i < vec1.length; i++) {
            innerProduct += vec1[i] * vec2[i];
            norm1 += vec1[i] * vec1[i];
            norm2 += vec2[i] * vec2[i];
        }
        norm1 = Math.sqrt(norm1);
        norm2 = Math.sqrt(norm2);
        float theta = (float) (Math.acos(innerProduct / norm1 / norm2) / Math.PI * 180);
        return theta;
    }

    // 判断2向量的夹角是否是锐角
    public static boolean isAcuteAngle(float[] vec1, float[] vec2) {
        float innerProduct = getInnerProduct(vec1, vec2);
        return innerProduct > 0;
    }
}
