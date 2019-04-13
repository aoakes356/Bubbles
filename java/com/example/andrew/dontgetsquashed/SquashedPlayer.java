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

public class SquashedPlayer extends Bubble{


    private boolean m_down;
    private float[] m_tapCoords;

    public SquashedPlayer(){
        super(.002f,9000000f,8000.0f, 150000f, new float[]{0.0f,1.0f,0.0f},1f);
        m_playerPhysics.m_charge = 1;
    }

    @Override
    public void update() {
        //super.m_playerPhysics.addGravity();
        if(m_down){
            tapForce(m_tapCoords[0],m_tapCoords[1], 45);
        }
        super.update();

    }

    @Override
    public void draw() {
        super.draw();

    }


    public void tapForce(float x, float y, float magnitude){ // Calculates the magnitude and direction of the force exerted by each tap.
        super.tapForce(x,y,magnitude);

    }

    @Override
    public void onTouch(MotionEvent e) {
        float[] coords = {-(e.getX()/MainActivity.width-.5f),-2*((e.getY()/MainActivity.height)-.5f),0.0f,0.0f};// Adjust the x and y coordinates so they are on the same scale as the MVP matrix
        // x goes from .5 to -.5, y goes from 1 to -1
        Log.d("TAPPED","x: "+Float.toString(coords[0])+" y: "+Float.toString(coords[1]));
        if(e.getAction() == MotionEvent.ACTION_MOVE) {
            tapForce(coords[0], coords[1], 10);
        }else if(e.getAction() == MotionEvent.ACTION_DOWN){
            m_down = true;
            m_tapCoords = coords;
            //tapForce(coords[0], coords[1], 100);
        }else if(e.getAction() == MotionEvent.ACTION_UP){
            m_down = false;
        }

    }
    @Override
    public void collide(float angle, GameObject object) {
        super.collide(angle,object);
        m_playerPhysics.m_charge += object.getPhysics().m_charge;
        object.setActive(false);
        if(m_playerPhysics.m_charge <= 0){
            m_active = false;
        }
        m_color[0] = ((float)m_playerPhysics.m_charge)/20.0f;
        m_aura = (float) Math.sqrt(m_playerPhysics.m_charge);
        m_radius = (float) (.002*m_aura);
    }

    @Override
    public String getType(){
        return "Player";
    }

}
