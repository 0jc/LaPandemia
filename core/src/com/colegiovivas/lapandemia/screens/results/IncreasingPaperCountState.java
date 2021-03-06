package com.colegiovivas.lapandemia.screens.results;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.screens.MultistateScreen;

/**
 * Estado en el que el valor de la vista de la puntuación de la partida
 * se incrementa gradualmente hasta alcanzar su valor real.
 */
public class IncreasingPaperCountState implements MultistateScreen.State {
    private final ResultsScreen resultsScreen;
    private final CounterAnimator counterAnimator;

    public IncreasingPaperCountState(ResultsScreen resultsScreen) {
        this.resultsScreen = resultsScreen;
        this.counterAnimator = new CounterAnimator();
    }

    @Override
    public void enter() {
        this.counterAnimator.init(0.3f, 0, resultsScreen.getPaperCount());
        resultsScreen.getResultsView().setPaperCountVisible(true);
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

        if (counterAnimator.isIncreasing()) {
            int newValue = (int)counterAnimator.update(delta);
            resultsScreen.getResultsView().setPaperCount(newValue);
            if (newValue > resultsScreen.getHighscore()) {
                resultsScreen.getResultsView().setTitleColor(Color.GOLD);
            }
        } else {
            resultsScreen.setState(ResultsScreen.STATE_PAPER_COUNT_FINISHED_MUSIC);
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
