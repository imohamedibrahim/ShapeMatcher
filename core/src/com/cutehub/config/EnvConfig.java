package com.cutehub.config;

import com.cutehub.enums.ENV_TYPE;
import com.cutehub.enums.PLAY_SHAPE;
import com.cutehub.enums.PROPS_TYPE;
import com.cutehub.enums.ROAD_TYPE;

import java.util.HashMap;
import java.util.HashSet;

public class EnvConfig {
    private static EnvConfig carConfigsInstance;
    private static HashMap<ROAD_TYPE, String> roadTypeMap;
    private static HashMap<PLAY_SHAPE, String> roadBlockPlayShapeMap;
    private static HashMap<PLAY_SHAPE, String> matchShapeMap;
    private static HashMap<String, String> envTypeStringHashMap;
    private static HashMap<PROPS_TYPE, String> propsTypeStringHashMap;
    public static String SKY = "envAsset/props/sky.g3dj";

    public EnvConfig() {
    }

    public static EnvConfig getInstance() {
        if (carConfigsInstance == null) {
            carConfigsInstance = new EnvConfig();
            carConfigsInstance.fillEnvTypeHashMap();
        }
        return carConfigsInstance;
    }

    public String getSkyName() {
        return SKY;
    }

    public void fillEnvTypeHashMap() {
        roadTypeMap = new HashMap<>();
        roadBlockPlayShapeMap = new HashMap<>();
        matchShapeMap = new HashMap<>();
        envTypeStringHashMap = new HashMap<>();
        propsTypeStringHashMap = new HashMap<>();


        roadTypeMap.put(ROAD_TYPE.CITY_ROAD1, "roadBlock.g3dj");
        roadTypeMap.put(ROAD_TYPE.CITY_ROAD2, "envAsset/road/road2.g3dj");
        roadBlockPlayShapeMap.put(PLAY_SHAPE.CIRCLE, "envAsset/barrier/barrierShapeCircle.g3dj");
        roadBlockPlayShapeMap.put(PLAY_SHAPE.FIVE_FACE_POLYGON, "envAsset/barrier/barrierShapeSquare.g3dj");
        roadBlockPlayShapeMap.put(PLAY_SHAPE.SQUARE, "envAsset/barrier/barrierShapeSquare.g3dj");
        roadBlockPlayShapeMap.put(PLAY_SHAPE.STAR, "envAsset/barrier/barrierShapeStar.g3dj");
        roadBlockPlayShapeMap.put(PLAY_SHAPE.TRIANGLE, "envAsset/barrier/barrierShapeTriangle.g3dj");

        matchShapeMap.put(PLAY_SHAPE.CIRCLE, "envAsset/barrier/circleShape.g3dj");
        matchShapeMap.put(PLAY_SHAPE.SQUARE, "envAsset/barrier/squareShape.g3dj");
        matchShapeMap.put(PLAY_SHAPE.TRIANGLE, "envAsset/barrier/triangleShape.g3dj");
        matchShapeMap.put(PLAY_SHAPE.STAR, "envAsset/barrier/starShape.g3dj");

        envTypeStringHashMap.put(ENV_TYPE.BUILDING.BUILDING1.toString(), "envAsset/buildings/house1.g3dj");
        envTypeStringHashMap.put(ENV_TYPE.BUILDING.BUILDING2.toString(), "envAsset/buildings/house2.g3dj");
        envTypeStringHashMap.put(ENV_TYPE.BUILDING.BUILDING3.toString(), "envAsset/buildings/shop1.g3dj");
        envTypeStringHashMap.put(ENV_TYPE.BUILDING.BUILDING4.toString(), "envAsset/props/wall1.g3dj");
        envTypeStringHashMap.put(ENV_TYPE.BUILDING.BUILDING5.toString(), "envAsset/buildings/house3.g3dj");
        envTypeStringHashMap.put(ENV_TYPE.BUILDING.BUILDING7.toString(), "envAsset/buildings/house4.g3dj");
        envTypeStringHashMap.put(ENV_TYPE.BUILDING.BUILDING6.toString(), "envAsset/buildings/house5.g3dj");
        envTypeStringHashMap.put(ENV_TYPE.BUILDING.BUILDING7.toString(), "envAsset/buildings/house6.g3dj");
        envTypeStringHashMap.put(ENV_TYPE.PLATFORM_TYPE.BUILDING.toString(),
                "envAsset/HouseAndBuildingPlatform/platform3.g3dj");
        envTypeStringHashMap.put(ENV_TYPE.BRIDGE.BRIDGE1.toString(), "envAsset/props/empty.g3dj");
        envTypeStringHashMap.put(ENV_TYPE.PLATFORM_TYPE.BRIDGE.toString(), "envAsset/bridge/bridge.g3dj");
        envTypeStringHashMap.put(ENV_TYPE.GARDEN.GARDEN1.toString(), "envAsset/bridge/garden.g3dj");
        envTypeStringHashMap.put(ENV_TYPE.PLATFORM_TYPE.GARDEN.toString(), "envAsset/HouseAndBuildingPlatform/platform3.g3dj");


        propsTypeStringHashMap.put(PROPS_TYPE.DUSTBIN2, "envAsset/props/bin2.g3dj");
        propsTypeStringHashMap.put(PROPS_TYPE.DUSTBIN1, "envAsset/props/bin1.g3dj");
        propsTypeStringHashMap.put(PROPS_TYPE.TREE1, "envAsset/props/tree1.g3dj");
        propsTypeStringHashMap.put(PROPS_TYPE.TREE2, "envAsset/props/tree2.g3dj");
        propsTypeStringHashMap.put(PROPS_TYPE.LAMP1, "envAsset/props/lamp1.g3dj");
        propsTypeStringHashMap.put(PROPS_TYPE.POSTBOX, "envAsset/props/postBox1.g3dj");
        propsTypeStringHashMap.put(PROPS_TYPE.SIGNBOARD, "envAsset/props/signBoard1.g3dj");
        propsTypeStringHashMap.put(PROPS_TYPE.FENCE, "envAsset/props/fence1.g3dj");
        propsTypeStringHashMap.put(PROPS_TYPE.FLOWERBASE, "envAsset/props/flowerBase1.g3dj");
        propsTypeStringHashMap.put(PROPS_TYPE.SEAT, "envAsset/props/seat1.g3dj");

    }

    public String getEnvObject(String s) {
        return envTypeStringHashMap.get(s);
    }

    public String getRoadBlockPlayShapeName(PLAY_SHAPE play_shape) {
        return roadBlockPlayShapeMap.get(play_shape);
    }

    public String getPropsName(PROPS_TYPE props_type) {
        return propsTypeStringHashMap.get(props_type);
    }

    public String getMatchShapeName(PLAY_SHAPE play_shape) {
        return matchShapeMap.get(play_shape);
    }

    public String getRoadNameOf(ROAD_TYPE road_type) {
        return roadTypeMap.get(road_type);
    }

    public HashSet giveMeTheModelNameAsSet() {
        HashSet returnSet = new HashSet();
        returnSet.addAll(roadBlockPlayShapeMap.values());
        returnSet.addAll(roadTypeMap.values());
        returnSet.addAll(envTypeStringHashMap.values());
        returnSet.addAll(matchShapeMap.values());
        returnSet.addAll(propsTypeStringHashMap.values());
        returnSet.add(SKY);
        return returnSet;
    }
}
