package com.cz.rubik.utils.render;

import com.cz.rubik.R;
import com.cz.rubik.model.Rubik;

/*
 * 资源工具类
 */
public class ResourcesUtils {
    private static int INSIDE_INDEX = 6;

    private static int[] RESOURCES_ID = new int[] {
            R.drawable.red, R.drawable.orange, R.drawable.green, R.drawable.blue,
            R.drawable.yellow, R.drawable.white, R.drawable.inside
    };

    // 获取mipmap
    public static int getMipmap(int[] cubeLoc, int squareLoc) {
        int axis = squareLoc / 2;
        int deep = cubeLoc[axis] * 2 + squareLoc % 2; // 方块在axis方向下的深度
        int near = axis * 2;
        int far = near + 1;
        int planeNum = Rubik.sOrder_1 * 2;
        int index = (deep == 0 ? near : (deep == planeNum - 1 ? far : INSIDE_INDEX));
        return RESOURCES_ID[index];
    }

    // 判断mipmap是否是魔方的表面
    public static boolean isFace(int resourceId) {
        return resourceId != RESOURCES_ID[INSIDE_INDEX];
    }
}
