package com.colegiovivas.lapandemia.screens.results;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.screens.MultistateScreen;

/**
 * Estado de reproducción de música. Se reproduce una única vez una
 * música especificada y después se cambia a otro estado.
 */
public class WaitMusicState implements MultistateScreen.State {
    private final ResultsScreen resultsScreen;
    /**
     * Siguiente estado al que se salta al terminarse la música.
     */
    private final int nextState;
    /**
     * Música que se reproduce.
     */
    private final Music music;

    public WaitMusicState(Music music, ResultsScreen resultsScreen, int nextState) {
        this.resultsScreen = resultsScreen;
        this.nextState = nextState;
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
            resultsScreen.setState(nextState);
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
