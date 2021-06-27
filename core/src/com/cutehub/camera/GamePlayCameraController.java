package com.cutehub.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.cutehub.GlobalReferences;
import com.cutehub.gameplay.GamePlayController;
import com.cutehub.gameplay.PlayerController;

import java.util.LinkedList;
import java.util.Queue;

public class GamePlayCameraController implements CameraController {

    public static class CameraTranformInfo {
        public Vector3 lookAt;
        public Vector3 position;

        public CameraTranformInfo(Vector3 _lookAt, Vector3 _position) {
            lookAt = _lookAt;
            position = _position;
        }
    }

    private static GamePlayCameraController gamePlayCameraController;
    private GlobalReferences globalReferences = GlobalReferences.getInstance();
    private Camera camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    private Vector3 currentPosition;
    private CameraTranformInfo nextCameraTranformInfo;
    private Vector3 nextPositionPlusCarPosition;
    private Queue<CameraTranformInfo> cameraTranformInfoQueue = new LinkedList<>();
    private boolean processNextAnimation;

    public static GamePlayCameraController getInstance() {
        if (gamePlayCameraController == null) {
            gamePlayCameraController = new GamePlayCameraController();
            gamePlayCameraController.camera.far = 150f;
        }
        return gamePlayCameraController;
    }

    @Override
    public Camera getCam() {
        return camera;
    }

    @Override
    public void create() {
        processNextAnimation = false;
        cameraTranformInfoQueue.add(new CameraTranformInfo(new Vector3(-0.5f, 0f, 0), new Vector3(3, 1.5f, 3.5f)));
        cameraTranformInfoQueue.add(new CameraTranformInfo(new Vector3(0, 0, 5), new Vector3(0, 3f, -4.5f)));
        nextCameraTranformInfo = cameraTranformInfoQueue.remove();
        currentPosition = new Vector3(nextCameraTranformInfo.position);
    }

    @Override
    public void update(float dt) {
        Vector3 carPosition = getPlayerController().getPosition();
        if (carPosition == null) return;
        if (cameraTranformInfoQueue.size() != 0 && processNextAnimation) {
            nextCameraTranformInfo = cameraTranformInfoQueue.remove();
            processNextAnimation = false;
        }
        moveAnimation(dt);
        camera.position.set(currentPosition);
        camera.up.set(0, 1, 0);
        camera.lookAt(nextCameraTranformInfo.lookAt.x+carPosition.x,nextCameraTranformInfo.lookAt.y+carPosition.y,nextCameraTranformInfo.lookAt.z+carPosition.z);
        camera.update();
    }

    public void setProcessNextAnimation(boolean t) {
        processNextAnimation = t;
    }

    private void moveAnimation(float dt) {
        Vector3 tmpVector3 = new Vector3(nextCameraTranformInfo.position.x, nextCameraTranformInfo.position.y, nextCameraTranformInfo.position.z);
        tmpVector3.add(getPlayerController().getPosition());
        float alpha = (globalReferences.univConfig.currentGameSpeed * dt) / currentPosition.dst(tmpVector3);
        if (Float.isInfinite(alpha) || alpha > 1) {
            alpha = 1;
        }
        currentPosition.lerp(tmpVector3, alpha);
    }

    private PlayerController getPlayerController() {
        return ((GamePlayController) globalReferences.controller.mainPlayController.currentPlayController).playerController;
    }

    public static GamePlayCameraController removeInstanceAndAssignNew() {
        gamePlayCameraController = null;
        return getInstance();
    }
}
