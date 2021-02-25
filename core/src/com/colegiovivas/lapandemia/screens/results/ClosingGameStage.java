package com.colegiovivas.lapandemia.screens.results;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.screens.StagedScreen;

public class ClosingGameStage implements StagedScreen.GameStage {
    private final ResultsScreen resultsScreen;

    public ClosingGameStage(ResultsScreen resultsScreen) {
        this.resultsScreen = resultsScreen;
    }

    @Override
    public void enter() {
        resultsScreen.getClosingTransition().start();
    }

    @Override
    public void leave() {
        resultsScreen.getBackgroundMusic().stop();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0xFF, 0xFF, 0xFF, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        resultsScreen.getClosingTransition().render(delta);

        resultsScreen.draw();
        resultsScreen.getClosingTransition().draw();

        if (!resultsScreen.getClosingTransition().isPlaying()) {
            resultsScreen.finish();
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
