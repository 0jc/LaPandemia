package com.colegiovivas.lapandemia.screens.results;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.screens.StagedScreen;

public class WaitGameStage implements StagedScreen.GameStage {
    private final ResultsScreen resultsScreen;
    private final float totalTime;
    private final int nextGameStage;
    private float timeWaited;

    public WaitGameStage(ResultsScreen resultsScreen, float totalTime, int nextGameStage) {
        this.resultsScreen = resultsScreen;
        this.totalTime = totalTime;
        this.nextGameStage = nextGameStage;
    }

    @Override
    public void enter() {
        timeWaited = 0;
    }

    @Override
    public void leave() {

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0xFF, 0xFF, 0xFF, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        resultsScreen.draw();

        timeWaited += delta;
        if (timeWaited >= totalTime) {
            resultsScreen.setGameStage(nextGameStage);
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
