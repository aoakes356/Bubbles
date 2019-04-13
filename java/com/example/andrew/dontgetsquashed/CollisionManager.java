package com.example.andrew.dontgetsquashed;

import android.util.Log;

import java.util.ArrayList;

public class CollisionManager {
    public ArrayList<GameObject> objects;
    private SquashedRenderer m_render;
    public CollisionManager(SquashedRenderer renderer){
        m_render = renderer;
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
        float inv, electricForce, fx, fy;
        float[] polarOne, polarTwo;
        int oneCharge, twoCharge;
        synchronized (objects) {
            for (int i = 0; i < objects.size(); i++) {
                for (int j = i; j < objects.size(); j++) {
                    if (i != j) {
                        one = objects.get(i);
                        SquashedPhysics onePhys = one.getPhysics();
                        two = objects.get(j);
                        SquashedPhysics twoPhys = two.getPhysics();
                        oneCharge = onePhys.m_charge;
                        twoCharge = twoPhys.m_charge;
                        polarOne = one.getCoords();
                        polarTwo = two.getCoords();
                        dx = polarOne[0] - polarTwo[0];
                        dy = polarOne[1] - polarTwo[1];
                        inv = SquashedPhysics.invSqrt(dx * dx + dy * dy);
                        electricForce = (float) ((oneCharge * twoCharge) * inv * inv)/25.0f;
                        fx = dx * inv * electricForce;
                        fy = dy * inv * electricForce;
                        if (one.contains(two)) {
                            if (one.getType() == "Player" && two.getType() == "Electron") {
                                m_render.m_score.setDigit((int) m_render.m_player.m_playerPhysics.m_charge + Integer.parseInt(m_render.m_score.m_digit) + twoCharge);
                            } else if (one.getType() == "Electron" && two.getType() == "Player") {
                                m_render.m_score.setDigit((int) m_render.m_player.m_playerPhysics.m_charge + Integer.parseInt(m_render.m_score.m_digit) + oneCharge);
                            }
                            m_render.m_spawnedCount--;
                            angle1 = (float) Math.toDegrees(Math.atan2(dy, dx));
                            angle2 = (float) Math.toDegrees(Math.atan2(-dy, -dx));
                            one.collide(-(angle1 + 90), two);
                            two.getPlayerModel().collide(angle1 + 90, two);
                            onePhys.addAcceleration(20 * (dx * inv), 20 * (dy * inv));
                            twoPhys.addAcceleration(-20 * (dx * inv), -20 * (dy * inv));
                            Log.d("Collide", "Charges Before: " + oneCharge + ", " + twoCharge + "\n Charges After: " + onePhys.m_charge + ", " + twoPhys.m_charge);
                        } else {
                            if(1/inv > 2.1f){
                                if(one.getType() != "Player" && two.getType() == "Player"){
                                    if(oneCharge < 0 ) {
                                        onePhys.addAcceleration(fx * 20000, fy * 20000);
                                    }else{
                                        onePhys.addAcceleration(-fx * 20000, -fy * 20000);
                                    }
                                    twoPhys.addAcceleration(-fx, -fy);
                                }else if(two.getType() != "Player" && one.getType() == "Player"){
                                    onePhys.addAcceleration(fx, fy);
                                    if(twoCharge > 0 ) {
                                        twoPhys.addAcceleration(fx * 20000, fy * 20000);
                                    }else{
                                        twoPhys.addAcceleration(-fx * 20000, -fy * 20000);
                                    }
                                }else{
                                    onePhys.addAcceleration(fx, fy);
                                    twoPhys.addAcceleration(-fx, -fy);
                                }
                            }else{
                                onePhys.addAcceleration(fx, fy);
                                twoPhys.addAcceleration(-fx, -fy);
                            }


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
