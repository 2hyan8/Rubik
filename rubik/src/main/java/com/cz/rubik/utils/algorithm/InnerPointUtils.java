package com.cz.rubik.utils.algorithm;

import com.cz.rubik.utils.tools.VectorUtils;

/*
 * 内点判断算法工具类
 */
public class InnerPointUtils {

    // 判断顶点是否在四边形内部，叉乘法一定要保证vertex按顺序排列
    public static boolean isPointInQuadrangle(float[][] vertex, float[] pos) {
        adjustVertexSeq(vertex);
        int num = vertex.length;
        float[][] vec1 = new float[num][];
        float[][] vec2 = new float[num][];
        for (int i = 0; i < num; i++) {
            int n = (i + 1) % num;
            vec1[i] = VectorUtils.getVec(vertex[i], vertex[n]);
            vec2[i] = VectorUtils.getVec(vertex[i], pos);
        }
        int plus = 0; // 正z个数
        int minus = 0; // 负z个数
        for (int i = 0; i < num; i++) {
            float z = VectorUtils.getCrossProductZ(vec1[i], vec2[i]); // 向量叉乘, 只计算z值, x和y都为0
            if (z > 0) {
                plus++;
            } else if (z < 0) {
                minus++;
            }
        }
        if (plus > 0 && minus > 0) {
            return false;
        }
        return true;
    }

    // 调整顶点顺序，使其按顺序排列
    private static void adjustVertexSeq(float[][] vertex) {
        int k = vertex.length - 2;
        for (int i = 0; i < vertex[0].length; i++) {
            float t = vertex[k][i];
            vertex[k][i] = vertex[k+1][i];
            vertex[k+1][i] = t;
        }
    }
}
