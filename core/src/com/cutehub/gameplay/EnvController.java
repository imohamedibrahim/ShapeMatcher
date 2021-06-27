package com.cutehub.gameplay;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.cutehub.GameObject;
import com.cutehub.GlobalReferences;
import com.cutehub.enums.ENV_TYPE;
import com.cutehub.property.EnvProperty;
import com.cutehub.templates.EnvTemplate;
import com.cutehub.utils.CallBack;
import com.cutehub.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class EnvController {
    private static EnvController envController;
    private GlobalReferences globalReferences = GlobalReferences.getInstance();
    private ArrayList<EnvProperty> envPropertyArrayList = new ArrayList<>();
    private HashMap<Object, ArrayList> envpropertyListHashMap = new HashMap<>();
    private CallBack addCallBack;
    private CallBack removeCallBack;
    private int count;
    private int brigeCount;
    private int gardenCount;

    public static EnvController getInstance() {
        if (envController == null) {
            envController = new EnvController();
        }
        return envController;
    }

    public static EnvController removeInstanceAndAssignNew() {
        envController = null;
        return getInstance();
    }

    public void create() {
        addCallBack = new CallBack() {
            @Override
            public void call(Object object) {
                platformAdded(object);
            }
        };
        removeCallBack = new CallBack() {
            @Override
            public void call(Object object) {
                platformRemoved(object);
            }
        };
        getPlatformController().addToCallBackList(addCallBack);
        getPlatformController().removeFromCallBackList(removeCallBack);
    }

    private PlatformController getPlatformController() {
        return ((GamePlayController) globalReferences.controller.mainPlayController.currentPlayController).platformController;
    }

    private void platformAdded(Object object) {
        count++;
        if (count % 2 != 0) return;
        EnvTemplate envTemplate = envTemplate(ENV_TYPE.PLATFORM_TYPE.BUILDING);
        GameObject platformObject = (GameObject) object;
        Vector3 position = platformObject.transform.getTranslation(new Vector3());
        position.add(6.5f, 0, 0);
        Vector3 rotation = new Vector3();
        int rand = Utils.getRandomInt(10);
        if (rand == 11 || brigeCount > 0 && brigeCount <= 3) {
            envTemplate = envTemplate(ENV_TYPE.PLATFORM_TYPE.BRIDGE);
            brigeCount++;
        } else if (rand == 2 || gardenCount > 0 && gardenCount <= 5) {
            brigeCount = 0;
            gardenCount++;
            envTemplate = envTemplate(ENV_TYPE.PLATFORM_TYPE.GARDEN);
        } else {
            brigeCount = 0;
            gardenCount = 0;
        }
        EnvProperty envProperty = new EnvProperty(position, rotation, envTemplate, platformObject);
        Vector3 position1 = platformObject.transform.getTranslation(new Vector3());
        position1.add(-6.5f, 0, 0);
        Vector3 rotation1 = new Vector3(0, 180, 0);
        envProperty.createEnv();
        if (envTemplate.platform_type == ENV_TYPE.PLATFORM_TYPE.BUILDING) {
            envTemplate = envTemplate(ENV_TYPE.PLATFORM_TYPE.BUILDING);
        }
        EnvProperty envProperty2 = new EnvProperty(position1, rotation1, envTemplate, platformObject);
        envProperty2.createEnv();
        ArrayList<EnvProperty> list = new ArrayList();
        list.add(envProperty);
        list.add(envProperty2);
        envpropertyListHashMap.put(object, list);
    }

    private void platformRemoved(Object object) {
        if (!envpropertyListHashMap.containsKey(object)) return;
        ArrayList<EnvProperty> envPropertyArrayList = envpropertyListHashMap.remove(object);
        for (EnvProperty envProperty : envPropertyArrayList) {
            envProperty.destroy();
        }
    }

    public void initializeRoadBlocks() {

    }

    public void addModelToInstance(Array<ModelInstance> instances) {

    }

    public void update(float dt) {

    }

    public EnvTemplate envTemplate(ENV_TYPE.PLATFORM_TYPE platform_type) {
        return EnvTemplate.getTemplate(platform_type);
    }
}
