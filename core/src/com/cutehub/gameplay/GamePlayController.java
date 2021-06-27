package com.cutehub.gameplay;

import com.badlogic.gdx.graphics.g3d.Model;
import com.cutehub.EnvironmentMap;
import com.cutehub.GameObject;
import com.cutehub.GlobalReferences;
import com.cutehub.config.CarConfigs;
import com.cutehub.enums.ROAD_TYPE;

public class GamePlayController implements PlayController {

    public PlatformController platformController = PlatformController.getInstance();
    public PlayerController playerController = PlayerController.getInstance();
    public EnvController envController = EnvController.getInstance();

    private static GamePlayController gamePlayController;
    private GlobalReferences globalReferences = GlobalReferences.getInstance();
    private boolean loading = true;
    EnvironmentMap environmentMap;
    private GameObject sky;

    public static GamePlayController getInstance() {
        if (gamePlayController == null) {
            gamePlayController = new GamePlayController();
        }
        return gamePlayController;
    }

    public static GamePlayController removeInstanceAndAssignNew() {
        if (gamePlayController != null) {
            gamePlayController.playerController = PlayerController.removeInstanceAndAssignNew();
            gamePlayController.platformController = PlatformController.removeInstanceAndAssignNew();
            gamePlayController.envController = EnvController.removeInstanceAndAssignNew();
            gamePlayController.loading = true;
        }
        return getInstance();
    }

    public void create() {
        loadModels();
        envController.create();
    }

    private void loadModels() {
        for (String name : globalReferences.univConfig.getListOfModelNameToLoad()) {
            globalReferences.modelLoader.loadModel(name);
        }
    }

    private Model getModel(String name) {
        return globalReferences.modelLoader.getModel(name);
    }

    private void doneLoading() {
        playerController.setCurrentCarModel(getModel(globalReferences.univConfig.carConfigs.getCarNameOf(CarConfigs.CAR_TYPE.CAR1)));
        platformController.initiatePlatforms(getModel(globalReferences.univConfig.envConfig.getRoadNameOf(ROAD_TYPE.CITY_ROAD2)));
        sky = new GameObject(getModel(globalReferences.univConfig.envConfig.getSkyName()), "sky");
        sky.transform.scale(10, 10, 10);
       // globalReferences.univConfig.renderArray.add(sky);
        loading = false;
    }

    public void update(float dt) {
        if (loading && globalReferences.modelLoader.isModelsLoaded()) doneLoading();
        if (!loading && platformController != null && playerController != null && envController != null) {
            platformController.update(dt);
            playerController.update(dt);
            envController.update(dt);
//            environmentMap.render(globalReferences.controller.mainCameraController.getCam());
            sky.transform.setTranslation(playerController.position.cpy().add(0, 0, 0));
/*            Quaternion q = new Quaternion();
            sky.transform.getRotation(q);
            Vector3 v =new Vector3();
            q.getAngle(v);
            sky.transform.rotate();*/
        }
    }

    public void dispose() {

    }
}
