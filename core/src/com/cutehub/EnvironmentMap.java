package com.cutehub;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class EnvironmentMap implements Disposable {

    public CloudShader defaultShader;
    protected final Pixmap[] data = new Pixmap[6];
    protected ShaderProgram shader;
    protected int u_worldTrans;
    protected Mesh quad;
    private Matrix4 worldTrans;

    protected String vertexShader;

    protected String fragmentShader;
    protected GameObject skyGameObject;

    private static EnvironmentMap environmentMap;

    public String getDefaultVertexShader() {
        return vertexShader;
    }

    public String getDefaultFragmentShader() {
        return fragmentShader;
    }

    public static EnvironmentMap getInstance() {
        if (environmentMap == null) {
            environmentMap = new EnvironmentMap();
        }
        return environmentMap;
    }

    EnvironmentMap() {

    }

    public EnvironmentMap(Pixmap positiveX, Pixmap negativeX, Pixmap positiveY, Pixmap negativeY, Pixmap positiveZ, Pixmap negativeZ) {
        data[0] = positiveX;
        data[1] = negativeX;

        data[2] = positiveY;
        data[3] = negativeY;

        data[4] = positiveZ;
        data[5] = negativeZ;

        init();
    }

    public EnvironmentMap(FileHandle positiveX, FileHandle negativeX, FileHandle positiveY, FileHandle negativeY, FileHandle positiveZ, FileHandle negativeZ) {
        this(new Pixmap(positiveX), new Pixmap(negativeX), new Pixmap(positiveY), new Pixmap(negativeY), new Pixmap(positiveZ), new Pixmap(negativeZ));
    }

    public void get(GameObject gameObject) {
        vertexShader = Gdx.files.internal("shader/CloudVertex.glsl").readString();
        fragmentShader = Gdx.files.internal("shader/CloudFragment.glsl").readString();
        //Renderable tmpRenderable = new Renderable();
        //gameObject.getRenderable(tmpRenderable);
        //gameObject.getRenderable()
        defaultShader = new CloudShader();
        defaultShader.init();
        skyGameObject = gameObject;
    }

    public static Shader getShader(GameObject gameObject) {
        if (environmentMap == null) {
            EnvironmentMap.getInstance();
        }
        if (environmentMap.defaultShader == null) environmentMap.get(gameObject);
        return environmentMap.defaultShader;
    }

    //IF ALL SIX SIDES ARE REPRESENTED IN ONE IMAGE
    public EnvironmentMap(Pixmap cubemap) {
        int w = cubemap.getWidth();
        int h = cubemap.getHeight();
        for (int i = 0; i < 6; i++) data[i] = new Pixmap(w / 4, h / 3, Pixmap.Format.RGB888);
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                //-X
                if (x >= 0 && x <= w / 4 && y >= h / 3 && y <= h * 2 / 3)
                    data[1].drawPixel(x, y - h / 3, cubemap.getPixel(x, y));
                //+Y
                if (x >= w / 4 && x <= w / 2 && y >= 0 && y <= h / 3)
                    data[2].drawPixel(x - w / 4, y, cubemap.getPixel(x, y));
                //+Z
                if (x >= w / 4 && x <= w / 2 && y >= h / 3 && y <= h * 2 / 3)
                    data[4].drawPixel(x - w / 4, y - h / 3, cubemap.getPixel(x, y));
                //-Y
                if (x >= w / 4 && x <= w / 2 && y >= h * 2 / 3 && y <= h)
                    data[3].drawPixel(x - w / 4, y - h * 2 / 3, cubemap.getPixel(x, y));
                //+X
                if (x >= w / 2 && x <= w * 3 / 4 && y >= h / 3 && y <= h * 2 / 3)
                    data[0].drawPixel(x - w / 2, y - h / 3, cubemap.getPixel(x, y));
                //-Z
                if (x >= w * 3 / 4 && x <= w && y >= h / 3 && y <= h * 2 / 3)
                    data[5].drawPixel(x - w * 3 / 4, y - h / 3, cubemap.getPixel(x, y));
            }
        cubemap.dispose();
        cubemap = null;
        init();
    }

    private void init() {
        shader = new ShaderProgram(vertexShader, fragmentShader);

        if (!shader.isCompiled())
            throw new GdxRuntimeException(shader.getLog());

        u_worldTrans = shader.getUniformLocation("u_worldTrans");

        quad = createQuad();
        worldTrans = new Matrix4();
        initCubemap();
    }

    private void initCubemap() {
        //bind cubemap
        Gdx.gl20.glBindTexture(GL20.GL_TEXTURE_CUBE_MAP, 0);
        Gdx.gl20.glTexImage2D(GL20.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GL20.GL_RGB, data[0].getWidth(), data[0].getHeight(), 0, GL20.GL_RGB, GL20.GL_UNSIGNED_BYTE, data[0].getPixels());
        Gdx.gl20.glTexImage2D(GL20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GL20.GL_RGB, data[1].getWidth(), data[1].getHeight(), 0, GL20.GL_RGB, GL20.GL_UNSIGNED_BYTE, data[1].getPixels());

        Gdx.gl20.glTexImage2D(GL20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GL20.GL_RGB, data[2].getWidth(), data[2].getHeight(), 0, GL20.GL_RGB, GL20.GL_UNSIGNED_BYTE, data[2].getPixels());
        Gdx.gl20.glTexImage2D(GL20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GL20.GL_RGB, data[3].getWidth(), data[3].getHeight(), 0, GL20.GL_RGB, GL20.GL_UNSIGNED_BYTE, data[3].getPixels());

        Gdx.gl20.glTexImage2D(GL20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GL20.GL_RGB, data[4].getWidth(), data[4].getHeight(), 0, GL20.GL_RGB, GL20.GL_UNSIGNED_BYTE, data[4].getPixels());
        Gdx.gl20.glTexImage2D(GL20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GL20.GL_RGB, data[5].getWidth(), data[5].getHeight(), 0, GL20.GL_RGB, GL20.GL_UNSIGNED_BYTE, data[5].getPixels());

        Gdx.gl20.glTexParameteri(GL20.GL_TEXTURE_CUBE_MAP, GL20.GL_TEXTURE_MIN_FILTER, GL20.GL_LINEAR_MIPMAP_LINEAR);
        Gdx.gl20.glTexParameteri(GL20.GL_TEXTURE_CUBE_MAP, GL20.GL_TEXTURE_MAG_FILTER, GL20.GL_LINEAR);
        Gdx.gl20.glTexParameteri(GL20.GL_TEXTURE_CUBE_MAP, GL20.GL_TEXTURE_WRAP_S, GL20.GL_CLAMP_TO_EDGE);
        Gdx.gl20.glTexParameteri(GL20.GL_TEXTURE_CUBE_MAP, GL20.GL_TEXTURE_WRAP_T, GL20.GL_CLAMP_TO_EDGE);

        Gdx.gl20.glGenerateMipmap(GL20.GL_TEXTURE_CUBE_MAP);
    }


    public void render(Camera camera) {
        shader.begin();
        shader.setUniformMatrix(u_worldTrans, worldTrans.translate(0, 0, -1));
        //skyGameObject.render(shader, GL20.GL_TRIANGLES);
        shader.end();
    }

    public Mesh createQuad() {
        Mesh mesh = new Mesh(true, 4, 6, VertexAttribute.Position(), VertexAttribute.ColorUnpacked(), VertexAttribute.TexCoords(0));
        mesh.setVertices(new float[]
                {-1f, -1f, 0, 1, 1, 1, 1, 0, 1,
                        1f, -1f, 0, 1, 1, 1, 1, 1, 1,
                        1f, 1f, 0, 1, 1, 1, 1, 1, 0,
                        -1f, 1f, 0, 1, 1, 1, 1, 0, 0});
        mesh.setIndices(new short[]{0, 1, 2, 2, 3, 0});
        return mesh;
    }

    @Override
    public void dispose() {
        shader.dispose();
        quad.dispose();
        for (int i = 0; i < 6; i++)
            data[i].dispose();
    }
}