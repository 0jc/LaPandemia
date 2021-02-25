package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.screens.StagedScreen;

public class WaitGameStage implements StagedScreen.GameStage {
    private final GameScreen gameScreen;
    private final float totalTime;
    private final int nextGameStage;

    private float waitedTime;

    public WaitGameStage(GameScreen gameScreen, float totalTime, int nextGameStage) {
        this.gameScreen = gameScreen;
        this.totalTime = totalTime;
        this.nextGameStage = nextGameStage;
    }

    @Override
    public void enter() {
        waitedTime = 0;
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

        waitedTime += delta;
        if (waitedTime >= totalTime) {
            gameScreen.setGameStage(nextGameStage);
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
