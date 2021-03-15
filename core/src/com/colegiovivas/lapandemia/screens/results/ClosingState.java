package com.colegiovivas.lapandemia.screens.results;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.screens.MultistateScreen;

/**
 * Estado en el que se reproduce la transición final.
 */
public class ClosingState extends MultistateScreen.StateAdapter {
    private final ResultsScreen resultsScreen;

    public ClosingState(ResultsScreen resultsScreen) {
        this.resultsScreen = resultsScreen;
    }

    @Override
    public void enter() {
        if (resultsScreen.isNewHighscore()) {
            resultsScreen.saveScore();
        }
        resultsScreen.getClosingTransition().start();
    }

    @Override
    public void leave() {
        resultsScreen.getBackgroundMusic().stop();
        resultsScreen.getHighscoreMusic().stop();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0xFF, 0xFF, 0xFF, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        resultsScreen.getClosingTransition().render(delta);

        resultsScreen.draw();
        resultsScreen.getClosingTransition().draw();

        if (resultsScreen.getClosingTransition().isComplete()) {
            resultsScreen.finish();
        }
    }
}
