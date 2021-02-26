package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.screens.StagedScreen;

public class WaitIntroMusicGameStage implements StagedScreen.GameStage {
    private final GameScreen gameScreen;

    public WaitIntroMusicGameStage(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
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

        if (!gameScreen.getIntroMusic().isPlaying()) {
            gameScreen.setGameStage(GameScreen.STAGE_WAIT_AFTER_ZOOM_IN);
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
