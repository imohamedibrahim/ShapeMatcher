package com.cutehub;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.utils.Array;
import com.cutehub.enums.GAME_SCREEN;
import com.cutehub.shader.ExtendedDefaultShaderProvider;

public class ShapeMatcherRunner extends ApplicationAdapter {

    private GlobalReferences globalReferences;
    public ModelBatch modelBatch;
    public Environment environment;
    public boolean loading;
    private String vertexString = "";
    private String fragmentString = "";
    private boolean DEBUG = true;
    DefaultShaderProvider shaderProvider;
    EnviromentCubemap environmentMap;
    CloudShader cloudShader;
    @Override
    public void create() {
        cloudShader = new CloudShader();
        cloudShader.init();
        environmentMap = new EnviromentCubemap(new Pixmap(Gdx.files.internal("envAsset/props/sky.png")));
        vertexString = Gdx.files.internal("shader/vertex.glsl").readString();
        fragmentString = Gdx.files.internal("shader/fragment.glsl").readString();
        shaderProvider = new ExtendedDefaultShaderProvider(vertexString, fragmentString);
        modelBatch = new ModelBatch(shaderProvider);
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1, 1, 1, 1f));
        environment.set(new ColorAttribute(ColorAttribute.Fog, 0.5f, 0.5f, 0.5f, 1f));
        environment.add(new DirectionalLight().set(1, 1, 1, 0f, -1f, 0.5f));
        globalReferences = GlobalReferences.getInstance();
        globalReferences.setUnivAndController();
        globalReferences.controller.setGameStage(GAME_SCREEN.GAME_PLAY);
    }

    @Override
    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.58431372549f, 0.90588235294f, 0.98039215686f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        globalReferences.controller.update(Gdx.graphics.getDeltaTime());
        modelBatch.begin(globalReferences.controller.mainCameraController.getCam());
        Array<GameObject> listOfObjectToRender = globalReferences.univConfig.renderArray;
        for (GameObject modelInstance : listOfObjectToRender) {
            if(modelInstance.name.contains("sky")){
                modelBatch.render(modelInstance,environment, cloudShader);
                continue;
            }
            modelBatch.render(modelInstance, environment);
        }
        modelBatch.end();
        globalReferences.controller.mainUIController.render();
        if (DEBUG) System.out.println("number of objects rendered: " +globalReferences.univConfig.renderArray.size);
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
