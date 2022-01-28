package com.cz.rubik.model;

import java.io.Serializable;

/*
 * 模型接口
 * 定义了模型通用且必须实现的接口
 */
public interface IModel extends Serializable {
    int DIMENSION_2 = 2; // 二维坐标
    int DIMENSION_3 = 3; // 三维坐标
    int DIMENSION_4 = 4; // 四维坐标
    int INVALID_AXIS = -1; // 无效的坐标轴
    int INVALID_LAYER = -1; // 无效的层号，按照左、中1、右、底、中2、上、后、中3、前编号

    // 模型初始化
    void onCreate();

    // 更新模型的配置数据
    void onChangeConfig();

    // 绘制模型
    void onDraw();

    // 获取触摸到的方块信息
    void getTouchedSquare(SquareEnvoy outSquareRecord, float[] pos);

    // 更新模型的顶点数据
    void onChangeVertex();

    // 更新当前模型在父模型中的位置
    void onChangeLocation(int rotateLayer, int rotateAngle);
}