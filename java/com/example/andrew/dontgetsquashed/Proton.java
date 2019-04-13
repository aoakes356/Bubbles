package com.example.andrew.dontgetsquashed;

import android.util.Log;
import android.view.MotionEvent;

public class Proton extends Bubble {
    public Proton(){
        super(.002f,600000f,12000.0f, 80000f, new float[]{1.0f,.5f,0.0f},1f);
        m_playerPhysics.m_charge = (int) (-3*Math.random())-1;
        m_aura = Math.abs(m_playerPhysics.m_charge);
        m_radius = (float) (.002*Math.sqrt(m_aura));
    }
    public Proton(float radius, float tension, float drag,float thicc, float[] color, float aura){
        super(radius, tension, drag, thicc, color, aura);
    }

    @Override
    public void update() {
        //super.m_playerPhysics.addGravity();
        super.update();
    }

    @Override
    public void draw() {
        super.draw();

    }


    public void tapForce(float x, float y, float magnitude){ // Calculates the magnitude and direction of the force exerted by each tap.
        //super.tapForce(x,y,magnitude);

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
    @Override
    public void collide(float angle, GameObject object) {
        super.collide(angle,object);
        int save = object.getPhysics().m_charge;
        object.setCharge(save+m_playerPhysics.m_charge);
        m_playerPhysics.m_charge += save;
        if(object.getType() == getType()){
            object.setActive(false);
        }else if(object.getType() == "Electron" && object.getPhysics().m_charge <= 0){
            object.setActive(false);
        }else if(object.getType() == "Proton" && object.getPhysics().m_charge >= 0){
            object.setActive(false);
        }
        if(m_playerPhysics.m_charge >= 0){
            m_active = false;
        }
        m_aura = Math.abs(m_playerPhysics.m_charge);
        m_radius = (float) (.002*Math.sqrt(m_aura));
    }
    @Override
    public String getType(){
        return "Proton";
    }
}
