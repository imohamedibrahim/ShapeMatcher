package com.cutehub;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.cutehub.config.UnivConfig;

import java.util.HashMap;

public class ModelLoader {
    private static ModelLoader modelLoader;
    private HashMap<String, Model> modelHashMap = new HashMap<>();
    private AssetManager assetManager = new AssetManager();
    private HashMap<String, Boolean> modelStatusMap = new HashMap<>();
    private UnivConfig univConfig;

    public static ModelLoader getInstance() {
        if (modelLoader == null) {
            modelLoader = new ModelLoader();
            modelLoader.univConfig = UnivConfig.getInstance();
        }
        return modelLoader;
    }

    public Model getModel(String name) {
        Model model = null;
        if (modelHashMap.containsKey(name)) {
            model = modelHashMap.get(name);
        }
        return model;
    }

    public void loadModel(String name) {
        if (!modelStatusMap.containsKey(name)) {
            modelStatusMap.put(name, false);
            assetManager.load(name, Model.class);
        }
    }

    public boolean isModelsLoaded() {
        boolean isLoaded = assetManager.update();
        if (isLoaded) {
            for (String name : modelStatusMap.keySet()) {
                if (!modelHashMap.containsKey(name))
                    modelHashMap.put(name, assetManager.get(name, Model.class));
            }
        }
        return isLoaded;
    }
}
