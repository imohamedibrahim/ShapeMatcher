package com.cutehub.camera;

import com.badlogic.gdx.graphics.Camera;

public interface CameraController {
    public Camera getCam();

    public void create();

    public void update(float dt);
}
