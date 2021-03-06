package com.colegiovivas.lapandemia.screens.settings;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.hardware.HardwareWrapper;
import com.colegiovivas.lapandemia.screens.MonochromaticDrawable;
import com.colegiovivas.lapandemia.screens.credits.CreditsScreen;

/**
 * Pantalla de ajustes.
 */
public class SettingsScreen implements Screen {
    private final LaPandemia main;
    private final Stage stage;
    private final InputMultiplexer inputProcessor;
    private final InputProcessor noInput;

    public SettingsScreen(LaPandemia main) {
        this.main = main;

        Camera camera = new OrthographicCamera();
        Viewport viewport = new StretchViewport(400, 240, camera);
        stage = new Stage(viewport);

        Skin cloudFormSkin = main.assetManager.get("cloud-form-skin/cloud-form-ui.json");

        Label topTextLabel = new Label("Ajustes", cloudFormSkin);
        final CheckBox gyroscopeCheckBox = new CheckBox("", cloudFormSkin);
        final CheckBox vibratorCheckBox = new CheckBox("", cloudFormSkin);

        Table outerTable = new Table();
        outerTable.setFillParent(true);
        outerTable.setBackground(new MonochromaticDrawable(main, Color.SKY));
        outerTable.pad(10).top();

        Table headerTable = new Table();
        headerTable.setBackground(new MonochromaticDrawable(main, Color.LIME));
        headerTable.pad(10);

        headerTable.add(topTextLabel).expandX().left().row();

        outerTable.add(headerTable).expandX().fillX().row();

        Table formTable = new Table();
        formTable.setBackground(new MonochromaticDrawable(main, Color.WHITE));

        formTable.add(new Label("Uso del giroscopio:", cloudFormSkin)).padRight(20);
        if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope)) {
            formTable.add(gyroscopeCheckBox).left();
        } else {
            formTable.add(new Label("No disponible", cloudFormSkin)).left();
        }
        formTable.row();

        formTable.add(new Label("Uso del vibrador:", cloudFormSkin)).padTop(20);
        if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Vibrator)) {
            formTable.add(vibratorCheckBox).padTop(20).left();
        } else {
            formTable.add(new Label("No disponible", cloudFormSkin)).padTop(20).left();
        }
        formTable.row();

        outerTable.add(formTable).expand().fill().row();

        stage.addActor(outerTable);
        noInput = new InputAdapter();
        inputProcessor = new InputMultiplexer();
        inputProcessor.addProcessor(stage);
        inputProcessor.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    SettingsScreen.this.main.navigatedBackToMain(SettingsScreen.this);
                    return true;
                }

                return false;
            }
        });

        final HardwareWrapper hardwareWrapper = main.hardwareWrapper;
        vibratorCheckBox.setChecked(hardwareWrapper.getVibratorEnabled());
        gyroscopeCheckBox.setChecked(hardwareWrapper.getGyroscopeEnabled());

        vibratorCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hardwareWrapper.setVibratorEnabled(vibratorCheckBox.isChecked());
            }
        });

        gyroscopeCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hardwareWrapper.setGyroscopeEnabled(gyroscopeCheckBox.isChecked());
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0xFF, 0xFF, 0xFF, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(noInput);
        Gdx.input.setCatchKey(Input.Keys.BACK, false);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
