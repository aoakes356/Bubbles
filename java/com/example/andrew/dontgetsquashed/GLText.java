package com.example.andrew.dontgetsquashed;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.MotionEvent;

public class GLText{

    public int[] m_textures;

    public GLText(){
        m_textures = new int[10];
        Bitmap bitmap = null;
        Canvas canvas;

        Paint textPaint = new Paint();
        textPaint.setTextSize(32);
        textPaint.setAntiAlias(true);
        textPaint.setARGB(0xff, 0xff, 0xff, 0xff);
        GLES20.glGenTextures(10, m_textures, 0);

        for(int i = 0; i < 10; i++) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0+i);
            bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_4444);
            canvas = new Canvas(bitmap);
            bitmap.eraseColor(0);
            Drawable background = MainActivity.m_Context.getResources().getDrawable(R.drawable.w_bg);
            background.setBounds(0, 0, 256, 256);
            background.draw(canvas);
            canvas.drawText(i+"", 16, 112, textPaint);
//...and bind it to our array
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_textures[i]);

//Create Nearest Filtered Texture
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

//Different possible texture parameters, e.g. GLES20.GL_CLAMP_TO_EDGE
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            if (m_textures[i] == 0)
            {
                Log.d("Error","ERROORR AAHHH");
                throw new RuntimeException("Error loading texture."+i);
            }
        }
        bitmap.recycle();
    }

}
