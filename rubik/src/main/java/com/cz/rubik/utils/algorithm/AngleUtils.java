package com.cz.rubik.utils.algorithm;

/*
 * 角度相关算法工具类
 */
public class AngleUtils {
    private static final float MAX_ANGLE = 90; // 最大旋转角度
    private static final float MAGNETIC_THRESHOLD = 15f; // 磁力阈值，旋转到0°, 90°, 180° 时，有磁力吸附效果
    private static final float[] MAGNETIC_ANGLE = new float[] {0, 90, 180}; // 需要磁力吸附旋转角度

    // 获取最近的90°的整数倍度数(0, 90, 180)
    public static int getNearAngle(float angle) {
        float theta = Math.abs(angle);
        int res;
        if (theta < 45) {
            res = 0;
        } else if (theta < 135) {
            res = 90;
        } else {
            res = 180;
        }
        return angle > 0 ? res : -res;
    }

    // 旋转重要节点(0, 90, 180)磁力吸附效果
    public static float getMagneticAngle(float angle) {
        int sign = angle >= 0 ? 1 : -1;
        float theta = Math.min(Math.abs(angle), MAX_ANGLE);
        for (int i = 0; i < MAGNETIC_ANGLE.length; i++) {
            if (Math.abs(theta - MAGNETIC_ANGLE[i]) < MAGNETIC_THRESHOLD) {
                return sign * MAGNETIC_ANGLE[i];
            }
        }
        return angle;
    }
}
