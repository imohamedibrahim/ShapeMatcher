package com.cutehub.controllers;

import com.cutehub.enums.GAME_SCREEN;
import com.cutehub.gameplay.GamePlayController;
import com.cutehub.gameplay.PlayController;

public class MainPlayController {

    public PlayController currentPlayController;

    private static MainPlayController gamePlayController;

    public static MainPlayController getInstance() {
        if (gamePlayController == null) {
            gamePlayController = new MainPlayController();
        }
        return gamePlayController;
    }

    public static MainPlayController refreshControllers(){
        gamePlayController.currentPlayController = null;
        return getInstance();
    }

    public void setGameStage(GAME_SCREEN gameStage) {
        switch (gameStage) {
            case GAME_PLAY:
                currentPlayController = GamePlayController.getInstance();
        }
    }

    public void refreshGameStage(GAME_SCREEN gameStage) {
        switch (gameStage) {
            case GAME_PLAY:
                currentPlayController = GamePlayController.removeInstanceAndAssignNew();
        }
    }
    public void create() {
        if (currentPlayController != null) {
            currentPlayController.create();
        }
    }

    public void update(float dt) {
        if (currentPlayController != null) {
            currentPlayController.update(dt);
        }
    }

    public void dispose() {
        if (currentPlayController != null) {
            currentPlayController.dispose();
        }
    }
}
