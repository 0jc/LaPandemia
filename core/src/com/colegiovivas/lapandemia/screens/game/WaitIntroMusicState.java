package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.screens.MultistateScreen;

/**
 * Estado en el que se espera a que la música introductoria se termine
 * de reproducir.
 */
public class WaitIntroMusicState extends MultistateScreen.StateAdapter {
    private final GameScreen gameScreen;

    public WaitIntroMusicState(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0xFF, 0x88, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameScreen.draw();

        if (!gameScreen.getIntroMusic().isPlaying()) {
            gameScreen.setState(GameScreen.STATE_WAIT_AFTER_ZOOM_IN);
        }
    }
}
