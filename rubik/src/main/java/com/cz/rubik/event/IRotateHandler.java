package com.cz.rubik.event;

import java.io.Serializable;

/*
 * 旋转处理器
 */
public interface IRotateHandler extends Serializable {
    // 旋转模式
    int ROTETE_MODE_UNDEFINE = 0;
    int ROTETE_MODE_TOTAL = 1;
    int ROTETE_MODE_PART = 2;

    // 旋转之前的准备工作
    void onHandlerReady();

    // 开始旋转
    boolean onRotateStart(float[] pos);

    // 旋转过程中
    void onRotate(float[] pos);

    // 旋转结束
    void onRotateEnd();

    // 获取旋转轴
    float[] getRotateAxis();

    // 获取旋转角度
    float getRotateAngle();
}
