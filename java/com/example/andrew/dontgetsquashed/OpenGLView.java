package com.example.andrew.dontgetsquashed;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by andrew on 28/06/18.
 */

public class OpenGLView extends GLSurfaceView{
    private SquashedPlayer m_player;
    private SquashedRenderer renderer = null;
    private SquashedThread thread;
    public OpenGLView(Context context) {
        super(context);
        init();
    }

    public OpenGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setFocusable(true);
        setEGLContextClientVersion(3);
        setPreserveEGLContextOnPause(true);
        renderer = new SquashedRenderer();
        setRenderer(renderer);
        setRenderMode(OpenGLView.RENDERMODE_WHEN_DIRTY);
        thread = new SquashedThread(this);
        thread.setRunning(true);
        thread.start();
    }

    public boolean onTouchEvent(MotionEvent e){
        //Log.d("Debug","Touch detected");
        // Pass event to game object handlers
        if(renderer != null){
            renderer.onTouch(e);
        }
        return true;
    }

    public void update(){

    }

}
