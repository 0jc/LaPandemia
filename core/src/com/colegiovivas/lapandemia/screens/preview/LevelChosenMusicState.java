package com.colegiovivas.lapandemia.screens.preview;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.screens.MultistateScreen;

/**
 * Estado en el que se encuentra la pantalla mientras reproduce el sonido de que se
 * ha elegido empezar una partida en el mapa elegido por el usuario.
 */
public class LevelChosenMusicState extends MultistateScreen.StateAdapter {
    private final LaPandemia main;
    private final PreviewScreen previewScreen;

    public LevelChosenMusicState(LaPandemia main, PreviewScreen previewScreen) {
        this.main = main;
        this.previewScreen = previewScreen;
    }

    @Override
    public void enter() {
        previewScreen.getBackgroundMusic().stop();
        previewScreen.getLevelChosenMusic().play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0xFF, 0x88, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        previewScreen.draw();

        if (!previewScreen.getLevelChosenMusic().isPlaying()) {
            previewScreen.setState(PreviewScreen.STATE_CLOSING);
        }
    }
}
