package com.example.andrew.dontgetsquashed;

/**
 * Created by andrew on 29/06/18.
 */

public class SquashedThread extends Thread {
    public static final long MAX_FPS = 30;
    public long timePerFrame = 1000/MAX_FPS;
    private OpenGLView m_view;
    private boolean m_running;
    public SquashedThread(OpenGLView view){
        m_view = view;
    }

    @Override
    public void run(){
        long startTime;
        long waitTime;
        while(m_running){
            startTime = System.nanoTime();
            synchronized (m_view) {
                m_view.requestRender();
            }
            try{
                waitTime = timePerFrame - ((System.nanoTime()-startTime)/1000000);
                if(waitTime > 0) {
                    sleep(waitTime);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void setRunning(boolean running){
        m_running = running;
    }

}
