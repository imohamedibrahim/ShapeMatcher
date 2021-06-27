package com.cutehub.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.cutehub.GlobalReferences;
import com.cutehub.ScoreManager;
import com.cutehub.controllers.Controllers;
import com.cutehub.enums.BUTTON_TYPE;
import com.cutehub.enums.PLAY_SHAPE;
import com.cutehub.gameplay.GamePlayController;
import com.cutehub.gameplay.PlayerController;
import com.cutehub.property.ButtonProperty;
import com.cutehub.property.LabelProperty;
import com.cutehub.property.UIProperty;
import com.cutehub.templates.ButtonTemplates;
import com.cutehub.utils.CallBack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;


public class UIGamePlayController implements UIContoller {

    public boolean updatingUI = true;
    private static float WIDTH = Gdx.graphics.getWidth();
    private static float HEIGHT = Gdx.graphics.getHeight();

    // configs //
    private String[] names = {"circleButton", "squareButton", "triangleButton", "starButton"};
    private PLAY_SHAPE[] play_shapes = {PLAY_SHAPE.CIRCLE, PLAY_SHAPE.SQUARE, PLAY_SHAPE.TRIANGLE, PLAY_SHAPE.STAR};
    float buttonWidth = Gdx.graphics.getWidth() / (4.5f);

    // noChange //
    private SpriteBatch batch;
    private TextureAtlas buttonsAtlas;
    private Stage stage;
    private Skin buttonSkin;
    private Controllers controllers;
    private GlobalReferences globalReferences = GlobalReferences.getInstance();
    private int numberOfMatcherShapeButtonCurrentlyDisplayed;
    private ArrayList<Button> listOfAllButtons = new ArrayList<>();
    private Text scoreText;
    private Label scoreLabel;
    private boolean processNextElementFromQueue;
    private boolean processNextElementFromUpdateQueue;
    private Queue<ButtonTemplates> buttonToBeSpawnedQueue = new LinkedList<ButtonTemplates>();
    private Queue<Object[]> buttonToBeUpdatedQueue = new LinkedList<>();

    // helper ButtonList to avoid concurrency
    private ArrayList<Button> writeButtonList = new ArrayList<>();
    private boolean done = false;
    private static UIGamePlayController uiGamePlayController;
    private LabelProperty scoreLabelProperty;

    public float shootPowerLevel;

    public static UIGamePlayController getInstance() {
        if (uiGamePlayController == null) {
            uiGamePlayController = new UIGamePlayController();
        }
        return uiGamePlayController;
    }

    public static UIGamePlayController removeInstanceAndAssignNew() {
        uiGamePlayController = null;
        return getInstance();
    }

    public void create() {
        stage = new Stage();
        stage.clear();
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
        buttonsAtlas = new TextureAtlas("UI/runner.pack");
        buttonSkin = new Skin();
        buttonSkin.addRegions(buttonsAtlas);
        numberOfMatcherShapeButtonCurrentlyDisplayed = 0;
        createPowerShootButton();
        controllers = Controllers.getInstance();
        addScoreText();
        while (numberOfMatcherShapeButtonCurrentlyDisplayed < globalReferences.univConfig.numberOfMatchShapes) {
            createShapeMatcherTemplate(false);
        }
        processNextElementFromQueue = true;
        processNextElementFromUpdateQueue = true;
    }

    public void createPowerShootButton() {
        ButtonTemplates buttonTemplates = new ButtonTemplates();
        Vector2 initialPosition =  new Vector2(WIDTH - 1.1f * WIDTH / 4, 1.25f * HEIGHT / 5);
        buttonTemplates.currentPosition = initialPosition;
        buttonTemplates.spriteName = buttonSkin.getDrawable("cannonButton");
        buttonTemplates.finalPosition = initialPosition;
        buttonTemplates.size = buttonWidth;
        buttonTemplates.buttonClassType = PowerButton.class;
        buttonTemplates.inputListener = new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ButtonProperty buttonProperty = ((ButtonProperty) event.getTarget().getUserObject());
                buttonProperty.buttonDown();
                if (shootPowerLevel < 1.0) return true;
                shootPowerLevel = 0;
                getPlayerController().powerShootShapes();
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ButtonProperty buttonProperty = ((ButtonProperty) event.getTarget().getUserObject());
                buttonProperty.buttonUp();
            }
        };
        buttonToBeSpawnedQueue.add(buttonTemplates);
    }

    private PlayerController getPlayerController() {
        return ((GamePlayController) globalReferences.controller.mainPlayController.currentPlayController).playerController;
    }

    public void createShapeMatcherTemplate(boolean spawnAtMiddle) {
        ButtonTemplates buttonTemplates = new ButtonTemplates();
        float remainingWidth = WIDTH - buttonWidth * globalReferences.univConfig.numberOfMatchShapes;
        remainingWidth = remainingWidth / (globalReferences.univConfig.numberOfMatchShapes + 1);
        Vector2 finalPosition = new Vector2((numberOfMatcherShapeButtonCurrentlyDisplayed + 1) * remainingWidth + (numberOfMatcherShapeButtonCurrentlyDisplayed) * buttonWidth, 0.5f * HEIGHT / 5);
        buttonTemplates.currentPosition = finalPosition;
        if (spawnAtMiddle)
            buttonTemplates.currentPosition = new Vector2(WIDTH / 2 - buttonWidth / 2, HEIGHT / 2 - buttonWidth / 2);
        buttonTemplates.spriteName = buttonSkin.getDrawable(names[numberOfMatcherShapeButtonCurrentlyDisplayed]);
        buttonTemplates.finalPosition = finalPosition;
        buttonTemplates.size = buttonWidth;
        buttonTemplates.button_type = BUTTON_TYPE.SHAPE_MATCHER;
        buttonTemplates.userObject = play_shapes[numberOfMatcherShapeButtonCurrentlyDisplayed];
        buttonTemplates.buttonClassType = Button.class;
        buttonTemplates.inputListener = new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ButtonProperty buttonProperty = ((ButtonProperty) event.getTarget().getUserObject());
                buttonProperty.buttonDown();
                getPlayerController().shootShape((PLAY_SHAPE) buttonProperty.userObject);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ButtonProperty buttonProperty = ((ButtonProperty) event.getTarget().getUserObject());
                buttonProperty.buttonUp();
            }
        };
        buttonToBeSpawnedQueue.add(buttonTemplates);
        numberOfMatcherShapeButtonCurrentlyDisplayed++;
    }

    public void reAlignButton() {
        if (updatingUI) return;
        Array<Actor> actorArrayList = stage.getActors();
        int i = 0;
        for (Actor actor : actorArrayList) {
            if (!(actor instanceof Button)) continue;
            Button button = (Button) actor;
            ButtonProperty buttonProperty = ((ButtonProperty) button.getUserObject());
            if (buttonProperty.button_type != BUTTON_TYPE.SHAPE_MATCHER) continue;
            buttonToBeUpdatedQueue.add(new Object[]{buttonProperty, i});
            i++;
        }
        while (globalReferences.univConfig.numberOfMatchShapes > numberOfMatcherShapeButtonCurrentlyDisplayed) {
            createShapeMatcherTemplate(true);
        }
        processNextElementFromUpdateQueue = true;
        processNextElementFromQueue = true;
        done = false;
    }

    public void reAlignButtons(ButtonProperty buttonProperty, int i) {
        int numberOfButtons = globalReferences.univConfig.numberOfMatchShapes;
        float remainingWidth = WIDTH - buttonProperty.size * numberOfButtons;
        remainingWidth = remainingWidth / (numberOfButtons + 1);
        Vector2 position = new Vector2((i + 1) * remainingWidth + (i) * buttonProperty.size, 0.5f * HEIGHT / 5);
        buttonProperty.reAlign(position, new CallBack() {
            @Override
            public void call(Object object) {
                processNextElementFromUpdateQueue = true;
            }
        });
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

    private void addScoreText() {
        scoreLabel = new Label("0", new Label.LabelStyle(new BitmapFont(Gdx.files.internal("UI/square-font.fnt"), false), Color.WHITE));
        scoreLabelProperty = new LabelProperty(scoreLabel, WIDTH / 2, HEIGHT - HEIGHT / 6);
        globalReferences.scoreManager.addCallBack(new CallBack() {
            @Override
            public void call(Object object) {
                scoreLabelProperty.updateScore(((ScoreManager) object).getCurrentScore());
            }
        });
        scoreLabelProperty.container.setUserObject(scoreLabelProperty);
        stage.addActor(scoreLabelProperty.container);
    }


    public void update(float dt) {
        if (shootPowerLevel < 1.0f) shootPowerLevel += 0.001f;
        //if (done) return;
        if (buttonToBeSpawnedQueue.size() == 0 && buttonToBeUpdatedQueue.size() == 0 && processNextElementFromQueue && processNextElementFromUpdateQueue) {
            updatingUI = false;
            done = true;
        } else if (buttonToBeUpdatedQueue.size() == 0 && buttonToBeSpawnedQueue.size() > 0 && processNextElementFromQueue) {
            updatingUI = true;
            ButtonTemplates buttonTemplates = buttonToBeSpawnedQueue.remove();
            createButtonFromTemplate(buttonTemplates);
            processNextElementFromQueue = false;
        } else if (buttonToBeUpdatedQueue.size() > 0 && processNextElementFromUpdateQueue) {
            updatingUI = true;
            Object[] tmpObject = buttonToBeUpdatedQueue.remove();
            if (tmpObject != null && tmpObject.length > 0)
                reAlignButtons((ButtonProperty) tmpObject[0], (Integer) tmpObject[1]);
        }
        for (Iterator<Actor> iterator = stage.getActors().iterator(); iterator.hasNext(); ) {
            Actor actor = iterator.next();
            if (actor.getUserObject() instanceof UIProperty)
                ((UIProperty) actor.getUserObject()).update(dt);
        }
        if (writeButtonList.size() > 0) {
            listOfAllButtons.addAll(writeButtonList);
        }
    }

    public void render() {
        batch.begin();
        stage.act();
        stage.draw();
        batch.end();
    }

    public boolean isUpdating() {
        return updatingUI;
    }

    public void resize(int width, int height) {
    }

    public void pause() {
    }

    public void resume() {
    }

    public void dispose() {
        batch.dispose();
        buttonsAtlas.dispose();
    }

}
