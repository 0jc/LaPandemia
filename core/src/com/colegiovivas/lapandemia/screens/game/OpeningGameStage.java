package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.colegiovivas.lapandemia.screens.StagedScreen;

public class OpeningGameStage implements StagedScreen.GameStage {
    private final GameScreen gameScreen;
    private final OrthographicCamera camera;

    public OpeningGameStage(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        camera = (OrthographicCamera)gameScreen.getWorld().getStage().getCamera();
    }

    @Override
    public void enter() {
        camera.zoom = gameScreen.getWorld().getMaxZoom();
        gameScreen.getOpeningTransition().start();
        gameScreen.getIntroMusic().setLooping(false);
        gameScreen.getIntroMusic().play();
    }

    @Override
    public void leave() {

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0xFF, 0x88, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameScreen.getOpeningTransition().render(delta);

        gameScreen.draw();
        gameScreen.getOpeningTransition().draw();

        if (gameScreen.getOpeningTransition().isComplete()) {
            gameScreen.setGameStage(GameScreen.STAGE_WAIT_AFTER_OPENING);
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
