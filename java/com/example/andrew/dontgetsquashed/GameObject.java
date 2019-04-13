package com.example.andrew.dontgetsquashed;

import android.view.MotionEvent;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by andrew on 29/06/18.
 */

public interface GameObject {

    void update();
    void draw();
    void onTouch(MotionEvent e);
    void collide(float angle, GameObject object);
    boolean contains(GameObject object);
    float[] getCoords();
    float getRadius();
    SquashedPhysics getPhysics();
    String getType();
    boolean isActive();
    PlayerModel getPlayerModel();
    void setActive(boolean b);
    void setPosition(float[] pos);
    void setCharge(int charge);
    float[] getColor();
}
