package com.cutehub.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class Text extends Actor {

    BitmapFont font;
    int myScore;
    Vector2 position;

    public Text(Vector2 position) {
        Label hpLabel = new Label("",new Label.LabelStyle(new BitmapFont(Gdx.files.internal("UI/square-font.fnt"), false), Color.WHITE));
        this.position = position;
    }

    @Override
    public void setScale(float scale) {
        font.getData().setScale(scale);
    }

    @Override
    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
    }

    public void setPositionWithReference(Vector2 positionReference) {
        position = positionReference;
    }

    public Text() {
        this(new Vector2(10, 10));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //font.draw(batch, String.valueOf(myScore), position.x, position.y, 60, 1, false);
    }

    public Actor hit(float x, float y) {
        return null;
    }

    public void updateScore(int score) {
        myScore = score;
    }
}