package com.cutehub.gameplay;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.cutehub.GameObject;
import com.cutehub.GlobalReferences;
import com.cutehub.enums.GAME_SCREEN;
import com.cutehub.enums.PLAY_SHAPE;
import com.cutehub.property.PlatformProperty;
import com.cutehub.property.ShapeMatchProperty;
import com.cutehub.utils.CallBack;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerController {
    private static PlayerController playerController;

    public Vector3 position;
    private Vector3 nextPosition;
    private GameObject currentPlatformGameObject;
    private GameObject currentPlayerGameObject;
    private AnimationController carAnimationController;
    private CopyOnWriteArrayList<GameObject> listOfShapeMatcherSpawned = new CopyOnWriteArrayList<GameObject>();
    private int numberOfPowerShootDone = 0;
    private GlobalReferences globalReferences = GlobalReferences.getInstance();
    ArrayList<Node> tyresArrayList;
    private float rotation;

    public static PlayerController getInstance() {
        if (playerController == null) {
            playerController = new PlayerController();
        }
        return playerController;
    }

    public static PlayerController removeInstanceAndAssignNew() {
        playerController = null;
        return getInstance();
    }

    public void setCurrentCarModel(Model model) {
        currentPlayerGameObject = new GameObject(model, "car");
        //currentPlayerGameObject.transform.rotate(Vector3.Y, 90);
        setCarWheelNodes("wheelFront.L", "wheelFront.R", "wheelBack.L", "wheelBack.R");
        globalReferences.univConfig.renderArray.add(currentPlayerGameObject);
        carAnimationController = new AnimationController(currentPlayerGameObject);
        position = new Vector3(0, 0, 0);
        nextPosition = new Vector3(0, 0, 0);
        triggerCarAnimation();
    }

    public void setCarWheelNodes(String... wheelNames) {
        tyresArrayList = new ArrayList<>();
        for (String wheelName : wheelNames) {
            tyresArrayList.add(currentPlayerGameObject.getNode(wheelName));
            currentPlayerGameObject.getNode(wheelName).isAnimated = false;
        }
    }

    public GameObject getCurrentPlayerGameObject() {
        return currentPlayerGameObject;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void update(float dt) {
        if (currentPlayerGameObject == null) return;
        if (nextPosition.equals(position)) {
            if (currentPlatformGameObject == null || checkIfCurrentPlatformMatched())
                assignNextPosition();
            else {
                if (!globalReferences.univConfig.gameOver) publishGameEnd();
            }
        }
        float alpha = (globalReferences.univConfig.currentGameSpeed * dt) / position.dst(nextPosition);
        if (Float.isInfinite(alpha) || alpha > 1) alpha = 1;
        position.lerp(nextPosition, alpha);
        currentPlayerGameObject.transform.setToTranslation(position).scl(1);
        rotation += 5;
        for (Node wheelNode : tyresArrayList) {
            wheelNode.isAnimated = false;
            wheelNode.rotation.set(new Quaternion(Vector3.X, rotation));
            wheelNode.calculateTransforms(false);
            wheelNode.isAnimated = true;
        }
        carAnimationController.update(dt);
        for (GameObject tmpGameObject : listOfShapeMatcherSpawned) {
            ((ShapeMatchProperty) tmpGameObject.propertiesHashMap.get(GlobalReferences.SHAPE_MATCH_PROPERTY)).update(dt);
        }
    }

    private void publishGameEnd() {
        globalReferences.univConfig.gameOver = true;
        globalReferences.reloadAll(GAME_SCREEN.AFTER_GAME);
    }


    private boolean checkIfCurrentPlatformMatched() {
        return ((PlatformProperty) currentPlatformGameObject.propertiesHashMap.get(GlobalReferences.PLATFORM_PROPERTY)).isShapedMapped;
    }

    private void assignNextPosition() {
        if (currentPlatformGameObject != null) {
            getPlatformControllerInstance().removeGameObjectFromList(currentPlatformGameObject);
        }
        if (getPlatformControllerInstance().platformListForPlayerController.size() > 0) {
            currentPlatformGameObject = getPlatformControllerInstance().platformListForPlayerController.get(0);
            getPlatformControllerInstance().platformListForPlayerController.get(0).transform.getTranslation(nextPosition);
            return;
        }
        currentPlatformGameObject = null;
        return;
    }

    private PlatformController getPlatformControllerInstance() {
        return ((GamePlayController) globalReferences.controller.mainPlayController.currentPlayController).platformController;
    }

    public void shootShape(PLAY_SHAPE play_shape) {
        shoot(new CallBack() {
            @Override
            public void call(Object object) {
                clearBlocker(object);
            }
        }, play_shape, false);
    }

    public void powerShootShapes() {
        shoot(new CallBack() {
            @Override
            public void call(Object object) {
                clearBlocker(object);
                numberOfPowerShootDone++;
                if (globalReferences.univConfig.numberOfMatchesPerPowerShoot > numberOfPowerShootDone) {
                    powerShootShapes();
                } else {
                    numberOfPowerShootDone = 0;
                }

            }
        }, null, true);
    }

    private void shoot(CallBack callBack, PLAY_SHAPE play_shape, Boolean pickFromPlatform) {
        if (globalReferences.univConfig.gameOver) return;
        if (getPlatformControllerInstance().unMatchedPlatformList.size() == 0)
            return;
        triggerCannonAnimation();
        GameObject platformGameObject = getPlatformControllerInstance().unMatchedPlatformList.get(0);
        PlatformProperty platformProperty = (PlatformProperty) platformGameObject.propertiesHashMap.get(GlobalReferences.PLATFORM_PROPERTY);
        ShapeMatchProperty shapeMatchProperty = new ShapeMatchProperty();
        if (pickFromPlatform) play_shape = platformProperty.playShape;
        GameObject shapeMatchGameObject = new GameObject(GlobalReferences.modelLoader.getModel(globalReferences.univConfig.envConfig.getMatchShapeName(play_shape)), "shapeMatcher" + platformProperty.playShape.toString());
        shapeMatchProperty.shapeMatchGameObject = shapeMatchGameObject;
        shapeMatchProperty.initialPosition = new Vector3(position).add(0, 1.2f, 0);
        shapeMatchProperty.blockerGameObject = platformProperty.blockerGameObject;
        shapeMatchProperty.destinationPosition = platformProperty.blockerGameObject.transform.getTranslation(new Vector3()).add(0, 0.9f, 0);
        shapeMatchGameObject.propertiesHashMap.put(globalReferences.SHAPE_MATCH_PROPERTY, shapeMatchProperty);
        listOfShapeMatcherSpawned.add(shapeMatchGameObject);
        shapeMatchProperty.setCallBack(callBack);
        globalReferences.univConfig.renderArray.add(shapeMatchGameObject);
        shapeMatchProperty.falseMatch = true;
        shapeMatchProperty.platformGameObject = platformGameObject;
        if (platformProperty.isShapedMapped != true && platformProperty.playShape == play_shape) {
            platformProperty.isShapedMapped = true;
            shapeMatchProperty.falseMatch = false;
            getPlatformControllerInstance().unMatchedPlatformList.remove(platformGameObject);
            globalReferences.scoreManager.addScoreByOne();
        }
    }

    private void shoot(CallBack callBack) {
        if (globalReferences.univConfig.gameOver) return;
        if (getPlatformControllerInstance().unMatchedPlatformList.size() == 0)
            return;
        GameObject platformGameObject = getPlatformControllerInstance().unMatchedPlatformList.get(0);
        PlatformProperty platformProperty = (PlatformProperty) platformGameObject.propertiesHashMap.get(GlobalReferences.PLATFORM_PROPERTY);
        ShapeMatchProperty shapeMatchProperty = new ShapeMatchProperty();
        GameObject shapeMatchGameObject = new GameObject(GlobalReferences.modelLoader.getModel(globalReferences.univConfig.envConfig.getMatchShapeName(platformProperty.playShape)), "shapeMatcher" + platformProperty.playShape.toString());
        shapeMatchProperty.shapeMatchGameObject = shapeMatchGameObject;
        shapeMatchProperty.initialPosition = new Vector3(position);
        shapeMatchProperty.blockerGameObject = platformProperty.blockerGameObject;
        shapeMatchProperty.destinationPosition = platformProperty.blockerGameObject.transform.getTranslation(new Vector3()).add(0, 1.0752f, 0);
        shapeMatchGameObject.propertiesHashMap.put(globalReferences.SHAPE_MATCH_PROPERTY, shapeMatchProperty);
        listOfShapeMatcherSpawned.add(shapeMatchGameObject);
        globalReferences.univConfig.renderArray.add(shapeMatchGameObject);
        shapeMatchProperty.falseMatch = false;
        shapeMatchProperty.platformGameObject = platformGameObject;
        shapeMatchProperty.setCallBack(callBack);
        platformProperty.isShapedMapped = true;
        shapeMatchProperty.falseMatch = false;
        getPlatformControllerInstance().unMatchedPlatformList.remove(platformGameObject);
    }

    private void clearBlocker(Object object) {
        ShapeMatchProperty shapeMatchProperty = (ShapeMatchProperty) object;
        if (shapeMatchProperty.falseMatch) {
            publishGameEnd();
            return;
        }
        globalReferences.univConfig.renderArray.removeValue(shapeMatchProperty.shapeMatchGameObject, true);
        listOfShapeMatcherSpawned.remove(shapeMatchProperty.shapeMatchGameObject);
        ((PlatformProperty) shapeMatchProperty.platformGameObject.propertiesHashMap.get(globalReferences.PLATFORM_PROPERTY)).disappearBlockerGameObjectWithEffect();
    }

    public void addModelToInstance(Array<ModelInstance> instances) {

    }

    public void triggerCannonAnimation() {
        carAnimationController.queue("ownCar|shout", 1, globalReferences.univConfig.currentGameSpeed * 0.75f, null, 0f);
    }

    public void triggerCarAnimation() {
        AnimationController.AnimationDesc queue = carAnimationController.queue("ownCar|spawnCannon", 1, globalReferences.univConfig.currentGameSpeed * 0.1f, new AnimationController.AnimationListener() {
            @Override
            public void onEnd(AnimationController.AnimationDesc animation) {
                onEndOfCannonSpawnAnimation();
            }

            @Override
            public void onLoop(AnimationController.AnimationDesc animation) {

            }
        }, 0f);
    }

    public void onEndOfCannonSpawnAnimation() {
        globalReferences.getPlatformController().canAddBlocker = true;
        globalReferences.getCameraController().setProcessNextAnimation(true);
    }

    public void render(float dt) {

    }
}
