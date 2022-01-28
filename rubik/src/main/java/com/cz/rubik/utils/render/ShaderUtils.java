package com.cz.rubik.utils.render;

import android.opengl.GLES30;

import com.cz.rubik.activity.RubikActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/*
 * 着色器工具类
 */
public class ShaderUtils {
    //通过外部资源编译着色器
    public static int compileShader(int type, int shaderId){
        String shaderCode = readShaderFromResource(shaderId);
        return compileShader(type, shaderCode);
    }

    //通过代码片段编译着色器
    public static int compileShader(int type, String shaderCode){
        int shader = GLES30.glCreateShader(type);
        GLES30.glShaderSource(shader, shaderCode);
        GLES30.glCompileShader(shader);
        return shader;
    }

    //链接到着色器
    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        final int programId = GLES30.glCreateProgram();
        //将顶点着色器加入到程序
        GLES30.glAttachShader(programId, vertexShaderId);
        //将片元着色器加入到程序
        GLES30.glAttachShader(programId, fragmentShaderId);
        //链接着色器程序
        GLES30.glLinkProgram(programId);
        return programId;
    }

    //从shader文件读出字符串
    private static String readShaderFromResource(int shaderId) {
        InputStream is = RubikActivity.getContext().getResources().openRawResource(shaderId);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
