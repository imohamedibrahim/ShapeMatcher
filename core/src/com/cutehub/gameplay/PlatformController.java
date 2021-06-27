package com.cutehub.gameplay;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.cutehub.GameObject;
import com.cutehub.GlobalReferences;
import com.cutehub.utils.CallBack;
import com.cutehub.utils.Utils;
import com.cutehub.enums.PLAY_SHAPE;
import com.cutehub.property.PlatformProperty;

import java.util.ArrayList;

public class PlatformController {
    public int numberOfPlatformSpawned = 0;
    public Model currentPlatformModel;
    public ArrayList<GameObject> platformList = new ArrayList<>();
    public ArrayList<GameObject> platformListForPlayerController = new ArrayList<>();
    public ArrayList<GameObject> unMatchedPlatformList = new ArrayList<>();
    private ArrayList<GameObject> toBeDeletedList = new ArrayList<>();
    private ArrayList<CallBack> addCallBackList = new ArrayList<>();
    private ArrayList<CallBack> removeCallBackList = new ArrayList<>();
    public boolean canAddBlocker = false;
    private static GlobalReferences globalReferences = GlobalReferences.getInstance();
    private static PlatformController platformController;

    public static PlatformController getInstance() {
        if (platformController == null) {
            platformController = new PlatformController();
        }
        return platformController;
    }

    public static PlatformController removeInstanceAndAssignNew() {
        platformController = null;
        return getInstance();
    }

    public void assignPlatformModel(Model model) {
        currentPlatformModel = model;
    }

    public void addToCallBackList(CallBack callBack) {
        addCallBackList.add(callBack);
    }

    public void removeFromCallBackList(CallBack callBack) {
        removeCallBackList.add(callBack);
    }

    public void initiatePlatforms(Model model) {
        assignPlatformModel(model);
        for (int i = 0; i < globalReferences.univConfig.numberOfPlatformVisible; i++) {
            addNewPlatform();
        }
    }

    private void addNewPlatform() {
        GameObject tmpGameObject = new GameObject(currentPlatformModel, "roadBlock" + numberOfPlatformSpawned);
        numberOfPlatformSpawned++;
        tmpGameObject.transform.setToRotation(Vector3.X, 0).trn((platformList.size() == 0) ? new Vector3(0, -0.2f, 0) : Utils.addVectors(platformList.get(platformList.size() - 1).transform.getTranslation(new Vector3()), new Vector3(0, 0, 5f)));
        addPlatformProperty(tmpGameObject);
        addToLists(tmpGameObject);
    }

    private void addPlatformProperty(GameObject gameObject) {
        PLAY_SHAPE[] play_shapes = PLAY_SHAPE.values();
        int index = (numberOfPlatformSpawned > 5 && canAddBlocker)? Utils.getRandomInt(globalReferences.univConfig.numberOfMatchShapes + 1) : 0;
        PLAY_SHAPE play_shape = play_shapes[index];
        PlatformProperty platformProperty = new PlatformProperty(play_shape, gameObject);
        platformProperty.isShapedMapped = false;
        if (play_shape == PLAY_SHAPE.PLAIN)
            platformProperty.isShapedMapped = true;
        gameObject.propertiesHashMap.put(GlobalReferences.PLATFORM_PROPERTY, platformProperty);
    }

    private void addToLists(GameObject tmpGameObject) {
        for (CallBack callBack : addCallBackList) {
            callBack.call(tmpGameObject);
        }
        platformList.add(tmpGameObject);
        platformListForPlayerController.add(tmpGameObject);
        if (!((PlatformProperty) tmpGameObject.propertiesHashMap.get(GlobalReferences.PLATFORM_PROPERTY)).playShape.equals(PLAY_SHAPE.PLAIN))
            unMatchedPlatformList.add(tmpGameObject);
        globalReferences.univConfig.renderArray.add(tmpGameObject);
    }

    public void removeGameObjectFromList(GameObject tmpGameObject) {
        platformListForPlayerController.remove(tmpGameObject);
        toBeDeletedList.add(tmpGameObject);
        if (toBeDeletedList.size() > 5) {
            GameObject toBeDeleted = toBeDeletedList.remove(0);
            for (CallBack callBack : removeCallBackList) {
                callBack.call(toBeDeleted);
            }
            platformList.remove(toBeDeleted);
            globalReferences.univConfig.renderArray.removeValue(toBeDeleted, true);
        }
    }

    public void addModelToInstance(Array<ModelInstance> instances) {
        for (GameObject tmpGameObject : platformList) {
            instances.add(tmpGameObject);
        }
    }

    public void update(float dt) {
        if (platformListForPlayerController.size() < globalReferences.univConfig.numberOfPlatformVisible) {
            addNewPlatform();
        }
        for (GameObject platform : platformList) {
            ((PlatformProperty) platform.propertiesHashMap.get(GlobalReferences.PLATFORM_PROPERTY)).update(dt);
        }
    }
}
