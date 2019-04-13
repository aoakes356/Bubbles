package com.example.andrew.dontgetsquashed;

import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

public class GLNumber implements GameObject{
    public ArrayList<GLDigit> m_glDigits;
    private GLText m_settings;
    private String m_current;
    private float m_space = -.065f;
    public GLNumber(String number){
        m_settings = new GLText();
        m_glDigits = new ArrayList<>();
        GLDigit temp;
        float pos = 0.0f;
        for(int i = 0; i < number.length(); i++){
            temp = new GLDigit(m_settings,new float[]{pos,0.0f});
            temp.setDigit(Character.getNumericValue(number.charAt(i)));
            m_glDigits.add(i, temp);
            pos+= m_space;
        }
        m_current = number;
    }

    public void setDigits(String digits){
        GLDigit temp;
        if(m_current.equals(digits)){
            return;
        }else{
            m_current = digits;
        }
        float pos = 0.0f;
        Log.d("setDigit","Setting digits to new number"+ digits);
        for(int i = 0; i < m_glDigits.size(); i++){
            temp = m_glDigits.get(i);
            if(i < digits.length()) {
                Log.d("setDigit","Setting digit to "+digits.charAt(i));
                temp.setDigit(Character.getNumericValue(digits.charAt(i)));
            }else{
                Log.d("setDigit","Setting digit to NULL");
                temp.setDigit(null);
            }
            pos += m_space;
        }
        for(int i = m_glDigits.size(); i < digits.length(); i++ ) {
            Log.d("setDigit","Adding a new digit"+digits.charAt(i));
            temp = new GLDigit(m_settings, new float[]{pos, 0.0f});
            temp.setDigit(Character.getNumericValue(digits.charAt(i)));
            m_glDigits.add(i, temp);
            pos += m_space;

        }
    }

    @Override
    public void update() {
        for(int i = 0; i < m_glDigits.size(); i++) {
            m_glDigits.get(i).update();
        }
    }

    @Override
    public void draw() {
        for(int i = 0; i < m_glDigits.size(); i++) {
            m_glDigits.get(i).draw();
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

    }

    @Override
    public void setCharge(int charge) {

    }

    @Override
    public float[] getColor() {
        return new float[0];
    }
}
