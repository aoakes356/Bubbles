package com.example.andrew.dontgetsquashed;

import android.util.Log;

/**
 * Created by andrew on 01/07/18.
 */

public class SquashedPhysics {

    public static final float GRAVITY = -9.81f;
    private float area = 1.0f; // Tweak this to adjust drag force.
    private GameObject m_matter;
    private boolean m_gravity;
    private boolean m_drag;
    private boolean m_collide;
    public float m_collision_angle;
    private float m_maxVelocity = 100f;
    public float[] m_netAcceleration = {0.0f, 0.0f};
    public float[] m_velocity = {0.00001f, 0.0f};
    private float[] m_direction = {0.0f,0.0f};
    private float[] drag;

    public SquashedPhysics(GameObject object, boolean gravity, boolean drag){
        m_matter = object;
        m_gravity = gravity;
        m_drag = drag;
        m_collide = false;
    }
    public SquashedPhysics(GameObject object, boolean gravity, boolean drag,float area){
        this(object,gravity,drag);
        this.area = area;
    }
    public SquashedPhysics(GameObject object, boolean gravity, boolean drag,float area, float maxVelocity) {
        this(object,gravity,drag,area);
        m_maxVelocity = maxVelocity;
    }

    public SquashedPhysics(GameObject object){
        this(object, true, true);
    }

    public void addAcceleration(float fx,float fy){
        m_netAcceleration[0] += fx;
        m_netAcceleration[1] += fy;
    }
    public void addGravity(){
        m_netAcceleration[1] += GRAVITY;
    }
    // Values for time should be in seconds.
    public void exertForce(float time) {
        if (m_drag) {
            drag = dragForce();
//            Log.d("DRAGFORCE","FX "+Float.toString(drag[0])+" FY "+Float.toString(drag[1]));

            m_netAcceleration[0] += drag[0];
            m_netAcceleration[1] += drag[1];
        }
        if(m_collide){
            m_netAcceleration[0] -= Math.sin(m_collision_angle)*Math.abs(m_netAcceleration[0]);
            m_netAcceleration[1] -= Math.cos(m_collision_angle)*Math.abs(m_netAcceleration[1]);
        }
        m_velocity[0] += m_netAcceleration[0]*time/100.0;
        m_velocity[1] += m_netAcceleration[1]*time/100.0;
        float magn = (float)Math.sqrt(m_velocity[0]*m_velocity[0]+m_velocity[1]*m_velocity[1]);
        if(magn > m_maxVelocity){
            m_velocity[0] -= (m_velocity[0]/magn)*(magn-m_maxVelocity);
            m_velocity[1] -= (m_velocity[1]/magn)*(magn-m_maxVelocity);
        }
  //      Log.d("VELOCITY","VX "+Float.toString(m_velocity[0])+" VY "+Float.toString(m_velocity[1]));

        m_direction[0] = m_velocity[0]*(SquashedPhysics.invSqrt(Math.abs(magn)));
        m_direction[1] = m_velocity[1]*(SquashedPhysics.invSqrt(Math.abs(magn)));
        // Reset
        //Log.d("FORCE","FX "+Float.toString(m_previousNet[0])+" FY "+Float.toString(m_previousNet[1]));
        m_netAcceleration[0] = 0.0f;
        m_netAcceleration[1] = 0.0f;
        m_collide = false;
    }

    public float[] deltaPos(float time){
        float[] pos = {0.0f,0.0f};
        pos[0] += m_velocity[0]*time/5;
        pos[1] += m_velocity[1]*time/5;
        return pos;
    }

    public float[] dragForce(){
        float[] dragf = {0.0f,0.0f};
        if(m_velocity[0] < 0) {
            //dragf[0] = area * .2454f * m_velocity[0] * m_velocity[0];
            dragf[0] = (float)((area * .2454f * Math.sqrt(Math.abs(m_velocity[0] * m_velocity[0] * m_velocity[0])))/Math.log(2+50000*Math.abs(m_velocity[0])));
        }else{
            //dragf[0] = -area * .2454f * m_velocity[0] * m_velocity[0];
            dragf[0] = -(float)((area * .2454f * Math.sqrt(Math.abs(m_velocity[0] * m_velocity[0] * m_velocity[0])))/Math.log(2+50000*Math.abs(m_velocity[0])));
        }
        if(m_velocity[1] < 0) {
            //dragf[1] = area * .2454f * m_velocity[1] * m_velocity[1];
            dragf[1] = (float)((area * .2454f * Math.sqrt(Math.abs(m_velocity[1] * m_velocity[1] * m_velocity[1])))/Math.log(2+50000*Math.abs(m_velocity[1])));

        }else{
            //dragf[1] = -area * .2454f * m_velocity[1] * m_velocity[1];
            dragf[1] = -(float)((area * .2454f * Math.sqrt(Math.abs(m_velocity[1] * m_velocity[1] * m_velocity[1])))/Math.log(2+50000*Math.abs(m_velocity[1])));

        }
        return  dragf;
    }

    public void collide(float angle){
        if(!m_collide) {
            m_collision_angle = (float)Math.toRadians(angle);
            m_velocity[0] -= 2*Math.sin(m_collision_angle)*Math.abs(m_velocity[0]);
            m_velocity[1] -= 2*Math.cos(m_collision_angle)*Math.abs(m_velocity[1]);

            m_collide = true;

        }
    }

    // Quake engine inverse sqrt. wew, not amazingly accurate.
    public static float invSqrt(float x) {
        float xhalf = 0.5f * x;
        int i = Float.floatToIntBits(x);
        i = 0x5f3759df - (i >> 1);
        x = Float.intBitsToFloat(i);
        x *= (1.5f - xhalf * x * x);
        return x;
    }

    public float[] getNetAcceleration() {return m_netAcceleration; }
    public float[] getNetVelocity(){return  m_velocity;}
    public float[] get_direction(){return m_direction;}
    public void setMaxVelocity(float velocity){m_maxVelocity=velocity;}
}
