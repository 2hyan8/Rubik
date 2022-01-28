package com.cz.rubik.render;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.cz.rubik.R;
import com.cz.rubik.model.Rubik;
import com.cz.rubik.utils.render.ShaderUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/*
 * 魔方渲染器
 */
public class RubikRender implements GLSurfaceView.Renderer {
    private Rubik mModel;
    private int mProgramId;

    public RubikRender(Rubik rubik) {
        mModel = rubik;
    }

    @Override
    public synchronized void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
        //设置背景颜色
        GLES30.glClearColor(0f, 0f, 0f, 0f);
        //启动深度测试
        gl.glEnable(GLES30.GL_DEPTH_TEST);
        linkProgram();
    }

    @Override
    public synchronized void onSurfaceChanged(GL10 gl, int width, int height) {
        //设置视图窗口
        GLES30.glViewport(0, 0, width, height);
        float ratio = 1.0f * width / height;
        mModel.getTransform().init(mProgramId, ratio);
        mModel.onChangeConfig();
    }

    @Override
    public synchronized void onDrawFrame(GL10 gl) {
        //将颜色缓冲区设置为预设的颜色
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
        //启用顶点的数组句柄
        GLES30.glEnableVertexAttribArray(0);
        GLES30.glEnableVertexAttribArray(1);
        //启用顶点的数组句柄
        mModel.onDraw();
        GLES30.glDisableVertexAttribArray(0);
        GLES30.glDisableVertexAttribArray(1);
    }

    private void linkProgram() {
        //编译着色器
        final int vertexShaderId = ShaderUtils.compileShader(GLES30.GL_VERTEX_SHADER, R.raw.vertex_shader);
        final int fragmentShaderId = ShaderUtils.compileShader(GLES30.GL_FRAGMENT_SHADER, R.raw.fragment_shader);
        //链接程序片段
        mProgramId = ShaderUtils.linkProgram(vertexShaderId, fragmentShaderId);
        GLES30.glUseProgram(mProgramId);
    }
}
