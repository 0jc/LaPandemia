package com.colegiovivas.lapandemia.screens.results;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.screens.StagedScreen;

public class WaitMusicGameStage implements StagedScreen.GameStage {
    private final ResultsScreen resultsScreen;
    private final int nextGameStage;
    private final Music music;

    public WaitMusicGameStage(Music music, ResultsScreen resultsScreen, int nextGameStage) {
        this.resultsScreen = resultsScreen;
        this.nextGameStage = nextGameStage;
        this.music = music;
    }

    @Override
    public void enter() {
        music.setLooping(false);
        music.play();
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
        if (!music.isPlaying()) {
            resultsScreen.setGameStage(nextGameStage);
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
