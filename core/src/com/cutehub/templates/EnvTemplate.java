package com.cutehub.templates;

import com.badlogic.gdx.graphics.g3d.Model;
import com.cutehub.GlobalReferences;
import com.cutehub.enums.ENV_TYPE;
import com.cutehub.utils.Utils;

public class EnvTemplate {
    public Model building;
    public Model buildingPlatform;
    public ENV_TYPE.PLATFORM_TYPE platform_type;
    public static EnvTemplate getTemplate(ENV_TYPE.PLATFORM_TYPE platform_type) {
        EnvTemplate envTemplate = new EnvTemplate();
        envTemplate.platform_type = platform_type;
        if (platform_type == ENV_TYPE.PLATFORM_TYPE.BRIDGE) {
            envTemplate.buildingPlatform = getModelOfName(ENV_TYPE.PLATFORM_TYPE.BRIDGE.toString());
            envTemplate.building = getModelOfName(ENV_TYPE.BRIDGE.BRIDGE1.toString());
        } else if (platform_type == ENV_TYPE.PLATFORM_TYPE.BUILDING) {
            envTemplate.buildingPlatform = getModelOfName(ENV_TYPE.PLATFORM_TYPE.BUILDING.toString());
            envTemplate.building = getModelOfName(ENV_TYPE.BUILDING.values()[Utils.getRandomInt(ENV_TYPE.BUILDING.values().length)].toString());
        } else if(platform_type == ENV_TYPE.PLATFORM_TYPE.GARDEN){
            envTemplate.buildingPlatform = getModelOfName(ENV_TYPE.PLATFORM_TYPE.GARDEN.toString());
            envTemplate.building = getModelOfName(ENV_TYPE.GARDEN.GARDEN1.toString());
        }
        return envTemplate;
    }

    static Model getModelOfName(String o) {
        return GlobalReferences.modelLoader.getModel(GlobalReferences.getInstance().univConfig.envConfig.getEnvObject(o));
    }
}
