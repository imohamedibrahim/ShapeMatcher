package com.cutehub.controllers;

import com.cutehub.GlobalReferences;
import com.cutehub.UI.UIAfterGameController;
import com.cutehub.UI.UIBeforeGameContoller;
import com.cutehub.UI.UIContoller;
import com.cutehub.UI.UIGamePlayController;
import com.cutehub.enums.GAME_SCREEN;

import java.util.LinkedList;
import java.util.Queue;


public class UIMainController {
    // flag //
    public static boolean updatingUI = false;
    public static UIMainController uiMainController;

    private GlobalReferences globalReferences = GlobalReferences.getInstance();

    public UIContoller currentUIController;

    public static UIMainController getInstance() {
        if (uiMainController == null) {
            uiMainController = new UIMainController();
        }
        return uiMainController;
    }

    public static UIMainController refreshController() {
        uiMainController.currentUIController = null;
        return getInstance();
    }

    public void create() {
    }

    public void update(float dt) {
        if (currentUIController == null) return;
        currentUIController.update(dt);
        updatingUI = currentUIController.isUpdating();
    }

    public void setCurrentUIController(GAME_SCREEN uiController) {
        currentUIController = getUIControllerOfType(uiController);
    }

    public void refreshUIController(GAME_SCREEN game_screen){
        currentUIController = getUIControllerRefreshedOfType(game_screen);
    }

    public void reAlign() {
        if (currentUIController != null) currentUIController.reAlignButton();
    }

    private UIContoller getUIControllerRefreshedOfType(GAME_SCREEN ui_controllers) {
        UIContoller uiContoller = null;
        switch (ui_controllers) {
            case AFTER_GAME:
                uiContoller = UIAfterGameController.removeInstanceAndAssignNew();
                break;
            case MAIN_MENU:
                uiContoller = UIBeforeGameContoller.removeInstanceAndAssignNew();
                break;
            case GAME_PLAY:
                uiContoller = UIGamePlayController.removeInstanceAndAssignNew();
                break;
        }
        uiContoller.create();
        return uiContoller;
    }

    private UIContoller getUIControllerOfType(GAME_SCREEN ui_controllers) {
        UIContoller uiContoller = null;
        switch (ui_controllers) {
            case AFTER_GAME:
                uiContoller = UIAfterGameController.getInstance();
                break;
            case MAIN_MENU:
                uiContoller = UIBeforeGameContoller.getInstance();
                break;
            case GAME_PLAY:
                uiContoller = UIGamePlayController.getInstance();
                break;
        }
        uiContoller.create();
        return uiContoller;
    }

    public void render() {
        currentUIController.render();
    }

    public void gameEnded() {
        //  uiAfterGameController = new UIAfterGameController();
    }

    public void resize(int width, int height) {
    }

    public void pause() {
    }

    public void resume() {
    }

    public void dispose() {
    }

}
