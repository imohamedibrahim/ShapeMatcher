package com.cutehub.property;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.cutehub.GlobalReferences;
import com.cutehub.enums.BUTTON_TYPE;
import com.cutehub.utils.CallBack;

import java.util.LinkedList;
import java.util.Queue;

public class ButtonProperty implements UIProperty {

    private enum ANIMATION_TYPE {
        SCALE, MOVE
    }

    public Object userObject;
    public Button button;
    public BUTTON_TYPE button_type;
    public float size;

    private boolean reAlignDone = true;
    private CallBack callBack;
    private Boolean spawnAnimation;
    private Vector2 currentPosition;
    private Vector2 finalPosition;
    private Vector2 startButtonScale = new Vector2(0, 0);
    private Vector2[] finalButtonScale = {new Vector2(1.5f, 1.5f), new Vector2(1, 1)};
    private float[] scaleSpeed = {0.2f, 0.1f, 0.1f, 0.05f};
    private int spawnAnimationStage = 0;
    private int moveSpeed = 10;
    private Queue<ANIMATION_TYPE> animationQueue = new LinkedList<>();
    private ANIMATION_TYPE currentAnimation;
    private boolean processNextAnimation;
    private boolean done;

    private GlobalReferences globalReferences = GlobalReferences.getInstance();

    public ButtonProperty(Button button, float size, Vector2 initialPosition, Vector2 destinationPosition, CallBack callBack, Boolean spawnAnimation, BUTTON_TYPE button_type, Object userObject) {
        this.userObject = userObject;
        this.finalPosition = destinationPosition;
        this.currentPosition = initialPosition;
        this.button = button;
        this.callBack = callBack;
        this.spawnAnimation = spawnAnimation;
        this.size = size;
        this.button_type = button_type;
        button.setTransform(true);
        button.setPosition(initialPosition.x, initialPosition.y);
        setHeightAndWidth(size);
        if (spawnAnimation) button.setScale(0);
        else button.setScale(1);
        animationQueue.add(ANIMATION_TYPE.SCALE);
        animationQueue.add(ANIMATION_TYPE.MOVE);
        processNextAnimation = true;
        done = false;
    }

    public ButtonProperty(Button button, float size, Vector2 destinationPosition, CallBack callBack, Boolean spawnAnimation, BUTTON_TYPE button_type, Object userObject) {
        this(button, size, destinationPosition, destinationPosition, callBack, spawnAnimation, button_type, userObject);
    }

    public ButtonProperty(Button button, float size, Vector2 currentPosition, CallBack callBack, Boolean spawnAnimation, BUTTON_TYPE button_type) {
        this(button, size, currentPosition, callBack, spawnAnimation, button_type, null);
    }

    public void reAlign(Vector2 destinationPosition, CallBack callBack) {
        currentPosition = finalPosition;
        finalPosition = destinationPosition;
        this.callBack = callBack;
        animationQueue.add(ANIMATION_TYPE.SCALE);
        animationQueue.add(ANIMATION_TYPE.MOVE);
        processNextAnimation = true;
        done = false;
        spawnAnimationStage = 0;
    }

    public void update(float dt) {
        if (done) return;
        if (processNextAnimation && animationQueue.size() == 0) {
            callBack.call(this);
            processNextAnimation = false;
            done = true;
        }
        if (animationQueue.size() > 0 && processNextAnimation) {
            currentAnimation = animationQueue.remove();
            processNextAnimation = false;
        }
        if (currentAnimation == ANIMATION_TYPE.SCALE) {
            alignScale(dt);
        } else if (currentAnimation == ANIMATION_TYPE.MOVE) {
            alignPosition(dt);
        }
    }

    @Override
    public void create() {

    }

    @Override
    public void destroy() {

    }

    private void alignScale(float dt) {
        float alpha = globalReferences.univConfig.uiAnimationSpeed * scaleSpeed[spawnAnimationStage] * dt / startButtonScale.dst(finalButtonScale[spawnAnimationStage]);
        if (Float.isInfinite(alpha) || alpha > 1) {
            alpha = 1;
        }
        startButtonScale.lerp(finalButtonScale[spawnAnimationStage], alpha);
        button.setScale(startButtonScale.x, startButtonScale.y);
        if (alpha == 1) {
            if (spawnAnimationStage == finalButtonScale.length - 1) {
                processNextAnimation = true;
            }
            spawnAnimationStage++;
        }
    }

    private void alignPosition(float dt) {
        if (currentPosition.dst(finalPosition) == 0) {
            if (callBack != null) {
                processNextAnimation = true;
            }
            return;
        }
        float alpha = globalReferences.univConfig.uiAnimationSpeed * moveSpeed * dt / currentPosition.dst(finalPosition);
        if (Float.isInfinite(alpha) || alpha > 1) {
            alpha = 1;
        }
        currentPosition.lerp(finalPosition, alpha);
        button.setPosition(currentPosition.x, currentPosition.y);
    }

    private void setHeightAndWidth(float s) {
        button.setHeight(s);
        button.setWidth(s);
        button.setOrigin(button.getWidth() / 2, button.getHeight() / 2);
    }

    public void buttonDown() {
        button.setScale(0.9f);

    }

    public void buttonUp() {
        button.setScale(1);
    }
}
