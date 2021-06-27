package com.cutehub;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.cutehub.gameplay.GamePlayController;

import static com.badlogic.gdx.graphics.GL20.GL_REPEAT;
import static com.badlogic.gdx.graphics.GL20.GL_TEXTURE_2D;
import static com.badlogic.gdx.graphics.GL20.GL_TEXTURE_WRAP_S;
import static com.badlogic.gdx.graphics.GL20.GL_TEXTURE_WRAP_T;

public class CloudShader extends BaseShader {
    ShaderProgram program;
    Camera camera;
    RenderContext context;
    int u_projTrans;
    int u_worldTrans;
    int u_color;
    int u_tex;
    float rot;
    public Texture texture;
    GlobalReferences globalReferences;

    @Override
    public void init() {
        rot = 0;
        String vertexShader = Gdx.files.internal("shader/CloudVertex.glsl").readString();
        String fragmentShader = Gdx.files.internal("shader/CloudFragment.glsl").readString();
        program = new ShaderProgram(vertexShader, fragmentShader);
        if (!program.isCompiled())
            throw new GdxRuntimeException(program.getLog());
        u_projTrans = program.getUniformLocation("u_projTrans");
        u_worldTrans = program.getUniformLocation("u_worldTrans");
        u_color = program.getUniformLocation("u_color");
        u_tex = program.getUniformLocation("u_diffuseTexture");
        globalReferences = GlobalReferences.getInstance();
        texture = new Texture("envAsset/props/cloud.png");
    }

    @Override
    public void dispose() {
        program.dispose();
    }

    @Override
    public void begin(Camera camera, RenderContext context) {
        this.camera = camera;
        this.context = context;
        program.begin();
        program.setUniformMatrix(u_projTrans, camera.combined);
        context.setDepthTest(GL20.GL_LEQUAL);
        context.setCullFace(GL20.GL_BACK);
    }

    @Override
    public void render(Renderable renderable) {
        Vector3 pos = new Vector3(0, -0, 20);
        rot += 0.02f;
        renderable.worldTransform.setTranslation(pos.add(((GamePlayController) (globalReferences.controller.mainPlayController.currentPlayController)).playerController.position)).rotate(Vector3.Y, rot);
        texture.bind(0);
        program.setUniformi(u_tex, 0);
        program.setUniformMatrix(u_worldTrans, renderable.worldTransform);
        program.setUniformf(u_color, MathUtils.random(), MathUtils.random(), MathUtils.random());
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        Gdx.gl20.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        renderable.meshPart.render(program);
    }

    @Override
    public void end() {
        program.end();
    }

    @Override
    public int compareTo(Shader other) {
        return 0;
    }

    @Override
    public boolean canRender(Renderable instance) {
        return true;
    }
}