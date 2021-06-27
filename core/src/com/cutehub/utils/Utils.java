package com.cutehub.utils;

import com.badlogic.gdx.math.Vector3;

import java.util.Random;

public class Utils {

    static Random random = new Random();

    public static Vector3 addVectors(Vector3 one, Vector3 two) {
        return new Vector3(one.x + two.x, one.y + two.y, one.z + two.z);
    }

    public static int getRandomInt(int max) {
        int returnNum = random.nextInt(max);
        return returnNum;
    }

    public static float getRandomFloat(int max){
        float returnFloat = random.nextFloat() + getRandomInt(max);
        return returnFloat;
    }
}
