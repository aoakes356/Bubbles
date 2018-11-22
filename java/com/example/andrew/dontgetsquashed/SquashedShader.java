package com.example.andrew.dontgetsquashed;

import android.opengl.GLES20;

/**
 * Created by andrew on 30/06/18.
 */

public class SquashedShader {
    private String m_shaderText;
    private int m_vertPosition = 0, m_fragPosition = 0;

    public SquashedShader(String vertexCode, String fragmentCode){
        m_vertPosition = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        m_fragPosition = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        GLES20.glShaderSource(m_vertPosition,vertexCode);
        GLES20.glCompileShader(m_vertPosition);

        GLES20.glShaderSource(m_fragPosition,fragmentCode);
        GLES20.glCompileShader(m_fragPosition);
    }

    public int getFrag(){
        return m_fragPosition;
    }

    public int getVert(){
        return m_vertPosition;
    }

}
