package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.colegiovivas.lapandemia.screens.StagedScreen;

public class ZoomInGameStage implements StagedScreen.GameStage {
    private final GameScreen gameScreen;
    private final OrthographicCamera camera;
    private final float speed;

    public ZoomInGameStage(GameScreen gameScreen, float speed) {
        this.gameScreen = gameScreen;
        this.speed = speed;
        camera = (OrthographicCamera)gameScreen.getWorld().getStage().getCamera();
    }

    @Override
    public void enter() {
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

        gameScreen.draw();

        if (camera.zoom > 1) {
            camera.zoom = Math.max(camera.zoom - delta*speed, 1);
        } else if (!gameScreen.getIntroMusic().isPlaying()) {
            gameScreen.setGameStage(GameScreen.STAGE_WAIT_INTRO_MUSIC);
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
