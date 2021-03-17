package com.colegiovivas.lapandemia.screens.results;

import com.badlogic.gdx.audio.Music;
import com.colegiovivas.lapandemia.screens.MultistateScreen;

/**
 * Estado de reproducción de música. Se reproduce una única vez una
 * música especificada y después se cambia a otro estado especificado.
 */
public abstract class WaitMusicState<T extends ResultsScreen.States> extends MultistateScreen.StateAdapter {
    /**
     * Música que se reproduce durante este estado.
     */
    private final Music music;

    /**
     * Pantalla en la que se reproduce {@link #music}.
     */
    private final ResultsScreen resultsScreen;

    /**
     * Estado al que {@link #resultsScreen} cambia una vez se termina la reproducción de {@link #music}.
     */
    private final T nextState;

    /**
     * Inicializa el estado.
     * @param music Valor para {@link #music}.
     * @param resultsScreen Valor para {@link #resultsScreen}.
     * @param nextState Valor para {@link #nextState}.
     */
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

    /**
     * Método en el que {@link #render(float)} delega parcialmente mientras la música está
     * en reproducción.
     * @param delta El valor delta que {@link #render(float)} ha recibido.
     */
    public abstract void renderPlayingMusic(float delta);
}
