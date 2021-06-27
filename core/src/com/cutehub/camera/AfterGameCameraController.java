package com.cutehub.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.cutehub.GlobalReferences;

public class AfterGameCameraController implements CameraController {
    private static AfterGameCameraController afterGameCameraController;
    private GlobalReferences globalReferences = GlobalReferences.getInstance();
    private Camera camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    public static AfterGameCameraController getInstance() {
        if (afterGameCameraController == null) {
            afterGameCameraController = new AfterGameCameraController();
        }
        return afterGameCameraController;
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

    public static AfterGameCameraController removeInstanceAndAssignNew() {
        afterGameCameraController = null;
        return getInstance();
    }
}
