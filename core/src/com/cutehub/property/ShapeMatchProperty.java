package com.cutehub.property;

import com.badlogic.gdx.math.Vector3;
import com.cutehub.GameObject;
import com.cutehub.GlobalReferences;
import com.cutehub.config.UnivConfig;
import com.cutehub.utils.CallBack;

import java.util.LinkedList;
import java.util.Queue;

public class ShapeMatchProperty {
    enum ANIMATION_TYPE {
        MOVE, SCALE
    }

    private GlobalReferences globalReferences = GlobalReferences.getInstance();
    public GameObject shapeMatchGameObject;
    public Vector3 initialPosition;
    private Vector3 initialScale;
    private Vector3 destinationScale;
    public Vector3 destinationPosition;
    private CallBack callBack;
    public GameObject blockerGameObject;
    public boolean falseMatch;
    public GameObject platformGameObject;
    private Queue<ANIMATION_TYPE> animationQueue = new LinkedList();
    private boolean processNextAnimation;
    private ANIMATION_TYPE currentAnimationType;
    private boolean done;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public ShapeMatchProperty() {
        initialScale = new Vector3(0.4f, 0.4f, 0.4f);
        destinationScale = new Vector3(1, 1, 1);
        done = false;
        processNextAnimation = true;
        animationQueue.add(ANIMATION_TYPE.MOVE);
        animationQueue.add(ANIMATION_TYPE.SCALE);
    }

    public void start() {
        shapeMatchGameObject.transform.setTranslation(initialPosition).scl(initialScale);
    }

    public void update(float dt) {
        if (done) return;
        if (processNextAnimation && animationQueue.size() == 0) {
            done = true;
            callBack.call(this);
        }
        if (processNextAnimation && animationQueue.size() > 0) {
            processNextAnimation = false;
            currentAnimationType = animationQueue.remove();
        }
        if (currentAnimationType == ANIMATION_TYPE.MOVE) {
            move(dt);
        } else if (currentAnimationType == ANIMATION_TYPE.SCALE) {
            scale(dt);
        }
    }

    private void scale(float dt) {
        float scaleAlpha = globalReferences.univConfig.currentGameSpeed * UnivConfig.shootSpeed * 0.05f * dt / initialScale.dst(destinationScale);
        if (Float.isInfinite(scaleAlpha) || scaleAlpha > 1) scaleAlpha = 1;
        initialScale.lerp(destinationScale, scaleAlpha);
        shapeMatchGameObject.transform.setToTranslation(initialPosition).scl(initialScale);
        if (scaleAlpha == 1)
            processNextAnimation = true;
    }

    private void move(float dt) {
        float alpha = globalReferences.univConfig.currentGameSpeed * UnivConfig.shootSpeed * dt / initialPosition.dst(destinationPosition);
        if (Float.isInfinite(alpha) || alpha > 1) alpha = 1;
        initialPosition.lerp(destinationPosition, alpha);
        shapeMatchGameObject.transform.setToTranslation(initialPosition).scl(initialScale);
        if (alpha == 1) processNextAnimation = true;
    }
}
