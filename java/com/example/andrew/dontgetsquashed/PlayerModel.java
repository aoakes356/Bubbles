package com.example.andrew.dontgetsquashed;

import android.util.Log;
import android.view.MotionEvent;

public class PlayerModel implements GameObject {

    private static float scale = .00001f;    // model position scaling. lower => higher
    private float maxForce = 1.0f;
    private float[] angleInfo = {0.0f,0.0f,0.0f,0.0f};
    public float[] m_modelPos = {0.0f,0.0f};
    private float angle;
    public float springConstant = 40000f;
    private SquashedPhysics m_modelPhysics;         // For player model "Animation".
    public float[] mPosTemp = {0.0f,0.0f};

    public PlayerModel(){
        m_modelPhysics = new SquashedPhysics(this, false, true, 600000.0f);
        m_modelPhysics.setMaxVelocity(.00065f);
    }
    public PlayerModel(float area, float springConstant){
        m_modelPhysics = new SquashedPhysics(this, false, true, area);
        m_modelPhysics.setMaxVelocity(.00065f);
        this.springConstant = springConstant;
    }

    @Override
    public void update() {
        //----------------------------------------------------------------------------------------
        // Model Physics HERE --------------------------------------------------------------------
        //----------------------------------------------------------------------------------------
        float[] deltaPos;
        mPosTemp[0] = m_modelPos[0]/scale;  // Scale the position values so they are more noticeable.
        mPosTemp[1] = m_modelPos[1]/scale;
        if(Math.abs(mPosTemp[0]) > maxForce){ // Check if the value is greater than the chosen maximum value.
            if(mPosTemp[0] < maxForce){
                mPosTemp[0] = -maxForce;
            }else {
                mPosTemp[0] = maxForce;
            }
        }if(Math.abs(mPosTemp[1]) > maxForce){
            if(mPosTemp[1] < 0){
                mPosTemp[1] = -maxForce;
            }else {
                mPosTemp[1] = maxForce;
            }
        }
        if(Math.abs(mPosTemp[0]) > Math.abs(mPosTemp[1])){  //if mpostemp[0] > mpostemp[1] that means mpostemp[0] is "a" in the ellipse formula.
            // Dividing stretches and multiplying compresses.
            angleInfo[2] = 1/(mPosTemp[0]*mPosTemp[0]+1);   //  These calculations are done outside of the shader so its only calculated once per frame.
            angleInfo[3] = (mPosTemp[1]*mPosTemp[1]+1);     //  same.
        }else{
            angleInfo[2] = 1/(mPosTemp[1]*mPosTemp[1]+1);
            angleInfo[3] = (mPosTemp[0]*mPosTemp[0]+1);
        }
        if(mPosTemp[0] != 0) {
            angle = (float) (Math.atan(mPosTemp[1] / mPosTemp[0])+Math.PI/2);         //in radians. -atan(-y/x)+pi/2 to get the angle of the major axis.

        }else{
            angle = (float)Math.PI/2;
        }

        angleInfo[0] = (float)Math.sin(angle);
        angleInfo[1] = (float)(Math.cos(angle));
        //-------------------------------------------------------------------------------
        //----- END OF MODEL PHYSICS-----------------------------------------------------
        //-------------------------------------------------------------------------------

        m_modelPhysics.addAcceleration(-springConstant*(m_modelPos[0]),-springConstant*(m_modelPos[1]));
        m_modelPhysics.exertForce(.033f);
        deltaPos = m_modelPhysics.deltaPos(.033f);
        m_modelPos[0] += deltaPos[0];
        m_modelPos[1] += deltaPos[1];
    }

    public void collide(float angle, GameObject object){
        SquashedPhysics physics = object.getPhysics();
        m_modelPhysics.m_netAcceleration[0] += Math.sin(Math.toRadians(angle))+Math.sin(Math.toRadians(angle))*(Math.pow(physics.m_velocity[0],2.0)*5);
        m_modelPhysics.m_netAcceleration[1] += Math.cos(Math.toRadians(angle))+Math.cos(Math.toRadians(angle))*(Math.pow(physics.m_velocity[1],2.0)*5);
    }

    @Override
    public void draw() {

    }

    @Override
    public void onTouch(MotionEvent e) {

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
        return m_modelPhysics;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public PlayerModel getPlayerModel() {
        return this;
    }

    @Override
    public void setActive(boolean b) {

    }

    @Override
    public void setPosition(float[] pos) {

    }

    @Override
    public void setCharge(int charge) {

    }

    @Override
    public float[] getColor() {
        return new float[0];
    }

    public float[] getAngleInfo() {
        return angleInfo;
    }

    public float[] getModelPos() {
        return m_modelPos;
    }

    public float[] getmPosTemp() {
        return mPosTemp;
    }

    public SquashedPhysics getModelPhysics() {
        return m_modelPhysics;
    }
}
