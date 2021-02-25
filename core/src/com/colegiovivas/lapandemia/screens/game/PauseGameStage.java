package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.screens.StagedScreen;

public class PauseGameStage implements StagedScreen.GameStage {
    private final GameScreen gameScreen;

    private boolean pausing;
    private boolean resume;

    public PauseGameStage(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void enter() {
        pausing = true;
        resume = false;
        gameScreen.getWorld().setPaused(true);
        gameScreen.getStats().setPaused(true);
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

        if (pausing) {
            if (Gdx.input.getGyroscopeY() >= GameScreen.Y_GYROSCOPE_PAUSE_TRESHOLD) {
                pausing = false;
            }
        } else if (!resume){
            if (Gdx.input.getGyroscopeY() < GameScreen.Y_GYROSCOPE_PAUSE_TRESHOLD) {
                resume = true;
            }
        } else if (Gdx.input.getGyroscopeY() >= GameScreen.Y_GYROSCOPE_PAUSE_TRESHOLD) {
            gameScreen.setGameStage(GameScreen.STAGE_PLAYING);
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
