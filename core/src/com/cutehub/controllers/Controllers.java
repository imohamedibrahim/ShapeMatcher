package com.cutehub.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.cutehub.EnvironmentMap;
import com.cutehub.GlobalReferences;
import com.cutehub.enums.GAME_SCREEN;

public class Controllers {
    public MainCameraController mainCameraController = MainCameraController.getInstance();
    public UIMainController mainUIController = UIMainController.getInstance();
    public MainPlayController mainPlayController = MainPlayController.getInstance();
    public GAME_SCREEN currentGameStage;

    private static Controllers controllers;
    private boolean updateOnlyUI = false;
    private GlobalReferences globalReferences = GlobalReferences.getInstance();

    public static Controllers getInstance() {
        if (controllers == null) {
            controllers = new Controllers();
        }
        return controllers;
    }

    public void refreshController(GAME_SCREEN game_screen) {
        currentGameStage = game_screen;
        mainUIController.refreshController();
        mainPlayController.refreshControllers();
        mainCameraController.refreshControllers();
        refreshGameStage(game_screen);
    }

    public void update(float dt) {
        if (mainUIController != null) mainUIController.update(dt);
        if (mainUIController.updatingUI) return;
        if (mainPlayController != null) mainPlayController.update(dt);
        if (mainCameraController != null) mainCameraController.update(dt);
        updateGameSpeed();
    }

    public void refreshGameStage(GAME_SCREEN gameStage) {
        currentGameStage = gameStage;
        mainCameraController.refreshGameStage(gameStage);
        mainPlayController.refreshGameStage(gameStage);
        mainUIController.refreshUIController(gameStage);
        create();
    }

    public void setGameStage(GAME_SCREEN gameStage) {
        currentGameStage = gameStage;
        mainCameraController.setGameStage(gameStage);
        mainPlayController.setGameStage(gameStage);
        mainUIController.setCurrentUIController(gameStage);
        create();
    }


    public void create() {
        if (mainPlayController != null) mainPlayController.create();
        if (mainUIController != null) mainUIController.create();
        if (mainCameraController != null) mainCameraController.create();
    }


    private void updateGameSpeed() {
        globalReferences.univConfig.currentGameSpeed += 0.005f;
        if (globalReferences.univConfig.currentGameSpeed > globalReferences.univConfig.increaseShapeAt && globalReferences.univConfig.numberOfMatchShapes < 4 && !mainUIController.updatingUI) {
            globalReferences.univConfig.currentGameSpeed = 3.5f;
            globalReferences.univConfig.numberOfMatchShapes++;
            if (mainUIController != null) mainUIController.reAlign();
        }
    }
}
