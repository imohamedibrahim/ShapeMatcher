package com.cutehub.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.cutehub.GlobalReferences;

public class PowerButton extends Button {
    private ShaderProgram shaderProgram;
    int u_texture;
    int u_power;
    Texture texture;
    GlobalReferences globalReferences;
    public PowerButton(Drawable drawable) {
        super(drawable);
        String vertexShader = Gdx.files.internal("shader/buttonVertex.glsl").readString();
        String fragmentShader = Gdx.files.internal("shader/buttonFragment.glsl").readString();
        fragmentShader.replace("POWER_LEVEL_PLACEHOLDER", "0");
        shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
        u_texture = shaderProgram.getUniformLocation("u_texture");
        u_power = shaderProgram.getUniformLocation("u_powerlevel");
        texture = new Texture("UI/runner.png");
        globalReferences = GlobalReferences.getInstance();
    }

    public void setShaderProgram(ShaderProgram program) {
        this.shaderProgram = program;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (shaderProgram != null) {
            batch.setShader(shaderProgram);
            shaderProgram.setUniformf(u_power,((UIGamePlayController) globalReferences.controller.mainUIController.currentUIController).shootPowerLevel);
        }
        super.draw(batch, parentAlpha);
        if (shaderProgram != null) batch.setShader(null);
    }

}
