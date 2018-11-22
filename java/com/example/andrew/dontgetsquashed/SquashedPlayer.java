package com.example.andrew.dontgetsquashed;

import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by andrew on 30/06/18.
 */

public class SquashedPlayer implements GameObject{

    private float DEPTH = 0.0f;
    private float maxVelocity = .1f;
    private float tx,ty,fx,fy,tdist;
    private static final float[] COLOR = {1.0f,0.0f,0.0f,1.0f};
    private static final float[] SCREEN = {(float)MainActivity.width,(float)MainActivity.height};
    private float[] mvpMatrix;
    private int m_MVPMatrix;

    private Square m_square;
    private SquashedShader m_shader;
    private SquashedPhysics m_playerPhysics;        // For center of mass movement;
    private int m_program;
    public float[] deltaPos = {0.0f, 0.0f};
    public float[] m_pos = {0.0f,0.0f};
    private PlayerModel m_PlayerModel;

    public SquashedPlayer(){
        m_square = new Square();
        String vertexShaderCode = "", fragmentShaderCode = "";
        // GLSL programs.
        try {
            //fragmentShaderCode = new String(Files.readAllBytes(Paths.get("fragmentShader.glsl")), StandardCharsets.UTF_8);

            java.util.Scanner s = new java.util.Scanner(MainActivity.assets.open("fragmentShader.glsl")).useDelimiter("\\A");
            fragmentShaderCode = s.hasNext() ? s.next() : "";
            java.util.Scanner sc = new java.util.Scanner(MainActivity.assets.open("vertexShader.glsl")).useDelimiter("\\A");
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

        m_shader = new SquashedShader(vertexShaderCode,fragmentShaderCode);
        m_program = GLES20.glCreateProgram();
        GLES20.glAttachShader(m_program, m_shader.getVert());
        GLES20.glAttachShader(m_program,m_shader.getFrag());
        GLES20.glLinkProgram(m_program);
        m_playerPhysics = new SquashedPhysics(this,false, true,70000);    // setup the physics on the game object.
        m_playerPhysics.setMaxVelocity(maxVelocity);
        m_PlayerModel = new PlayerModel();
    }
    public SquashedPlayer(float[] matrix){
        this();
        mvpMatrix = matrix;
    }

    @Override
    public void update() {
        m_playerPhysics.addGravity();
        if(Math.abs(m_pos[0]) > .3 ) {
            m_playerPhysics.collide(90);
        }if (Math.abs(m_pos[1]) > .7){
            m_playerPhysics.collide(0);
        }
        m_PlayerModel.update();
        m_playerPhysics.exertForce(.033f);
        deltaPos = m_playerPhysics.deltaPos(.033f);
        m_pos[0] += deltaPos[0]*100;
        m_pos[1] += deltaPos[1]*100;

        //Log.d("MODEL_POS","X: "+Float.toString(m_modelPos[0])+" Y: " +Float.toString(m_modelPos[1]));
        //Log.d("MODEL_FORCE","X: "+Float.toString(m_modelPhysics.getNetAcceleration()[0])+" Y: " +Float.toString(m_modelPhysics.getNetAcceleration()[1]));

    }

    @Override
    public void draw(float[] mvpMatrix) {
        this.mvpMatrix = mvpMatrix;

        //-------------------------------------------------------------------------------
        //-----GLSL VARIABLES AND INITIALIZATIONS----------------------------------------


        //-------------------------------------------------------------------------------
        //Log.d("ANGLEINFO","[0]"+Float.toString(angleInfo[0])+" [1]"+Float.toString(angleInfo[1])+" [2]"+Float.toString(angleInfo[2])+" [3]"+Float.toString(angleInfo[3]));
        // Variables given to the shader.
        int position, color, screen, texCoord, aInfo;
        GLES20.glUseProgram(m_program);
        //Attributes
        position = GLES20.glGetAttribLocation(m_program, "vPosition");
        GLES20.glEnableVertexAttribArray(position);
        // GLES20.glVertextAttribPointer(openGLLocation,Components per vertext attribute, type of data, normalize?, distance between components, pointer to first element);
        GLES20.glVertexAttribPointer(position,Square.COORDS_PER_VERTEX,GLES20.GL_FLOAT,true,4*Square.COORDS_PER_VERTEX,m_square.m_vertexBuffer);
        //Uniforms
        //color = GLES20.glGetUniformLocation(m_program,"vColor");
        screen = GLES20.glGetUniformLocation(m_program,"uScreen");
        Log.d("Ratio","Ratio: "+(SCREEN[0]/SCREEN[1])/.5);
        texCoord = GLES20.glGetUniformLocation(m_program,"inputTextureCoordinate");
        aInfo = GLES20.glGetUniformLocation(m_program,"uAngles");
        int uAB = GLES20.glGetUniformLocation(m_program,"uAB");
        //GLES20.glUniform4fv(color,1,COLOR,0);
        GLES20.glUniform2fv(screen, 1,SCREEN,0);
        GLES20.glUniform2fv(texCoord,1,m_pos,0);
        Log.d("Player Position",Float.toString(m_pos[0])+","+Float.toString(m_pos[1]));
        GLES20.glUniform4fv(aInfo,1,m_PlayerModel.getAngleInfo(),0);
        GLES20.glUniform2fv(uAB,1,m_PlayerModel.getmPosTemp(),0);
        m_MVPMatrix = GLES20.glGetUniformLocation(m_program,"uMVPMatrix");
        //Log.d("ANGLE", "sin: "+angleInfo[0]+ " cos: "+angleInfo[1]);
        Matrix.translateM(mvpMatrix,0,m_pos[0],m_pos[1],DEPTH);
        GLES20.glUniformMatrix4fv(m_MVPMatrix,1,false,mvpMatrix,0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,4);
        GLES20.glDisableVertexAttribArray(position);

    }


    public void tapForce(float x, float y, float magnitude){ // Calculates the magnitude and direction of the force exerted by each tap.
        //Log.d("POSITION","x: "+Float.toString(m_pos[0])+" y: "+Float.toString(m_pos[1]));
        tx =(m_pos[0]-x);
        ty =(m_pos[1]-y);
        Log.d("Tap Distance", Float.toString(tx)+","+Float.toString(ty));
        float squared = tx*tx+ty*ty;
        tdist = SquashedPhysics.invSqrt(squared);
        fx = magnitude*tx*tdist;
        fy = magnitude*ty*tdist;
        m_playerPhysics.addAcceleration(fx,fy);
        m_PlayerModel.getModelPhysics().addAcceleration(fx/100,fy/100);
        //  Log.d("ACCEL", "Accelerating, wew "+Float.toString(ax)+" x:y "+Float.toString(ay)+" dist "+Float.toString(distance) + " dir "+Float.toString(direction[0])+","+Float.toString(direction[1]));

    }

    @Override
    public void onTouch(MotionEvent e) {
        float[] coords = {-(e.getX()/MainActivity.width-.5f),-2*((e.getY()/MainActivity.height)-.5f),0.0f,0.0f};// Adjust the x and y coordinates so they are on the same scale as the MVP matrix
        // x goes from .5 to -.5, y goes from 1 to -1
        Log.d("TAPPED","x: "+Float.toString(coords[0])+" y: "+Float.toString(coords[1]));
        if(e.getAction() == MotionEvent.ACTION_MOVE) {
            tapForce(coords[0], coords[1], 35);
        }else if(e.getAction() == MotionEvent.ACTION_DOWN){
            tapForce(coords[0], coords[1], 100);
        }

    }

    public void setMVPMatrix(float[] matrix){
        mvpMatrix = matrix;
    }
}
