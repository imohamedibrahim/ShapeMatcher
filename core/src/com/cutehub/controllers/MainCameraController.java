package com.cutehub.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.cutehub.GlobalReferences;
import com.cutehub.camera.AfterGameCameraController;
import com.cutehub.camera.CameraController;
import com.cutehub.camera.GamePlayCameraController;
import com.cutehub.camera.MenuCameraController;
import com.cutehub.enums.GAME_SCREEN;

public class MainCameraController {

    private static MainCameraController mainCameraController;
    public CameraController currentCameraController;

    public static MainCameraController getInstance() {
        if (mainCameraController == null) {
            mainCameraController = new MainCameraController();
        }
        return mainCameraController;
    }

    public static MainCameraController refreshControllers() {
        mainCameraController.currentCameraController = null;
        return getInstance();
    }

    public void create() {
        if (currentCameraController != null) currentCameraController.create();
    }

    public Camera getCam() {
        return currentCameraController.getCam();
    }

    public void setGameStage(GAME_SCREEN gameStage) {
        switch (gameStage) {
            case CAR_SHOWER:
                break;
            case MAIN_MENU:
                currentCameraController = MenuCameraController.getInstance();
                break;
            case GAME_PLAY:
                currentCameraController = GamePlayCameraController.getInstance();
                break;
            case AFTER_GAME:
                currentCameraController = AfterGameCameraController.getInstance();
                break;
        }
    }

    public void refreshGameStage(GAME_SCREEN gameStage) {
        switch (gameStage) {
            case CAR_SHOWER:
                break;
            case MAIN_MENU:
                currentCameraController = MenuCameraController.removeInstanceAndAssignNew();
                break;
            case GAME_PLAY:
                currentCameraController = GamePlayCameraController.removeInstanceAndAssignNew();
                break;
            case AFTER_GAME:
                currentCameraController = AfterGameCameraController.removeInstanceAndAssignNew();
                break;
        }
    }

    public void update(float dt) {
        if (currentCameraController != null) currentCameraController.update(dt);
    }
}
