package com.cutehub.property;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.cutehub.GameObject;
import com.cutehub.GlobalReferences;
import com.cutehub.ModelLoader;
import com.cutehub.config.UnivConfig;
import com.cutehub.enums.PLAY_SHAPE;

public class PlatformProperty {
    private GlobalReferences globalReferences = GlobalReferences.getInstance();
    public boolean isShapedMapped;
    public GameObject shapeMapped;
    public PLAY_SHAPE playShape;
    public UnivConfig univConfig;
    public GameObject platformGameObjectPropertyBelongsTo;
    public GameObject matchGameObject;
    private ModelLoader modelLoader;
    public GameObject blockerGameObject;
    public boolean deleteBlockerGameObject;


    private Vector3 currentPositionOfBlockerGameObject;
    private Vector3 destinationPositionOfBlockerGameObjectAfterDelete;
    private Vector3 currentScaleOfBlockerGameObject;
    private Vector3 destinationScaleOfBlockerGameObject;

    public PlatformProperty(PLAY_SHAPE play_shape, GameObject gameObject) {
        this.playShape = play_shape;
        this.platformGameObjectPropertyBelongsTo = gameObject;
        this.isShapedMapped = true;
        univConfig = UnivConfig.getInstance();
        modelLoader = ModelLoader.getInstance();
        deleteBlockerGameObject = false;
        destinationPositionOfBlockerGameObjectAfterDelete = new Vector3(0, 0, 0);
        currentPositionOfBlockerGameObject = new Vector3(0, 0, 0);
        currentScaleOfBlockerGameObject = new Vector3(1, 1, 1);
        destinationScaleOfBlockerGameObject = new Vector3(0, 0, 0);
        if (play_shape != PLAY_SHAPE.PLAIN) constructBlocker();
    }

    private void constructBlocker() {
        Model model = modelLoader.getModel(univConfig.envConfig.getRoadBlockPlayShapeName(this.playShape));
        blockerGameObject = new GameObject(model, playShape.toString());
        blockerGameObject.transform.setTranslation(platformGameObjectPropertyBelongsTo.transform.getTranslation(currentPositionOfBlockerGameObject).add(0, 0, 1.750f));
        globalReferences.univConfig.renderArray.add(blockerGameObject);
    }

    public void disappearBlockerGameObjectWithEffect() {
        deleteBlockerGameObject = true;
        destinationPositionOfBlockerGameObjectAfterDelete.add(currentPositionOfBlockerGameObject);
    }

    public void update(float dt) {
        if (deleteBlockerGameObject) {
            float alpha = univConfig.currentGameSpeed * dt / currentScaleOfBlockerGameObject.dst(destinationScaleOfBlockerGameObject);
            if (Float.isInfinite(alpha) || alpha > 1) alpha = 1;
            currentScaleOfBlockerGameObject.lerp(destinationScaleOfBlockerGameObject, alpha);
            blockerGameObject.transform.scl(currentScaleOfBlockerGameObject);
            if (alpha == 1) {
                globalReferences.univConfig.renderArray.removeValue(blockerGameObject, true);
                deleteBlockerGameObject = false;
            }
        }

    }

}
