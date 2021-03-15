package com.colegiovivas.lapandemia.screens.results;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.screens.MultistateScreen;

/**
 * Estado en el que se hace visible la estadística de la puntuación (aunque
 * todavía con su valor inicial de 0) y se reproduce el efecto sonoro relevante.
 */
public class PaperCountVisibleState extends MultistateScreen.StateAdapter {
    private final ResultsScreen resultsScreen;
    private final Music statShownMusic;

    public PaperCountVisibleState(LaPandemia main, ResultsScreen resultsScreen) {
        this.resultsScreen = resultsScreen;
        statShownMusic = main.assetManager.get("audio/stat-shown.wav");
    }

    @Override
    public void enter() {
        statShownMusic.setLooping(false);
        statShownMusic.play();
        resultsScreen.getResultsView().setPaperCountVisible(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0xFF, 0xFF, 0xFF, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        resultsScreen.draw();
        if (!statShownMusic.isPlaying()) {
            resultsScreen.setState(ResultsScreen.STATE_INCREASING_PAPER_COUNT);
        }
    }
}
