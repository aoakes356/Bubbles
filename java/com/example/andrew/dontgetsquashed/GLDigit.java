package com.example.andrew.dontgetsquashed;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class GLDigit implements GameObject {

    public String m_digit;
    private GLText m_settings;
    public Bitmap m_bitMap;
    public int m_texture;
    public float[] m_pos;
    public Square m_square;
    public boolean m_active;
    public float[] mvpMatrix;
    public SquashedShader m_shader;
    public int m_program;
    private int m_MVPMatrix;
    private float DEPTH = 0.0f;


    public GLDigit(GLText text, float[] position){
        m_settings = text;
        m_pos = position;

        m_square = new Square();
        m_active = true;
        String vertexShaderCode = "", fragmentShaderCode = "";
        // GLSL programs.
        try {
            //fragmentShaderCode = new String(Files.readAllBytes(Paths.get("fragmentShader.glsl")), StandardCharsets.UTF_8);

            java.util.Scanner s = new java.util.Scanner(MainActivity.assets.open("textFragmentShader.glsl")).useDelimiter("\\A");
            fragmentShaderCode = s.hasNext() ? s.next() : "";
            java.util.Scanner sc = new java.util.Scanner(MainActivity.assets.open("textVertexShader.glsl")).useDelimiter("\\A");
            vertexShaderCode = sc.hasNext() ? sc.next() : "";
            Log.d("FILE","File turned into string, success.");
        }catch (IOException e){
            Log.d("FILE","Failed.");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            Log.d("FILE",sStackTrace);
        }
        mvpMatrix = new float[16];
        m_shader = new SquashedShader(vertexShaderCode,fragmentShaderCode);
        m_program = GLES20.glCreateProgram();
        GLES20.glAttachShader(m_program, m_shader.getVert());
        GLES20.glAttachShader(m_program,m_shader.getFrag());
        GLES20.glLinkProgram(m_program);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw() {
        if(m_digit != "") {
            Matrix.multiplyMM(mvpMatrix, 0, SquashedRenderer.m_ProjectionMatrix, 0, SquashedRenderer.m_ViewMatrix, 0);
            //-------------------------------------------------------------------------------
            //-----GLSL VARIABLES AND INITIALIZATIONS----------------------------------------


            //-------------------------------------------------------------------------------
            //Log.d("ANGLEINFO","[0]"+Float.toString(angleInfo[0])+" [1]"+Float.toString(angleInfo[1])+" [2]"+Float.toString(angleInfo[2])+" [3]"+Float.toString(angleInfo[3]));
            // Variables given to the shader.
            int position, texCoord, texture;

            GLES20.glUseProgram(m_program);
            //Attributes
            position = GLES20.glGetAttribLocation(m_program, "vPosition");
            texCoord = GLES20.glGetAttribLocation(m_program, "texcoord");
            GLES20.glEnableVertexAttribArray(position);
            GLES20.glEnableVertexAttribArray(texCoord);
            // GLES20.glVertextAttribPointer(openGLLocation,Components per vertext attribute, type of data, normalize?, distance between components, pointer to first element);
            GLES20.glVertexAttribPointer(position, Square.COORDS_PER_VERTEX, GLES20.GL_FLOAT, true, 4 * Square.COORDS_PER_VERTEX, m_square.m_vertexBuffer);
            GLES20.glVertexAttribPointer(texCoord, 2, GLES20.GL_FLOAT, true, 8, m_square.m_textureBuffer);

            //Uniforms
            //color = GLES20.glGetUniformLocation(m_program,"vColor");
            texture = GLES20.glGetUniformLocation(m_program, "texture");
            GLES20.glActiveTexture(GLES20.GL_TEXTURE);

            // Bind the texture to this unit.
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
            m_bitMap = m_settings.generateTexture(m_digit,32);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_settings.m_textures[0]);

//Create Nearest Filtered Texture
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

//Different possible texture parameters, e.g. GLES20.GL_CLAMP_TO_EDGE
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, m_bitMap, 0);
            // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.

            GLES20.glUniform1i(texture, 0);
            m_MVPMatrix = GLES20.glGetUniformLocation(m_program, "uMVPMatrix");
            //Log.d("ANGLE", "sin: "+angleInfo[0]+ " cos: "+angleInfo[1]);
            Matrix.translateM(mvpMatrix, 0, m_pos[0], m_pos[1], DEPTH);
            GLES20.glUniformMatrix4fv(m_MVPMatrix, 1, false, mvpMatrix, 0);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
            GLES20.glDisableVertexAttribArray(position);
            GLES20.glDisableVertexAttribArray(texCoord);
        }
    }

    @Override
    public void onTouch(MotionEvent e) {

    }

    @Override
    public void collide(float angle, GameObject object) {

    }

    @Override
    public boolean contains(GameObject object) {
        return false;
    }

    @Override
    public float[] getCoords() {
        return new float[0];
    }

    @Override
    public float getRadius() {
        return 0;
    }

    @Override
    public SquashedPhysics getPhysics() {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public PlayerModel getPlayerModel() {
        return null;
    }

    @Override
    public void setActive(boolean b) {

    }

    @Override
    public void setPosition(float[] pos) {
        m_pos = pos;
    }

    @Override
    public void setCharge(int charge) {

    }

    @Override
    public float[] getColor() {
        return new float[0];
    }

    public void setDigit(Integer digit){
        if(digit == null){
            m_digit = "";
        }else {
            m_digit = digit.toString();
            m_bitMap = m_settings.generateTexture(m_digit,32);
        }
    }
}
