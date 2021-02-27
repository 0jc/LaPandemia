package com.colegiovivas.lapandemia.screens.results;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.screens.StagedScreen;

public class TimeVisibleGameStage implements StagedScreen.GameStage {
    private final ResultsScreen resultsScreen;
    private final boolean visible;
    private final Music statShownMusic;

    public TimeVisibleGameStage(LaPandemia main, ResultsScreen resultsScreen, boolean visible) {
        this.resultsScreen = resultsScreen;
        this.visible = visible;
        statShownMusic = main.assetManager.get("audio/stat-shown.wav");
    }

    @Override
    public void enter() {
        statShownMusic.setLooping(false);
        statShownMusic.play();
        resultsScreen.getResultsView().setTimeVisible(visible);
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
        if (!statShownMusic.isPlaying()) {
            resultsScreen.setGameStage(ResultsScreen.STAGE_INCREASING_TIME);
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
