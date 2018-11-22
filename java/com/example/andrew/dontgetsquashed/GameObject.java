package com.example.andrew.dontgetsquashed;

import android.view.MotionEvent;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by andrew on 29/06/18.
 */

public interface GameObject {

    void update();
    void draw(float[] mvpMatrix);
    void onTouch(MotionEvent e);
}
