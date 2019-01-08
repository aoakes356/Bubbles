package com.example.andrew.dontgetsquashed;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by andrew on 01/07/18.
 */

public class Square{
    public ShortBuffer m_drawListBuffer;
    public FloatBuffer m_vertexBuffer;
    public FloatBuffer m_textureBuffer;
    public static final int COORDS_PER_VERTEX = 3;
    public static float m_squareCoords[] = {
            -.5f,.5f,0.0f,
            -.5f,-.5f,0.0f,
            0.5f,-0.5f,0.0f,
            0.5f,0.5f,0.0f
    };
    private short drawOrder[] = {0,1,2,0,2,3};

    public Square(){
        ByteBuffer bb = ByteBuffer.allocateDirect(m_squareCoords.length*4);
        bb.order(ByteOrder.nativeOrder());
        m_vertexBuffer = bb.asFloatBuffer();
        m_vertexBuffer.put(m_squareCoords);
        m_vertexBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(2*drawOrder.length);
        dlb.order(ByteOrder.nativeOrder());
        m_drawListBuffer = dlb.asShortBuffer();
        m_drawListBuffer.put(drawOrder);
        m_drawListBuffer.position(0);

        float texture[] = {
                1, 0,
                1, 1,
                0, 1,
                0, 0,
        };

        ByteBuffer tbb = ByteBuffer.allocateDirect(4*texture.length);
        tbb.order(ByteOrder.nativeOrder());
        m_textureBuffer = tbb.asFloatBuffer();
        m_textureBuffer.put(texture);
        m_textureBuffer.position(0);
    }
}
