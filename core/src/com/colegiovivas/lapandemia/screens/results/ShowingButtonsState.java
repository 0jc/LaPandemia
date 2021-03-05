package com.colegiovivas.lapandemia.screens.results;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.screens.MultistateScreen;

/**
 * Estado en el que se muestran los botones y, en caso de haber logrado
 * batir el récord previo, se reproduce la música congratulatoria y se
 * muestra en el formulario el campo donde se pide al usuario que
 * introduzca su nombre.
 */
public class ShowingButtonsState implements MultistateScreen.State {
    private final ResultsScreen resultsScreen;
    private final InputProcessor noInput;

    public ShowingButtonsState(final ResultsScreen resultsScreen) {
        this.resultsScreen = resultsScreen;
        noInput = new InputAdapter();

        resultsScreen.getResultsView().setReturnListener(new ResultsView.ReturnListener() {
            @Override
            public void returnClicked() {
                resultsScreen.setPlayAgain(false);
                resultsScreen.setState(ResultsScreen.STAGE_CLOSING);
            }
        });

        resultsScreen.getResultsView().setRetryListener(new ResultsView.RetryListener() {
            @Override
            public void retryClicked() {
                resultsScreen.setPlayAgain(true);
                resultsScreen.setState(ResultsScreen.STAGE_CLOSING);
            }
        });
    }

    @Override
    public void enter() {
        resultsScreen.getResultsView().setReturnButtonVisible(true);
        resultsScreen.getResultsView().setRetryButtonVisible(true);
        if (resultsScreen.isNewHighscore()) {
            resultsScreen.getResultsView().setNicknameVisible(true);
            resultsScreen.getHighscoreMusic().setLooping(false);
            resultsScreen.getHighscoreMusic().play();
        }
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
