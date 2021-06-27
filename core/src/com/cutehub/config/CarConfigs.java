package com.cutehub.config;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.cutehub.GameObject;

import java.util.HashMap;
import java.util.HashSet;

public class CarConfigs {
    private static CarConfigs carConfigsInstance;
    private static HashMap<CAR_TYPE, String> carTypesMap;
    private static HashMap<CANNON_TYPE, String> cannonTypesMap;

    public CarConfigs() {
    }

    public static CarConfigs getInstance() {
        if (carConfigsInstance == null) {
            carConfigsInstance = new CarConfigs();
            carConfigsInstance.fillCarTypeHashMap();
        }
        return carConfigsInstance;
    }

    public void fillCarTypeHashMap() {
        if (carTypesMap == null) {
            carTypesMap = new HashMap<CAR_TYPE, String>();
            cannonTypesMap = new HashMap<>();
        }
        carTypesMap.put(CAR_TYPE.CAR1, "envAsset/car/car1Own.g3dj");
        cannonTypesMap.put(CANNON_TYPE.CANNON1, "envAsset/cannon/cannon1.g3dj");
    }

    public String getCarNameOf(CAR_TYPE car_type) {
        return carTypesMap.get(car_type);
    }

    public String getCannonNameOf(CANNON_TYPE cannon_type) {
        return cannonTypesMap.get(cannon_type);
    }

    public enum CAR_TYPE {
        CAR1, CAR2
    }

    public enum CANNON_TYPE {
        CANNON1, CANNON2
    }


    public HashSet giveMeTheModelNameAsSet() {
        HashSet returnSet = new HashSet();
        returnSet.addAll(carTypesMap.values());
        returnSet.addAll(cannonTypesMap.values());
        return returnSet;
    }
}
