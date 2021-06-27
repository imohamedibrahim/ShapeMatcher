package com.cutehub.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.cutehub.GlobalReferences;
import com.cutehub.config.UnivConfig;
import com.cutehub.controllers.Controllers;
import com.cutehub.enums.BUTTON_TYPE;
import com.cutehub.enums.GAME_SCREEN;
import com.cutehub.property.ButtonProperty;
import com.cutehub.property.UIProperty;
import com.cutehub.templates.ButtonTemplates;
import com.cutehub.utils.CallBack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class UIAfterGameController implements UIContoller {
    private Stage stage;
    private static UIAfterGameController uiAfterGameController;
    private GlobalReferences globalReferences = GlobalReferences.getInstance();
    private boolean updatingUI = false;
    private Queue<ButtonTemplates> buttonToBeSpawnedQueue = new LinkedList<ButtonTemplates>();
    private Queue<Object[]> buttonToBeUpdatedQueue = new LinkedList<>();
    public boolean done = false;
    private boolean processNextElementFromQueue = true;
    private boolean processNextElementFromUpdateQueue = true;
    private ArrayList<Button> writeButtonList = new ArrayList<>();

    public static UIAfterGameController getInstance() {
        if (uiAfterGameController == null) {
            uiAfterGameController = new UIAfterGameController();
        }
        return uiAfterGameController;
    }

    public static UIAfterGameController removeInstanceAndAssignNew() {
        uiAfterGameController = null;
        return getInstance();
    }

    public void create() {
        stage = new Stage();
        stage.clear();
        Gdx.input.setInputProcessor(stage);
/*        TextureAtlas buttonsAtlas = new TextureAtlas("UI/runner.pack");
        Skin buttonSkin = new Skin();
        buttonSkin.addRegions(buttonsAtlas);
        restartButton = new Button(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("UI/start_button.png")))));
        restartButton.setWidth(Gdx.graphics.getWidth() / 4);
        restartButton.setHeight(Gdx.graphics.getWidth() / 4);
        restartButton.setPosition(Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2 - 50);
        stage.addActor(restartButton);
        restartButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                updatingUI = true;
                globalReferences.reloadAll(GAME_SCREEN.GAME_PLAY);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            }
        });*/
        createShapeMatcherTemplate(false);
    }

    public void createShapeMatcherTemplate(boolean spawnAtMiddle) {
        ButtonTemplates buttonTemplates = new ButtonTemplates();
        buttonTemplates.currentPosition = new Vector2(Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2 - 50);
        buttonTemplates.spriteName = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("UI/start_button.png"))));
        buttonTemplates.finalPosition = buttonTemplates.currentPosition;
        buttonTemplates.size = 120;
        buttonTemplates.button_type = BUTTON_TYPE.SHAPE_MATCHER;
        buttonTemplates.buttonClassType = Button.class;
        buttonTemplates.inputListener = new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ButtonProperty buttonProperty = ((ButtonProperty) event.getTarget().getUserObject());
                buttonProperty.buttonDown();
                updatingUI = true;
                globalReferences.reloadAll(GAME_SCREEN.GAME_PLAY);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ButtonProperty buttonProperty = ((ButtonProperty) event.getTarget().getUserObject());
                buttonProperty.buttonUp();
            }
        };
        buttonToBeSpawnedQueue.add(buttonTemplates);
    }

    public void carSelectionButtonTemplate(boolean spawnAtMiddle) {
        ButtonTemplates buttonTemplates = new ButtonTemplates();
        buttonTemplates.currentPosition = new Vector2(Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 4 - 50);
        buttonTemplates.spriteName = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("UI/start_button.png"))));
        buttonTemplates.finalPosition = buttonTemplates.currentPosition;
        buttonTemplates.size = 120;
        buttonTemplates.button_type = BUTTON_TYPE.SHAPE_MATCHER;
        buttonTemplates.buttonClassType = Button.class;
        buttonTemplates.inputListener = new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ButtonProperty buttonProperty = ((ButtonProperty) event.getTarget().getUserObject());
                buttonProperty.buttonDown();
                updatingUI = true;
                globalReferences.reloadAll(GAME_SCREEN.GAME_PLAY);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ButtonProperty buttonProperty = ((ButtonProperty) event.getTarget().getUserObject());
                buttonProperty.buttonUp();
            }
        };
        buttonToBeSpawnedQueue.add(buttonTemplates);
    }

    @Override
    public boolean isUpdating() {
        return updatingUI;
    }

    public void update(float dt) {
        if (buttonToBeSpawnedQueue.size() == 0 && buttonToBeUpdatedQueue.size() == 0 && processNextElementFromQueue && processNextElementFromUpdateQueue) {
            updatingUI = false;
            done = true;
        } else if (buttonToBeUpdatedQueue.size() == 0 && buttonToBeSpawnedQueue.size() > 0 && processNextElementFromQueue) {
            updatingUI = true;
            ButtonTemplates buttonTemplates = buttonToBeSpawnedQueue.remove();
            createButtonFromTemplate(buttonTemplates);
            processNextElementFromQueue = false;
        }
        for (Iterator<Actor> iterator = stage.getActors().iterator(); iterator.hasNext(); ) {
            Actor actor = iterator.next();
            if (actor.getUserObject() instanceof UIProperty)
                ((UIProperty) actor.getUserObject()).update(dt);
        }
    }

    private void createButtonFromTemplate(ButtonTemplates buttonTemplates) {
        Button tmpButton;
        if (buttonTemplates.buttonClassType == PowerButton.class) {
            tmpButton = new PowerButton(buttonTemplates.spriteName);
        } else {
            tmpButton = new Button(buttonTemplates.spriteName);
        }
        tmpButton.addListener(buttonTemplates.inputListener);
        CallBack callBack = new CallBack() {
            @Override
            public void call(Object object) {
                processNextElementFromQueue = true;
            }
        };
        ButtonProperty tmpButtonProperty = (buttonTemplates.finalPosition != null) ?
                new ButtonProperty(tmpButton, buttonTemplates.size, buttonTemplates.currentPosition, buttonTemplates.finalPosition, callBack, true, buttonTemplates.button_type, buttonTemplates.userObject) :
                new ButtonProperty(tmpButton, buttonTemplates.size, buttonTemplates.currentPosition, callBack, true, buttonTemplates.button_type, buttonTemplates.userObject);
        tmpButton.setUserObject(tmpButtonProperty);
        stage.addActor(tmpButton);
        writeButtonList.add(tmpButton);
    }

    @Override
    public void reAlignButton() {

    }

    public void render() {
        if (stage == null) return;
        stage.act();
        stage.draw();
    }

    public void dispose() {

    }

}
