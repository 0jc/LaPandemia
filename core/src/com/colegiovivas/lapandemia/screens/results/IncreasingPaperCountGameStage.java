package com.colegiovivas.lapandemia.screens.results;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.screens.StagedScreen;

public class IncreasingPaperCountGameStage implements StagedScreen.GameStage {
    private final ResultsScreen resultsScreen;
    private final CounterAnimator counterAnimator;

    public IncreasingPaperCountGameStage(ResultsScreen resultsScreen) {
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
            resultsScreen.getResultsView().setPaperCount((int)counterAnimator.update(delta));
        } else {
            resultsScreen.setGameStage(ResultsScreen.STAGE_PAPER_COUNT_POST_WAIT);
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
