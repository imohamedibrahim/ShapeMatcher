package com.cutehub.shader;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;

public class ExtendedDefaultShaderProvider extends DefaultShaderProvider {

    public final ExtendedDefaultShader.Config config;

    public ExtendedDefaultShaderProvider(final ExtendedDefaultShader.Config config) {
        this.config = (config == null) ? new ExtendedDefaultShader.Config() : config;
    }

    public ExtendedDefaultShaderProvider(final String vertexShader, final String fragmentShader) {
        this(new ExtendedDefaultShader.Config(vertexShader, fragmentShader));
    }

    @Override
    protected Shader createShader (final Renderable renderable) {
        return new ExtendedDefaultShader(renderable, config);
    }
}
