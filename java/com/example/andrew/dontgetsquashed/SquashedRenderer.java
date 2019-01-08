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
    public CollisionManager m_collisions;
    private int frameCount;
    public SquashedPlayer m_player;
    public Electron m_enemy;
    public Bubble [] m_burbles = new Bubble[20];
    public static final float[] m_ProjectionMatrix = new float[16];
    public static final float[] m_ViewMatrix = new float[16];
    public int electrons = 0;
    public GLDigit m_score;
    public SquashedRenderer(){

    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(.0f,.0f,.0f,1.0f);
        frameCount = 0;
        m_player = new SquashedPlayer();
        m_enemy = new Electron();
        m_collisions = new CollisionManager();
        m_collisions.addObject(m_player);
        GLText numbers = new GLText();
        m_score = new GLDigit(numbers, new float[]{0,0});
        m_score.setDigit(new Integer(1));
        //m_collisions.addObject(m_enemy);
        /*for(int i = 0; i < 1; i++){
            m_burbles[i] = new Electron();
            m_burbles[i].m_pos[1] = (float) (Math.random()*.5);
            m_burbles[i].m_pos[0] = (float) (.5*Math.random());
            m_collisions.addObject(m_burbles[i]);
        }
        for(int i = 1; i < 2; i++){
            m_burbles[i] = new Proton();
            m_burbles[i].m_pos[1] = (float) (-.5*Math.random());
            m_burbles[i].m_pos[0] = (float) (-.5*Math.random());
            m_collisions.addObject(m_burbles[i]);
        }*/
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
        //m_player.update();
        //m_player.draw();
        //m_enemy.update();
        //m_enemy.draw();
        frameCount++;
        synchronized (m_collisions.objects) {

            if (frameCount >= 60) {
                frameCount = 0;
                Bubble e;
                if (Math.random() > .3+(electrons/5.0)) {
                    e = new Electron();
                    electrons++;
                } else {
                    e = new Proton();
                    electrons--;
                }
                e.m_pos[1] = (float) (1.8*Math.random()-.9);
                e.m_pos[0] = (float) ((Math.random())-.4);
                float dx = m_player.m_pos[0]-e.m_pos[0];
                float dy = m_player.m_pos[1]-e.m_pos[1];
                float dist = (float) Math.sqrt(dx*dx+dy*dy);
                Log.d("Spawned","Distance from spawn: "+dist);
                if(dist < .1){
                    e.m_pos[0] -= (dx/dist)*.1;
                    e.m_pos[1] -= (dy/dist)*.1;
                }
                m_collisions.addObject(e);
            }

            for (GameObject a : m_collisions.objects) {
                a.update();
                a.draw();

            }
        }
        m_score.setDigit((int) m_player.m_playerPhysics.m_charge);
        m_score.draw();
        m_collisions.collisionCheck();
        //Log.d("SPAM","Drawing a frame");
        // Call game object's draw stuff here.
    }

    public void onTouch(MotionEvent e){
        m_player.onTouch(e);
        m_enemy.onTouch(e);
        synchronized (m_collisions.objects) {
            for (GameObject a : m_collisions.objects) {

                a.onTouch(e);

            }
        }
    }

}
