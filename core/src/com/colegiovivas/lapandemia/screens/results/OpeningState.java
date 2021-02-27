package com.colegiovivas.lapandemia.screens.results;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.screens.MultistateScreen;

/**
 * Estado en el que se reproduce la transición inicial.
 */
public class OpeningState implements MultistateScreen.State {
    private final ResultsScreen resultsScreen;

    public OpeningState(ResultsScreen resultsScreen) {
        this.resultsScreen = resultsScreen;
    }

    @Override
    public void enter() {
        resultsScreen.getBackgroundMusic().setLooping(true);
        resultsScreen.getBackgroundMusic().play();
        resultsScreen.getOpeningTransition().start();
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

        resultsScreen.getOpeningTransition().render(delta);

        resultsScreen.draw();
        resultsScreen.getOpeningTransition().draw();

        if (resultsScreen.getOpeningTransition().isComplete()) {
            resultsScreen.setState(ResultsScreen.STAGE_TIME_VISIBLE);
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
