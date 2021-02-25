package com.colegiovivas.lapandemia.screens.results;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.screens.StagedScreen;

public class TimeVisibleGameStage implements StagedScreen.GameStage {
    private final ResultsScreen resultsScreen;
    private boolean visible;

    public TimeVisibleGameStage(ResultsScreen resultsScreen, boolean visible) {
        this.resultsScreen = resultsScreen;
        this.visible = visible;
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
        Gdx.gl.glClearColor(0xFF, 0xFF, 0xFF, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        resultsScreen.getResultsView().setTimeVisible(visible);
        resultsScreen.draw();
        resultsScreen.setGameStage(ResultsScreen.STAGE_TIME_PRE_WAIT);
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
