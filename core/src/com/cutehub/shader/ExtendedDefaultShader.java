package com.cutehub.shader;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;

public class ExtendedDefaultShader extends DefaultShader {
    int roadTurn;
    float rangeBetween = 0.05f;
    float currentTurn = 0;
    boolean reverse;
    float  change = 0.0000001f;
    public ExtendedDefaultShader(Renderable renderable) {
        super(renderable);
    }

    public ExtendedDefaultShader(Renderable renderable, Config config) {
        super(renderable, config);
    }

    public void init() {
        roadTurn = this.program.getUniformLocation("u_turnAmount");
        currentTurn = rangeBetween;
        super.init();
    }

    public void render(Renderable renderable) {
        currentTurn = currentTurn + ((reverse) ? -change : change);
        if (currentTurn >= rangeBetween && !reverse) {
            reverse = !reverse;
        } else if (currentTurn <= -rangeBetween && reverse) {
            reverse = !reverse;
        }
        program.setUniformf(roadTurn, 0.00f);
        super.render(renderable);
    }
}
