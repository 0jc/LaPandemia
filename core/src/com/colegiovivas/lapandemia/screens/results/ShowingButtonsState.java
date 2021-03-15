package com.colegiovivas.lapandemia.screens.results;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.colegiovivas.lapandemia.screens.MultistateScreen;

/**
 * Estado en el que se muestran los botones y, en caso de haber logrado
 * batir el récord previo, se reproduce la música congratulatoria y se
 * muestra en el formulario el campo donde se pide al usuario que
 * introduzca su nombre.
 */
public class ShowingButtonsState extends MultistateScreen.StateAdapter {
    private final ResultsScreen resultsScreen;
    private final InputProcessor noInput;

    public ShowingButtonsState(final ResultsScreen resultsScreen) {
        this.resultsScreen = resultsScreen;
        noInput = new InputAdapter();

        resultsScreen.getResultsView().addReturnListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                resultsScreen.setPlayAgain(false);
                resultsScreen.setState(ResultsScreen.STATE_CLOSING);
            }
        });

        resultsScreen.getResultsView().addRetryListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                resultsScreen.setPlayAgain(true);
                resultsScreen.setState(ResultsScreen.STATE_CLOSING);
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
}
