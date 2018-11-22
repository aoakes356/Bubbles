package com.example.andrew.dontgetsquashed;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by andrew on 30/06/18.
 */

public class Triangle {
    public FloatBuffer m_vertexBuffer;
    public static final int COORDS_PER_VERTEX = 3;
    private float m_coords[] = {
        0.0f, 1.0f, 0.0f,   // top
        -1.0f, -1.0f, 0.0f,   // bottom left
        1.0f, -1.0f, 0.0f     // bottom right
    };
    private float color [] = {1.0f,0,0,1.0f};

    public Triangle (){
        ByteBuffer bb = ByteBuffer.allocateDirect(m_coords.length*4);
        bb.order(ByteOrder.nativeOrder());
        m_vertexBuffer = bb.asFloatBuffer();
        m_vertexBuffer.put(m_coords);
        m_vertexBuffer.position(0);
    }

}
