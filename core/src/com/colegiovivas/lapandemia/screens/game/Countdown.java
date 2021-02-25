package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.colegiovivas.lapandemia.LaPandemia;

public class Countdown {
    private static final float DELAY_AFTER_FADE = 0.2f;
    private static final float FADE_OUT_DURATION = 1.0f;

    private final Stage stage;
    private final Image[] images;
    private final Sound numberBeep;
    private final Sound goBeep;

    private int countdownNum = -1;
    private float postFadeWaitedTime = 0;

    public Countdown(LaPandemia main) {
        OrthographicCamera camera = new OrthographicCamera();
        Viewport viewport = new StretchViewport(800, 480, camera);
        stage = new Stage(viewport);

        camera.position.x = 0;
        camera.position.y = 0;

        images = new Image[4];
        Array<TextureAtlas.AtlasRegion> regions
                = ((TextureAtlas)main.assetManager.get("images.pack")).findRegions("countdown");
        for (int i = 0; i <= 3; i++) {
            images[i] = new Image(regions.get(i));
            images[i].setPosition(-images[i].getWidth()/2, -images[i].getHeight()/2);
            images[i].setVisible(false);
            stage.addActor(images[i]);
        }

        numberBeep = main.assetManager.get("audio/countdown-beep-number.wav");
        goBeep = main.assetManager.get("audio/countdown-beep-go.wav");
    }

    public void render(float delta) {
        if (countdownNum > -1) {
            float currentAlpha = images[countdownNum].getColor().a;
            if (currentAlpha == 1) {
                if (countdownNum > 0) {
                    numberBeep.play();
                } else {
                    goBeep.play();
                }
                images[countdownNum].setVisible(true);
                images[countdownNum].addAction(Actions.fadeOut(FADE_OUT_DURATION));
            } else if (currentAlpha == 0) {
                postFadeWaitedTime += delta;
                if (postFadeWaitedTime >= DELAY_AFTER_FADE) {
                    images[countdownNum].setVisible(false);
                    images[countdownNum].getColor().a = 1;
                    countdownNum--;
                    postFadeWaitedTime = 0;
                }
            }
            stage.act();
        }
    }

    public Stage getStage() {
        return stage;
    }

    public boolean isPlaying() {
        return countdownNum > -1;
    }

    public boolean isCountingDown() {
        return countdownNum > 0;
    }

    public void start() {
        if (countdownNum != -1) {
            resetNum(countdownNum);
        }

        countdownNum = 3;
    }

    private void setNum(int num) {
        images[num].setVisible(true);
        images[num].addAction(Actions.fadeOut(FADE_OUT_DURATION));
    }

    private void resetNum(int num) {
        images[num].setVisible(false);
        images[num].getColor().a = 1;
    }
}
