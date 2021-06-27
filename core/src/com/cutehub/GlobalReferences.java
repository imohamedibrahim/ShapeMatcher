package com.cutehub;

import com.cutehub.camera.CameraController;
import com.cutehub.camera.GamePlayCameraController;
import com.cutehub.config.UnivConfig;
import com.cutehub.controllers.Controllers;
import com.cutehub.controllers.UIMainController;
import com.cutehub.enums.GAME_SCREEN;
import com.cutehub.gameplay.GamePlayController;
import com.cutehub.gameplay.PlatformController;

public class GlobalReferences {
    public static final String PLATFORM_PROPERTY = "platformProperty";
    public static final String SHAPE_MATCH_PROPERTY = "shapeMatcherProperty";
    public static ModelLoader modelLoader = ModelLoader.getInstance();
    private static GlobalReferences globalReferences;

    public Controllers controller;
    public UnivConfig univConfig;
    public ScoreManager scoreManager;

    public static GlobalReferences getInstance() {
        if (globalReferences == null) {
            globalReferences = new GlobalReferences();
        }
        return globalReferences;
    }

    public void setUnivAndController() {
        univConfig = UnivConfig.getInstance();
        controller = Controllers.getInstance();
        scoreManager = ScoreManager.getInstance();
    }

    public void loadModel() {
        for (String name : univConfig.getListOfModelNameToLoad()) {
            modelLoader.loadModel(name);
        }
    }

    public static void reloadAll(GAME_SCREEN game_stage) {
        globalReferences.univConfig = globalReferences.univConfig.removeInstanceAndAssignNew();
        globalReferences.scoreManager.resetCurrentScore();
        globalReferences.controller.refreshController(game_stage);
    }

    public PlatformController getPlatformController() {
        if (controller.mainPlayController.currentPlayController instanceof GamePlayController) {
            return ((GamePlayController) controller.mainPlayController.currentPlayController).platformController;
        }
        return null;
    }

    public GamePlayCameraController getCameraController(){
        if (controller.mainCameraController.currentCameraController instanceof GamePlayCameraController) {
            return ((GamePlayCameraController) controller.mainCameraController.currentCameraController);
        }
        return null;
    }


}
