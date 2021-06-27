package com.cutehub.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.cutehub.GlobalReferences;

public class MenuCameraController implements CameraController {
    private static MenuCameraController menuCameraController;
    private GlobalReferences globalReferences = GlobalReferences.getInstance();
    private Camera camera = new PerspectiveCamera(67, 90, Gdx.graphics.getHeight());

    public static MenuCameraController getInstance() {
        if (menuCameraController == null) {
            menuCameraController = new MenuCameraController();
        }
        return menuCameraController;
    }

    @Override
    public Camera getCam() {
        return camera;
    }

    @Override
    public void create() {
    }

    @Override
    public void update(float dt) {
    }

    public static MenuCameraController removeInstanceAndAssignNew() {
        menuCameraController = null;
        return getInstance();
    }
}
