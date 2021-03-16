package com.colegiovivas.lapandemia.screens.results;

import com.badlogic.gdx.audio.Music;
import com.colegiovivas.lapandemia.screens.MultistateScreen;

/**
 * Estado de reproducción de música. Se reproduce una única vez una
 * música especificada y después se cambia a otro estado.
 */
public abstract class WaitMusicState<T extends ResultsScreen.States> extends MultistateScreen.StateAdapter {
    private final Music music;
    private final ResultsScreen resultsScreen;
    private final T nextState;

    public WaitMusicState(Music music, ResultsScreen resultsScreen, T nextState) {
        this.music = music;
        this.resultsScreen = resultsScreen;
        this.nextState = nextState;
    }

    @Override
    public void enter() {
        music.setLooping(false);
        music.play();
    }

    @Override
    public void render(float delta) {
        renderPlayingMusic(delta);
        if (!music.isPlaying()) {
            resultsScreen.setState(nextState);
        }
    }

    public abstract void renderPlayingMusic(float delta);
}
