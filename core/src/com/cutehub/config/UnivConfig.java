package com.cutehub.config;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Array;
import com.cutehub.GameObject;

import java.util.HashSet;

public class UnivConfig {
    private static UnivConfig univConfig;
    // user defined //
    public static int numberOfPlatformVisible = 17;
    public static float shootSpeed = 20;

    public EnvConfig envConfig = EnvConfig.getInstance();
    public CarConfigs carConfigs = CarConfigs.getInstance();
    public float currentGameSpeed = 3;
    public float increaseShapeAt = 15;
    public int numberOfMatchShapes = 2;
    public int numberOfMatchesPerPowerShoot = 5;
    public float uiAnimationSpeed = 50f;
    public Array<GameObject> renderArray = new Array<>();
    public boolean gameOver = false;

    public static UnivConfig getInstance() {
        if (univConfig == null) {
            univConfig = new UnivConfig();
        }
        return univConfig;
    }

    public static UnivConfig removeInstanceAndAssignNew() {
        univConfig = null;
        return getInstance();
    }

    public HashSet<String> getListOfModelNameToLoad() {
        HashSet<String> stringHashSet = new HashSet<>();
        stringHashSet.addAll(envConfig.giveMeTheModelNameAsSet());
        stringHashSet.addAll(carConfigs.giveMeTheModelNameAsSet());
        return stringHashSet;
    }
}
