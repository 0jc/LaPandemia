package com.colegiovivas.lapandemia.screens;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.colegiovivas.lapandemia.LaPandemia;

public class CountdownSubscreen extends Subscreen {
    private static final float DELAY_AFTER_FADE = 0.2f;
    private static final float FADE_OUT_DURATION = 1.0f;

    private final Stage stage;
    private final Image[] images;
    private final Sound numberBeep;
    private final Sound goBeep;
    private int countdownNum = -1;
    private float waitedTime = 0;

    public CountdownSubscreen(LaPandemia game) {
        Camera uiCamera = new OrthographicCamera();
        Viewport uiViewport = new StretchViewport(800, 480, uiCamera);
        stage = new Stage(uiViewport);

        uiCamera.position.x = 0;
        uiCamera.position.y = 0;

        images = new Image[4];
        Array<TextureAtlas.AtlasRegion> regions
                = ((TextureAtlas)game.assetManager.get("images.pack")).findRegions("countdown");
        for (int i = 0; i <= 3; i++) {
            images[i] = new Image(regions.get(i));
            images[i].setPosition(-images[i].getWidth()/2, -images[i].getHeight()/2);
            images[i].setVisible(false);
            stage.addActor(images[i]);
        }

        numberBeep = game.assetManager.get("audio/countdown-beep-number.wav");
        goBeep = game.assetManager.get("audio/countdown-beep-go.wav");
    }

    @Override
    protected void drawWithinBounds(float delta) {
        stage.draw();
    }

    @Override
    public void act(float delta) {
        if (countdownNum != -1) {
            float currentAlpha = images[countdownNum].getColor().a;
            if (currentAlpha == 1) {
                if (countdownNum > 0) {
                    numberBeep.play();
                } else {
                    goBeep.play();
                }
                setNum(countdownNum);
            } else if (currentAlpha == 0) {
                waitedTime += delta;
                if (waitedTime >= DELAY_AFTER_FADE) {
                    resetNum(countdownNum);
                    countdownNum--;
                    waitedTime = 0;
                }
            }
            stage.act();
        }
    }

    private void setNum(int num) {
        images[num].setVisible(true);
        images[num].addAction(Actions.fadeOut(FADE_OUT_DURATION));
    }

    private void resetNum(int num) {
        images[num].setVisible(false);
        images[num].getColor().a = 1;
    }

    public void startCountdown() {
        if (countdownNum != -1) {
            resetNum(countdownNum);
        }

        countdownNum = 3;
    }

    public boolean isCountingDown() {
        return countdownNum > 0;
    }

    public boolean isActing() {
        return countdownNum != -1;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }
}
