package com.example.andrew.dontgetsquashed;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    public static int width;
    public static int height;
    public static AssetManager assets;
    public static Context m_Context;
    private OpenGLView openGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openGLView = new OpenGLView(this);
        setContentView(openGLView);
        assets = this.getAssets();
        m_Context = this;
        //Log.d("MAIN","Set the view");
    }

    public void updateWindowMetrics(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
    }

    protected void onResume(){
        super.onResume();
        updateWindowMetrics();
        openGLView.onResume();
    }

    protected void onPause(){
        super.onPause();
        updateWindowMetrics();
        openGLView.onPause();
    }

}
