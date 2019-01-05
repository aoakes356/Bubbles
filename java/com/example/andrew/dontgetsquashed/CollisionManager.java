package com.example.andrew.dontgetsquashed;

import android.util.Log;

import java.util.ArrayList;

public class CollisionManager {
    public ArrayList<GameObject> objects;

    public CollisionManager(){
    }

    public void addObject(GameObject bubble){
        if(objects == null){
            objects = new ArrayList<>();
        }
        objects.add(bubble);
    }

    public void collisionCheck(){
        GameObject one,two;
        float dx, dy, angle1, angle2;
        float[] polarOne, polarTwo;
        synchronized (objects) {
            for (int i = 0; i < objects.size(); i++) {
                for (int j = i; j < objects.size(); j++) {
                    if (i != j) {
                        one = objects.get(i);
                        SquashedPhysics onePhys = one.getPhysics();
                        two = objects.get(j);
                        SquashedPhysics twoPhys = two.getPhysics();
                        polarOne = one.getCoords();
                        polarTwo = two.getCoords();
                        dx = polarOne[0] - polarTwo[0];
                        dy = polarOne[1] - polarTwo[1];
                        float inv = SquashedPhysics.invSqrt(dx * dx + dy * dy);
                        float electricForce = (float) ((onePhys.m_charge * twoPhys.m_charge) * inv * inv * inv)/100.0f;
                        float fx = dx * inv * electricForce, fy = dy * inv * electricForce;
                        if (one.contains(two)) {
                            angle1 = (float) Math.toDegrees(Math.atan2(dy, dx));
                            angle2 = (float) Math.toDegrees(Math.atan2(-dy, -dx));
                            one.collide(-(angle1 + 90), two);
                            two.getPlayerModel().collide(angle1 + 90, two);
                            onePhys.addAcceleration(20 * (dx * inv), 20 * (dy * inv));
                            twoPhys.addAcceleration(-20 * (dx * inv), -20 * (dy * inv));
                            Log.d("Collide", angle1 + "");
                        } else {
                            onePhys.addAcceleration(fx, fy);
                            twoPhys.addAcceleration(-fx, -fy);
                        }
                        if (!one.isActive()) {
                            objects.remove(one);
                        }
                        if (!two.isActive()) {
                            objects.remove(two);
                        }

                    }
                }
            }
        }
    }
}
