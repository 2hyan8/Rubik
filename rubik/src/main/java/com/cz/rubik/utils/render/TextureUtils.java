package com.cz.rubik.utils.render;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;

import com.cz.rubik.activity.MainActivity;
import com.cz.rubik.activity.RubikActivity;

/*
 * 纹理加载工具类
 */
public class TextureUtils {
    //加载纹理贴图
    public static int loadTexture(int resourceId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(RubikActivity.getContext().getResources(), resourceId, options);
        final int[] textureIds = new int[1];
        // 生成纹理id
        GLES30.glGenTextures(1, textureIds, 0);
        // 绑定纹理到OpenGL
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureIds[0]);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR_MIPMAP_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        // 加载bitmap到纹理中
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);
        // 生成MIP贴图
        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D);
        // 取消绑定纹理
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        return textureIds[0];
    }
}
