package com.colegiovivas.lapandemia.screens.results;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.screens.StagedScreen;

public class ShowingContinueButtonGameStage implements StagedScreen.GameStage {
    private final ResultsScreen resultsScreen;
    private final ResultsView.ContinueListener continueListener;
    private final InputProcessor noInput;

    public ShowingContinueButtonGameStage(final ResultsScreen resultsScreen) {
        this.resultsScreen = resultsScreen;
        noInput = new InputAdapter();
        continueListener = new ResultsView.ContinueListener() {
            @Override
            public void continueClicked() {
                resultsScreen.setGameStage(ResultsScreen.STAGE_CLOSING);
            }
        };
    }

    @Override
    public void enter() {
        resultsScreen.getBackgroundMusic().setLooping(true);
        resultsScreen.getBackgroundMusic().play();
        resultsScreen.getResultsView().setContinueButtonVisible(true);
        resultsScreen.getResultsView().setContinueListener(continueListener);
    }

    @Override
    public void leave() {
        Gdx.input.setInputProcessor(noInput);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(resultsScreen.getResultsView().getStage());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0xFF, 0xFF, 0xFF, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        resultsScreen.draw();
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
