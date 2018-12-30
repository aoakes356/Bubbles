package com.example.andrew.dontgetsquashed;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by andrew on 28/06/18.
 */

public class SquashedRenderer implements GLSurfaceView.Renderer {
    SquashedPlayer m_player;
    Electron m_enemy;
    public static final float[] m_ProjectionMatrix = new float[16];
    public static final float[] m_ViewMatrix = new float[16];

    public SquashedRenderer(){

    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.0f,.0f,.0f,.0f);
        m_player = new SquashedPlayer();
        m_enemy = new Electron();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        GLES20.glViewport(0,0,i, i1);
        float ratio = (float)i/i1;
        Matrix.frustumM(m_ProjectionMatrix,0,-ratio,ratio,-1,1,3,7);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        Matrix.setLookAtM(m_ViewMatrix,0,0,0,-3,0f,0f,0f,0f,1.0f,0.0f);
        m_player.update();
        m_player.draw();
        m_enemy.update();
        m_enemy.draw();
        //Log.d("SPAM","Drawing a frame");
        // Call game object's draw stuff here.
    }

    public void onTouch(MotionEvent e){
        m_player.onTouch(e);
        m_enemy.onTouch(e);
    }

}
