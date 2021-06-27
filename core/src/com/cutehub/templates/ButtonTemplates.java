package com.cutehub.templates;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.cutehub.enums.BUTTON_TYPE;

public class ButtonTemplates {
    public Vector2 currentPosition;
    public Vector2 finalPosition;
    public Drawable spriteName;
    public InputListener inputListener;
    public BUTTON_TYPE button_type;
    public Object userObject;
    public float size;
    public Class buttonClassType;
}
