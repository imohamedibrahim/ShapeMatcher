package com.cutehub.property;

import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.cutehub.GameObject;
import com.cutehub.GlobalReferences;
import com.cutehub.config.EnvConfig;
import com.cutehub.enums.ENV_TYPE;
import com.cutehub.enums.PROPS_TYPE;
import com.cutehub.templates.EnvTemplate;
import com.cutehub.utils.Utils;

import java.util.ArrayList;

import javax.swing.text.AttributeSet;

public class EnvProperty {
    EnvConfig envConfig;

    private Vector3 position;
    private Vector3 rotation;
    private EnvTemplate envTemplate;
    private GameObject platformEnvBelongsTo;
    GameObject buildingGameOject;
    GameObject baseGameObject;
    private ArrayList<GameObject> propsGameObjectList = new ArrayList<>();

    public EnvProperty(Vector3 position, Vector3 rotation, EnvTemplate envTemplate, GameObject platformEnvBelongsTo) {
        this.envTemplate = envTemplate;
        this.position = position;
        this.rotation = rotation;
        this.platformEnvBelongsTo = platformEnvBelongsTo;
        this.envConfig = EnvConfig.getInstance();
    }

    private GlobalReferences globalReferences = GlobalReferences.getInstance();

    public void createEnv() {
        buildingGameOject = new GameObject(envTemplate.buildingPlatform, "buildingOf" + platformEnvBelongsTo.name);
        baseGameObject = new GameObject(envTemplate.building, "buildingBaseOf" + platformEnvBelongsTo.name);
        setPositionAndRotation(baseGameObject, buildingGameOject);
        globalReferences.univConfig.renderArray.add(buildingGameOject, baseGameObject);
        buildingGameOject.materials.set(0, new Material());
        createProps();
    }

    private void createProps() {
        if (envTemplate.platform_type == ENV_TYPE.PLATFORM_TYPE.BUILDING) {
            int random = Utils.getRandomInt(3);
            PROPS_TYPE prop_type_array[] = PROPS_TYPE.values();
            for (int i = 0; i < random; i++) {
                PROPS_TYPE props_type = prop_type_array[Utils.getRandomInt(prop_type_array.length - 1)];
                GameObject propGameObject = new GameObject(GlobalReferences.modelLoader.getModel(envConfig.getPropsName(props_type)), "propsOf" + platformEnvBelongsTo.name);
                setPositionAndRotation(propGameObject);
                propsGameObjectList.add(propGameObject);
                globalReferences.univConfig.renderArray.add(propGameObject);
            }
        }
    }


    void setPositionAndRotation(GameObject... gameObjects) {
        for (GameObject gameObject : gameObjects) {
            int offset = 0;// gameObject.name.toLowerCase().contains("base") ? 1 : 0;
            //gameObject.transform.scale(1.5f, 1.5f, 1.5f);
            gameObject.transform.setToTranslation(new Vector3().add(position).add(position.x > 0 ? offset : -offset, 0, 0)).rotate(Vector3.Y, rotation.y);
        }
    }

    public void destroy() {
        for (GameObject tmpGameObject : propsGameObjectList) {
            globalReferences.univConfig.renderArray.removeValue(tmpGameObject, true);
        }
        globalReferences.univConfig.renderArray.removeValue(buildingGameOject, true);
        globalReferences.univConfig.renderArray.removeValue(baseGameObject, true);
    }
}
