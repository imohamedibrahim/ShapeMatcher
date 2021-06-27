package com.cutehub.property;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.cutehub.GlobalReferences;
import com.cutehub.utils.CallBack;

import java.util.LinkedList;
import java.util.Queue;

public class LabelProperty implements UIProperty {

    private GlobalReferences globalReferences;
    private float currentButtonScale = 3;
    private float[] finalButtonScale = {5f, 4f};
    private float[] scaleSpeed = {0.5f, 0.5f};
    private int spawnAnimationStage = 0;
    private Boolean processNextAnimation = true;
    private Boolean done = false;
    private CallBack callBack;
    private ANIMATION_TYPE currentAnimation;

    private enum ANIMATION_TYPE {
        SCALE
    }

    public Object userObject;
    public Label label;
    public String textContent;
    private Queue<ANIMATION_TYPE> animationQueue = new LinkedList();
    public Container<Label> container;

    public LabelProperty(Label label, Vector2 pos) {
        this.label = label;
        label.setOrigin(label.getWidth() / 2, label.getHeight() / 2);
        label.addAction(Actions.moveTo(pos.x, pos.y));
        container = new Container<Label>(label);
        container.setTransform(true);   // for enabling scaling and rotation
        //container.size(100, 60);
        container.setOrigin(container.getWidth() / 2, container.getHeight() / 2);
        container.setPosition(pos.x, pos.y);
        animationQueue.add(ANIMATION_TYPE.SCALE);
        textContent = "";
        globalReferences = GlobalReferences.getInstance();
    }

    public LabelProperty(Label label, float x, float y) {
        this(label, new Vector2(x, y));
    }

    public void resetCallBack(CallBack callBack) {
        callBack.call(this);
    }

    private void alignScale(float dt) {
        if (spawnAnimationStage == finalButtonScale.length) {
            processNextAnimation = true;
            return;
        }
        float alpha = globalReferences.univConfig.uiAnimationSpeed * scaleSpeed[spawnAnimationStage] * dt / Math.abs(finalButtonScale[spawnAnimationStage] - currentButtonScale);
        if (Float.isInfinite(alpha) || alpha > 1) {
            currentButtonScale = finalButtonScale[spawnAnimationStage];
            spawnAnimationStage++;
        } else {
            currentButtonScale = currentButtonScale + (finalButtonScale[spawnAnimationStage] - currentButtonScale) * alpha;
        }
        container.setScale(currentButtonScale);
    }

    public void updateScore(int score) {
        done = false;
        processNextAnimation = true;
        animationQueue.clear();
        animationQueue.add(ANIMATION_TYPE.SCALE);
        currentAnimation = null;
        textContent = String.valueOf(score);
        spawnAnimationStage = 0;
        label.setText(score);
    }

    @Override
    public void update(float dt) {
        if (done) return;
        if (processNextAnimation && animationQueue.size() == 0) {
            if (callBack != null) callBack.call(this);
            processNextAnimation = false;
            done = true;
        }
        if (animationQueue.size() > 0 && processNextAnimation) {
            currentAnimation = animationQueue.remove();
            processNextAnimation = false;
        }
        if (currentAnimation == ANIMATION_TYPE.SCALE) {
            alignScale(dt);
        }
    }

    @Override
    public void create() {

    }

    @Override
    public void destroy() {

    }

}
