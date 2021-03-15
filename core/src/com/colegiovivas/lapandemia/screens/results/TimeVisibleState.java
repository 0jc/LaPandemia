package com.colegiovivas.lapandemia.screens.results;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.screens.MultistateScreen;

/**
 * Estado en el que se hace visible la estadística de la duración de la partida
 * (mostrándose todavía como 00:00) y se reproduce el efecto sonoro asociado.
 */
public class TimeVisibleState extends MultistateScreen.StateAdapter {
    private final ResultsScreen resultsScreen;
    private final Music statShownMusic;

    public TimeVisibleState(LaPandemia main, ResultsScreen resultsScreen) {
        this.resultsScreen = resultsScreen;
        statShownMusic = main.getAssetManager().get("audio/stat-shown.wav");
    }

    @Override
    public void enter() {
        statShownMusic.setLooping(false);
        statShownMusic.play();
        resultsScreen.getResultsView().setTimeVisible(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0xFF, 0xFF, 0xFF, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        resultsScreen.draw();
        if (!statShownMusic.isPlaying()) {
            resultsScreen.setState(ResultsScreen.STATE_INCREASING_TIME);
        }
    }
}
