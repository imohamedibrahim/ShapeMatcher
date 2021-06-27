package com.cutehub;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import java.util.ArrayList;
import java.util.HashMap;

public class GameObject extends ModelInstance {
    public final Vector3 center = new Vector3();
    public final Vector3 dimensions = new Vector3();
    public Vector3 position;
    public final float radius;
    public String name;
    public BoundingBox bounds = new BoundingBox();
    public HashMap<String, Object> propertiesHashMap = new HashMap<>();
    public Shader shader;
    public GameObject (Model model, String name) {
        super(model);
        this.name = name;
        calculateBoundingBox(bounds);
        bounds.getCenter(center);
        bounds.getDimensions(dimensions);
        radius = dimensions.len() / 2f;
    }


}